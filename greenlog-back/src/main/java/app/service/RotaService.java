package app.service;

import app.dto.rota.RotaRequestDTO;
import app.dto.rota.RotaResponseDTO;
import app.entity.Caminhao;
import app.entity.PontoColeta;
import app.entity.Rota;
import app.entity.RuaConexao;
import app.entity.TrechoRota;
import app.enums.StatusCaminhao;
import app.enums.StatusMotorista;
import app.enums.TipoResiduo;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.RotaMapper;
import app.patterns.RotaFactory;
import app.repository.CaminhaoRepository;
import app.repository.ItinerarioRepository;
import app.repository.PontoColetaRepository;
import app.repository.RotaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Serviço responsável pela criação e consulta de rotas.
 */
@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final PontoColetaRepository pontoColetaRepository;
    private final CaminhaoRepository caminhaoRepository;
    private final DijkstraService dijkstraService;
    private final GrafoService grafoService;
    private final RotaMapper rotaMapper;
    private final ItinerarioRepository itinerarioRepository;

    public RotaService(RotaRepository rotaRepository,
                       PontoColetaRepository pontoColetaRepository,
                       CaminhaoRepository caminhaoRepository,
                       DijkstraService dijkstraService,
                       GrafoService grafoService,
                       RotaMapper rotaMapper,
                       ItinerarioRepository itinerarioRepository) {
        this.rotaRepository = rotaRepository;
        this.pontoColetaRepository = pontoColetaRepository;
        this.caminhaoRepository = caminhaoRepository;
        this.dijkstraService = dijkstraService;
        this.grafoService = grafoService;
        this.rotaMapper = rotaMapper;
        this.itinerarioRepository = itinerarioRepository;
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

        Caminhao caminhao = caminhaoRepository.findByPlacaIgnoreCase(dto.caminhaoPlaca())
                .orElseThrow(() -> new NegocioException("Caminhão não encontrado."));

        validarCaminhaoAtivoComMotoristaAtivo(caminhao);

        TipoResiduo tipoResiduo = dto.tipoResiduo();

        List<PontoColeta> pontos = carregarPontosNaOrdem(dto.pontosColetaIds());

        validarCompatibilidadeTipoResiduo(caminhao, tipoResiduo, pontos);

        double pesoTotal = calcularPesoTotal(pontos);
        validarCapacidade(caminhao, pesoTotal);

        List<Long> bairrosIds = extrairBairrosOrdenados(pontos);

        List<Long> ordemBairros = dijkstraService.calcularRotaMultiParadas(bairrosIds);

        List<List<RuaConexao>> caminhosPorTrecho = montarCaminhosPorTrecho(ordemBairros);

        Rota rota = RotaFactory.criarRota(
                dto.nome(),
                tipoResiduo,
                caminhao,
                pesoTotal,
                caminhosPorTrecho
        );

        rota.setPontosColeta(new HashSet<>(pontos));

        Rota salva = rotaRepository.save(rota);
        return rotaMapper.toResponseDTO(salva);
    }

    // ========= EDIÇÃO DE ROTA =========

    @Transactional
    public RotaResponseDTO atualizar(Long id, RotaRequestDTO dto) {
        Rota existente = rotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Rota não encontrada: " + id));

        validar(dto);

        Caminhao caminhao = caminhaoRepository.findByPlacaIgnoreCase(dto.caminhaoPlaca())
                .orElseThrow(() -> new NegocioException("Caminhão não encontrado."));

        validarCaminhaoAtivoComMotoristaAtivo(caminhao);

        TipoResiduo tipoResiduo = dto.tipoResiduo();

        List<PontoColeta> pontos = carregarPontosNaOrdem(dto.pontosColetaIds());

        validarCompatibilidadeTipoResiduo(caminhao, tipoResiduo, pontos);

        double pesoTotal = calcularPesoTotal(pontos);
        validarCapacidade(caminhao, pesoTotal);

        List<Long> bairrosIds = extrairBairrosOrdenados(pontos);
        List<Long> ordemBairros = dijkstraService.calcularRotaMultiParadas(bairrosIds);
        List<List<RuaConexao>> caminhosPorTrecho = montarCaminhosPorTrecho(ordemBairros);

        Rota rotaCalculada = RotaFactory.criarRota(
                dto.nome(),
                tipoResiduo,
                caminhao,
                pesoTotal,
                caminhosPorTrecho
        );

        existente.setNome(rotaCalculada.getNome());
        existente.setTipoResiduo(rotaCalculada.getTipoResiduo());
        existente.setCaminhao(rotaCalculada.getCaminhao());
        existente.setPesoTotal(rotaCalculada.getPesoTotal());
        existente.setDistanciaTotal(rotaCalculada.getDistanciaTotal());

        List<TrechoRota> novosTrechos = new ArrayList<>();
        for (TrechoRota trecho : rotaCalculada.getTrechos()) {
            trecho.setRota(existente);
            novosTrechos.add(trecho);
        }
        existente.getTrechos().clear();
        existente.getTrechos().addAll(novosTrechos);

        existente.setPontosColeta(new HashSet<>(pontos));

        Rota atualizada = rotaRepository.save(existente);
        return rotaMapper.toResponseDTO(atualizada);
    }

    @Transactional
    public void excluir(Long id) {
        Rota rota = rotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Rota não encontrada: " + id));

        if (itinerarioRepository.existsByRota(rota)) {
            throw new NegocioException("Não é possível excluir uma rota vinculada a itinerários.");
        }

        rotaRepository.delete(rota);
    }

    // ========= AUXILIARES =========

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
        if (dto.tipoResiduo() == null) {
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
                                                   TipoResiduo tipoResiduo,
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

        if (bairros.isEmpty()) {
            throw new NegocioException("Não foi possível determinar os bairros da rota.");
        }

        return bairros;
    }

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

    // valida status de caminhão e motorista
    private void validarCaminhaoAtivoComMotoristaAtivo(Caminhao caminhao) {
        if (caminhao.getStatus() == null || caminhao.getStatus() != StatusCaminhao.ATIVO) {
            throw new NegocioException("Não é possível utilizar caminhão com status " + caminhao.getStatus() + " em uma rota.");
        }
        var motorista = caminhao.getMotorista();
        if (motorista == null) {
            throw new NegocioException("O caminhão selecionado não possui motorista associado.");
        }
        if (motorista.getStatus() == null || motorista.getStatus() != StatusMotorista.ATIVO) {
            throw new NegocioException("Não é possível utilizar motorista com status " + motorista.getStatus() + " em uma rota.");
        }
    }
}
