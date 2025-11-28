package app.service;

import app.dto.rota.RotaRequestDTO;
import app.dto.rota.RotaResponseDTO;
import app.entity.Caminhao;
import app.entity.PontoColeta;
import app.entity.Rota;
import app.entity.RuaConexao;
import app.entity.TipoResiduoModel;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.RotaMapper;
import app.patterns.RotaFactory;
import app.repository.CaminhaoRepository;
import app.repository.PontoColetaRepository;
import app.repository.RotaRepository;
import app.repository.TipoResiduoModelRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Serviço responsável pela criação e consulta de rotas.
 * A montagem da entidade Rota é delegada ao RotaFactory (Factory Method).
 */
@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final PontoColetaRepository pontoColetaRepository;
    private final TipoResiduoModelRepository tipoResiduoModelRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final DijkstraService dijkstraService;
    private final GrafoService grafoService;
    private final RotaMapper rotaMapper;

    public RotaService(RotaRepository rotaRepository,
                       PontoColetaRepository pontoColetaRepository,
                       TipoResiduoModelRepository tipoResiduoModelRepository,
                       CaminhaoRepository caminhaoRepository,
                       DijkstraService dijkstraService,
                       GrafoService grafoService,
                       RotaMapper rotaMapper) {
        this.rotaRepository = rotaRepository;
        this.pontoColetaRepository = pontoColetaRepository;
        this.tipoResiduoModelRepository = tipoResiduoModelRepository;
        this.caminhaoRepository = caminhaoRepository;
        this.dijkstraService = dijkstraService;
        this.grafoService = grafoService;
        this.rotaMapper = rotaMapper;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<RotaResponseDTO> listarTodas() {
        return rotaRepository.findAll(Sort.by("dataCriacao").descending())
                .stream()
                .map(rotaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RotaResponseDTO buscarPorId(Long id) {
        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Rota não encontrada: " + id));
        return rotaMapper.toResponseDTO(rota);
    }

    // ========= CRIAÇÃO DE ROTA (MÚLTIPLAS PARADAS) =========

    @Transactional
    public RotaResponseDTO criar(RotaRequestDTO dto) {
        validar(dto);

        Caminhao caminhao = caminhaoRepository.findById(dto.caminhaoPlaca())
                .orElseThrow(() -> new NegocioException("Caminhão não encontrado."));

        TipoResiduoModel tipoResiduo = tipoResiduoModelRepository.findById(dto.tipoResiduoId())
                .orElseThrow(() -> new NegocioException("Tipo de resíduo não encontrado."));

        // Carrega pontos de coleta preservando a ordem informada
        List<PontoColeta> pontos = carregarPontosNaOrdem(dto.pontosColetaIds());

        validarCompatibilidadeTipoResiduo(caminhao, tipoResiduo, pontos);

        double pesoTotal = calcularPesoTotal(pontos);
        validarCapacidade(caminhao, pesoTotal);

        // Monta lista de bairros únicos (na ordem de seleção)
        List<Long> bairrosIds = extrairBairrosOrdenados(pontos);

        // Usa Adapter + Dijkstra para definir a ordem ideal das paradas,
        // incluindo o retorno ao ponto inicial.
        List<Long> ordemBairros = dijkstraService.calcularRotaMultiParadas(bairrosIds);

        // Para cada par consecutivo (origem -> destino), calcula o menor caminho
        // e monta a lista de caminhos (um por trecho), preservando a ordem.
        List<List<RuaConexao>> caminhosPorTrecho = montarCaminhosPorTrecho(ordemBairros);

        // Factory Method: delega criação da Rota e dos TrechoRota ao RotaFactory
        Rota rota = RotaFactory.criarRota(
                dto.nome(),
                tipoResiduo,
                caminhao,
                pesoTotal,
                caminhosPorTrecho
        );

        // Associa os pontos de coleta selecionados à rota
        rota.setPontosColeta(new HashSet<>(pontos));

        Rota salva = rotaRepository.save(rota);
        return rotaMapper.toResponseDTO(salva);
    }

    @Transactional
    public void excluir(Long id) {
        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Rota não encontrada: " + id));

        // Caso no futuro seja necessário impedir exclusão se houver itinerário vinculado,
        // a validação pode ser feita aqui antes do delete.
        rotaRepository.delete(rota);
    }

    // ========= AUXILIARES DE REGRA DE NEGÓCIO =========

    private void validar(RotaRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new NegocioException("O nome da rota é obrigatório.");
        }
        if (dto.caminhaoPlaca() == null || dto.caminhaoPlaca().isBlank()) {
            throw new NegocioException("A placa do caminhão é obrigatória.");
        }
        if (dto.tipoResiduoId() == null) {
            throw new NegocioException("O tipo de resíduo é obrigatório.");
        }
        if (dto.pontosColetaIds() == null || dto.pontosColetaIds().isEmpty()) {
            throw new NegocioException("Informe pelo menos um ponto de coleta.");
        }
    }

    private List<PontoColeta> carregarPontosNaOrdem(List<Long> pontosIds) {
        Map<Long, PontoColeta> mapa = new HashMap<>();
        List<PontoColeta> encontrados = pontoColetaRepository.findAllById(pontosIds);

        if (encontrados.size() != pontosIds.size()) {
            throw new NegocioException("Um ou mais pontos de coleta não foram encontrados.");
        }

        for (PontoColeta p : encontrados) {
            mapa.put(p.getId(), p);
        }

        List<PontoColeta> ordenados = new ArrayList<>();
        for (Long id : pontosIds) {
            PontoColeta p = mapa.get(id);
            if (p == null) {
                throw new NegocioException("Ponto de coleta não encontrado: " + id);
            }
            ordenados.add(p);
        }
        return ordenados;
    }

    private void validarCompatibilidadeTipoResiduo(Caminhao caminhao,
                                                   TipoResiduoModel tipoResiduo,
                                                   List<PontoColeta> pontos) {

        if (!caminhao.getTiposResiduo().contains(tipoResiduo)) {
            throw new NegocioException("O caminhão não está habilitado para o tipo de resíduo selecionado.");
        }

        for (PontoColeta p : pontos) {
            if (!p.getTiposResiduo().contains(tipoResiduo)) {
                throw new NegocioException(
                        "O ponto de coleta \"" + p.getNome() + "\" não recebe o tipo de resíduo selecionado."
                );
            }
        }
    }

    private double calcularPesoTotal(List<PontoColeta> pontos) {
        return pontos.stream()
                .mapToInt(pc -> pc.getQuantidadeResiduosKg() != null ? pc.getQuantidadeResiduosKg() : 0)
                .sum();
    }

    private void validarCapacidade(Caminhao caminhao, double pesoTotal) {
        Integer capacidade = caminhao.getCapacidadeKg();
        if (capacidade != null && pesoTotal > capacidade) {
            throw new NegocioException("Capacidade do caminhão excedida para os pontos selecionados.");
        }
    }

    /**
     * Extrai os IDs de bairro da lista de pontos,
     * mantendo a ordem e removendo duplicados.
     */
    private List<Long> extrairBairrosOrdenados(List<PontoColeta> pontos) {
        List<Long> bairros = new ArrayList<>();
        Set<Long> vistos = new HashSet<>();

        for (PontoColeta p : pontos) {
            if (p.getBairro() == null || p.getBairro().getId() == null) {
                throw new NegocioException("Ponto de coleta sem bairro associado: " + p.getNome());
            }
            Long bairroId = p.getBairro().getId();
            if (vistos.add(bairroId)) {
                bairros.add(bairroId);
            }
        }

        if (bairros.size() < 1) {
            throw new NegocioException("Não foi possível determinar os bairros da rota.");
        }

        return bairros;
    }

    /**
     * Monta a lista de caminhos (cada caminho é um trecho) na ordem
     * calculada pelo DijkstraService:
     *  - primeiro trecho: ponto de partida → ponto mais próximo
     *  - ...
     *  - último trecho: último ponto → ponto de origem (volta)
     */
    private List<List<RuaConexao>> montarCaminhosPorTrecho(List<Long> ordemBairros) {
        if (ordemBairros == null || ordemBairros.size() < 2) {
            throw new NegocioException("A lista de bairros da rota é insuficiente.");
        }

        List<List<RuaConexao>> caminhos = new ArrayList<>();

        for (int i = 0; i < ordemBairros.size() - 1; i++) {
            Long origemId = ordemBairros.get(i);
            Long destinoId = ordemBairros.get(i + 1);

            List<RuaConexao> caminho = grafoService.calcularMenorCaminho(origemId, destinoId);
            if (caminho == null || caminho.isEmpty()) {
                throw new NegocioException("Não foi encontrado caminho entre os bairros selecionados.");
            }

            caminhos.add(caminho);
        }

        return caminhos;
    }
}
