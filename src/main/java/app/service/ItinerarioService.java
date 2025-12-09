package app.service;

import app.dto.itinerario.ItinerarioRequestDTO;
import app.dto.itinerario.ItinerarioResponseDTO;
import app.entity.Caminhao;
import app.entity.Itinerario;
import app.entity.Motorista;
import app.entity.PontoColeta;
import app.entity.Rota;
import app.enums.StatusCaminhao;
import app.enums.StatusMotorista;
import app.enums.TipoResiduo;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.ItinerarioMapper;
import app.patterns.AbstractItinerarioTemplate;
import app.repository.ItinerarioRepository;
import app.repository.RotaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementação concreta do Template Method para criação de itinerários.
 * Usa AbstractItinerarioTemplate como classe base.
 */
@Service
public class ItinerarioService extends AbstractItinerarioTemplate {

    private final ItinerarioRepository itinerarioRepository;
    private final RotaRepository rotaRepository;
    private final ItinerarioMapper itinerarioMapper;

    public ItinerarioService(ItinerarioRepository itinerarioRepository,
                             RotaRepository rotaRepository,
                             ItinerarioMapper itinerarioMapper) {
        this.itinerarioRepository = itinerarioRepository;
        this.rotaRepository = rotaRepository;
        this.itinerarioMapper = itinerarioMapper;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarTodos() {
        return itinerarioRepository.findAll()
                .stream()
                .map(itinerarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorData(String dataTexto) {
        if (dataTexto == null || dataTexto.isBlank()) {
            throw new NegocioException("A data é obrigatória.");
        }
        LocalDate data;
        try {
            data = LocalDate.parse(dataTexto);
        } catch (Exception e) {
            throw new NegocioException("Data inválida. Use o formato yyyy-MM-dd.");
        }

        return itinerarioRepository.findByDataAgendamentoBetween(data, data)
                .stream()
                .map(itinerarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorCaminhao(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new NegocioException("A placa do caminhão é obrigatória.");
        }

        return itinerarioRepository.findByRota_Caminhao_Placa(placa)
                .stream()
                .map(itinerarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorCaminhaoEPeriodo(String placa,
                                                                 String inicioTexto,
                                                                 String fimTexto) {
        if (placa == null || placa.isBlank()) {
            throw new NegocioException("A placa do caminhão é obrigatória.");
        }
        if (inicioTexto == null || inicioTexto.isBlank()
                || fimTexto == null || fimTexto.isBlank()) {
            throw new NegocioException("As datas de início e fim são obrigatórias.");
        }

        LocalDate inicio;
        LocalDate fim;
        try {
            inicio = LocalDate.parse(inicioTexto);
            fim = LocalDate.parse(fimTexto);
        } catch (Exception e) {
            throw new NegocioException("Datas inválidas. Use o formato yyyy-MM-dd.");
        }

        return itinerarioRepository
                .findByRota_Caminhao_PlacaAndDataAgendamentoBetween(placa, inicio, fim)
                .stream()
                .map(itinerarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItinerarioResponseDTO> listarPorTipoResiduo(TipoResiduo tipoResiduo) {
        if (tipoResiduo == null) {
            throw new NegocioException("O tipo de resíduo é obrigatório.");
        }

        return itinerarioRepository.findByTipoResiduo(tipoResiduo)
                .stream()
                .map(itinerarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItinerarioResponseDTO buscarPorId(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Itinerário não encontrado: " + id));
        return itinerarioMapper.toResponseDTO(itinerario);
    }

    // ========= CRIAÇÃO / CANCELAMENTO =========

    @Transactional
    public ItinerarioResponseDTO criar(ItinerarioRequestDTO dto) {
        Itinerario itinerario = criarItinerario(dto);
        return itinerarioMapper.toResponseDTO(itinerario);
    }

    @Transactional
    public ItinerarioResponseDTO cancelar(Long id) {
        Itinerario itinerario = itinerarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Itinerário não encontrado: " + id));

        if (Boolean.FALSE.equals(itinerario.getAtivo())) {
            throw new NegocioException("O itinerário já está inativo.");
        }

        itinerario.setAtivo(false);
        Itinerario salvo = itinerarioRepository.save(itinerario);
        return itinerarioMapper.toResponseDTO(salvo);
    }

    // ========= IMPLEMENTAÇÃO DO TEMPLATE METHOD =========

    @Override
    protected void validarDadosBasicos(ItinerarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.rotaId() == null) {
            throw new NegocioException("A rota é obrigatória.");
        }
        if (dto.data() == null || dto.data().isBlank()) {
            throw new NegocioException("A data é obrigatória.");
        }
        try {
            LocalDate.parse(dto.data());
        } catch (Exception e) {
            throw new NegocioException("Data inválida. Use o formato yyyy-MM-dd.");
        }
    }

    @Override
    protected void validarCaminhaoDisponivel(ItinerarioRequestDTO dto) {
        Rota rota = rotaRepository.findById(dto.rotaId())
                .orElseThrow(() -> new NegocioException("Rota não encontrada."));

        Caminhao caminhao = rota.getCaminhao();
        if (caminhao == null) {
            throw new NegocioException("A rota selecionada não possui caminhão associado.");
        }

        // status caminhão/motorista
        if (caminhao.getStatus() == null || caminhao.getStatus() != StatusCaminhao.ATIVO) {
            throw new NegocioException("Não é possível criar itinerário para caminhão com status " + caminhao.getStatus() + ".");
        }

        Motorista motorista = caminhao.getMotorista();
        if (motorista == null) {
            throw new NegocioException("O caminhão selecionado não possui motorista associado.");
        }
        if (motorista.getStatus() == null || motorista.getStatus() != StatusMotorista.ATIVO) {
            throw new NegocioException("Não é possível criar itinerário para motorista com status " + motorista.getStatus() + ".");
        }

        LocalDate data = LocalDate.parse(dto.data());

        // caminhão não pode ter dois itinerários no mesmo dia
        if (itinerarioRepository.existsByCaminhaoAndData(caminhao.getPlaca(), data)) {
            throw new NegocioException("O caminhão já possui itinerário para a data informada.");
        }

        // motorista não pode ter dois itinerários no mesmo dia
        if (itinerarioRepository.existsByMotoristaAndData(motorista.getCpf(), data)) {
            throw new NegocioException("O motorista já possui itinerário para a data informada.");
        }

        // ponto de coleta não pode estar em dois itinerários no mesmo dia
        if (rota.getPontosColeta() != null && !rota.getPontosColeta().isEmpty()) {
            List<Long> pontosIds = rota.getPontosColeta().stream()
                    .map(PontoColeta::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (!pontosIds.isEmpty()
                    && itinerarioRepository.existsByPontoColetaAndData(pontosIds, data)) {
                throw new NegocioException("Um ou mais pontos de coleta desta rota já estão em outro itinerário na data informada.");
            }
        }
    }

    @Override
    protected void validarCapacidadeResiduos(ItinerarioRequestDTO dto) {
        Rota rota = rotaRepository.findById(dto.rotaId())
                .orElseThrow(() -> new NegocioException("Rota não encontrada."));

        Caminhao caminhao = rota.getCaminhao();
        if (caminhao == null) {
            throw new NegocioException("A rota selecionada não possui caminhão associado.");
        }

        // compatibilidade de resíduos caminhão/rota/pontos
        TipoResiduo tipoResiduo = rota.getTipoResiduo();
        if (tipoResiduo == null) {
            throw new NegocioException("A rota selecionada não possui tipo de resíduo definido.");
        }

        if (!caminhao.getTiposResiduo().contains(tipoResiduo)) {
            throw new NegocioException("O caminhão não está habilitado para o tipo de resíduo da rota selecionada.");
        }

        if (rota.getPontosColeta() != null && !rota.getPontosColeta().isEmpty()) {
            for (PontoColeta p : rota.getPontosColeta()) {
                if (!p.getTiposResiduo().contains(tipoResiduo)) {
                    throw new NegocioException("O ponto de coleta \"" + p.getNome() + "\" não recebe o tipo de resíduo da rota selecionada.");
                }
            }
        }

        // capacidade x peso total
        Double pesoTotal = rota.getPesoTotal() != null ? rota.getPesoTotal() : 0.0;
        Integer capacidadeKg = caminhao.getCapacidadeKg();

        if (capacidadeKg != null && pesoTotal > capacidadeKg) {
            throw new NegocioException("Capacidade do caminhão excedida para a rota selecionada.");
        }
    }

    @Override
    protected void montarRota(ItinerarioRequestDTO dto) {
        // Rota já está montada e persistida.
    }

    @Override
    @Transactional
    protected Itinerario salvarItinerario(ItinerarioRequestDTO dto) {
        Rota rota = rotaRepository.findById(dto.rotaId())
                .orElseThrow(() -> new NegocioException("Rota não encontrada."));

        LocalDate data = LocalDate.parse(dto.data());

        Itinerario itinerario = new Itinerario();
        itinerario.setRota(rota);
        itinerario.setDataAgendamento(data);
        itinerario.setDistanciaTotal(rota.getDistanciaTotal());
        itinerario.setAtivo(true);

        return itinerarioRepository.save(itinerario);
    }
}
