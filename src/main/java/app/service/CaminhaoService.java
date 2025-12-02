package app.service;

import app.dto.caminhao.CaminhaoRequestDTO;
import app.dto.caminhao.CaminhaoResponseDTO;
import app.entity.Caminhao;
import app.entity.Motorista;
import app.enums.StatusCaminhao;
import app.enums.TipoResiduo;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.CaminhaoMapper;
import app.repository.CaminhaoRepository;
import app.repository.MotoristaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class CaminhaoService {

    private final CaminhaoRepository caminhaoRepository;
    private final MotoristaRepository motoristaRepository;
    private final CaminhaoMapper caminhaoMapper;

    public CaminhaoService(CaminhaoRepository caminhaoRepository,
                           MotoristaRepository motoristaRepository,
                           CaminhaoMapper caminhaoMapper) {
        this.caminhaoRepository = caminhaoRepository;
        this.motoristaRepository = motoristaRepository;
        this.caminhaoMapper = caminhaoMapper;
    }

    // ===== CONSULTAS =====

    @Transactional(readOnly = true)
    public List<CaminhaoResponseDTO> listarTodos() {
        return caminhaoRepository.findAll(Sort.by("placa").ascending())
                .stream()
                .map(caminhaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CaminhaoResponseDTO> listarPorStatus(StatusCaminhao status) {
        return caminhaoRepository.findByStatus(status)
                .stream()
                .map(caminhaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CaminhaoResponseDTO buscarPorPlaca(String placa) {
        Caminhao caminhao = caminhaoRepository.findByPlacaIgnoreCase(placa)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Caminhão não encontrado: " + placa));
        return caminhaoMapper.toResponseDTO(caminhao);
    }

    // ===== CRIAR / ATUALIZAR / EXCLUIR =====

    @Transactional
    public CaminhaoResponseDTO criar(CaminhaoRequestDTO dto) {
        validar(dto);

        if (caminhaoRepository.existsByPlacaIgnoreCase(dto.placa())) {
            throw new NegocioException("Já existe um caminhão cadastrado com essa placa.");
        }

        Motorista motorista = motoristaRepository.findById(dto.motoristaCpf())
                .orElseThrow(() -> new NegocioException("Motorista não encontrado."));

        Caminhao caminhao = caminhaoMapper.toEntity(dto);
        caminhao.setMotorista(motorista);
        caminhao.setTiposResiduo(new HashSet<>(dto.tiposResiduos()));

        Caminhao salvo = caminhaoRepository.save(caminhao);
        return caminhaoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public CaminhaoResponseDTO atualizar(String placa, CaminhaoRequestDTO dto) {
        validar(dto);

        Caminhao existente = caminhaoRepository.findByPlacaIgnoreCase(placa)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Caminhão não encontrado: " + placa));

        if (!dto.placa().equalsIgnoreCase(placa)) {
            throw new NegocioException("Não é permitido alterar a placa do caminhão.");
        }

        Motorista motorista = motoristaRepository.findById(dto.motoristaCpf())
                .orElseThrow(() -> new NegocioException("Motorista não encontrado."));

        existente.setMotorista(motorista);
        existente.setCapacidadeKg(dto.capacidadeKg());
        existente.setStatus(dto.status());
        existente.setTiposResiduo(new HashSet<TipoResiduo>(dto.tiposResiduos()));

        Caminhao atualizado = caminhaoRepository.save(existente);
        return caminhaoMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(String placa) {
        Caminhao caminhao = caminhaoRepository.findByPlacaIgnoreCase(placa)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Caminhão não encontrado: " + placa));

        caminhaoRepository.delete(caminhao);
    }

    // ===== AUXILIAR =====

    private void validar(CaminhaoRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.placa() == null || dto.placa().isBlank()) {
            throw new NegocioException("A placa é obrigatória.");
        }
        if (dto.motoristaCpf() == null || dto.motoristaCpf().isBlank()) {
            throw new NegocioException("O CPF do motorista é obrigatório.");
        }
        if (dto.capacidadeKg() == null || dto.capacidadeKg() <= 0) {
            throw new NegocioException("A capacidade deve ser maior que zero.");
        }
        if (dto.status() == null) {
            throw new NegocioException("O status do caminhão é obrigatório.");
        }
        if (dto.tiposResiduos() == null || dto.tiposResiduos().isEmpty()) {
            throw new NegocioException("Informe pelo menos um tipo de resíduo.");
        }
    }
}
