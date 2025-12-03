package app.service;

import app.dto.pontocoleta.PontoColetaRequestDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;
import app.entity.PontoColeta;
import app.enums.TipoResiduo;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.PontoColetaMapper;
import app.repository.BairroRepository;
import app.repository.PontoColetaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class PontoColetaService {

    private final PontoColetaRepository pontoColetaRepository;
    private final BairroRepository bairroRepository;
    private final PontoColetaMapper pontoColetaMapper;

    public PontoColetaService(PontoColetaRepository pontoColetaRepository,
                              BairroRepository bairroRepository,
                              PontoColetaMapper pontoColetaMapper) {
        this.pontoColetaRepository = pontoColetaRepository;
        this.bairroRepository = bairroRepository;
        this.pontoColetaMapper = pontoColetaMapper;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarTodos() {
        return pontoColetaRepository.findAll(Sort.by("nome").ascending())
                .stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarPorBairro(Long bairroId) {
        Bairro bairro = bairroRepository.findById(bairroId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro não encontrado: " + bairroId));

        return pontoColetaRepository.findByBairro(bairro)
                .stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PontoColetaResponseDTO> listarPorNome(String nome) {
        return pontoColetaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PontoColetaResponseDTO buscarPorId(Long id) {
        PontoColeta ponto = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + id));

        return pontoColetaMapper.toResponseDTO(ponto);
    }

    // ========= CRIAR / ATUALIZAR / EXCLUIR =========

    @Transactional
    public PontoColetaResponseDTO criar(PontoColetaRequestDTO dto) {
        validar(dto);

        Bairro bairro = bairroRepository.findById(dto.bairroId())
                .orElseThrow(() -> new NegocioException("Bairro não encontrado."));

        PontoColeta ponto = pontoColetaMapper.toEntity(dto);
        ponto.setBairro(bairro);
        ponto.setTiposResiduo(new HashSet<TipoResiduo>(dto.tiposResiduos()));

        PontoColeta salvo = pontoColetaRepository.save(ponto);
        return pontoColetaMapper.toResponseDTO(salvo);
    }

    @Transactional
    public PontoColetaResponseDTO atualizar(Long id, PontoColetaRequestDTO dto) {
        validar(dto);

        PontoColeta existente = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + id));

        Bairro bairro = bairroRepository.findById(dto.bairroId())
                .orElseThrow(() -> new NegocioException("Bairro não encontrado."));

        existente.setBairro(bairro);
        existente.setNome(dto.nome());
        existente.setResponsavel(dto.responsavel());
        existente.setTelefone(dto.telefone());
        existente.setEmail(dto.email());
        existente.setEndereco(dto.endereco());
        existente.setHoraEntrada(dto.horaEntrada());
        existente.setHoraSaida(dto.horaSaida());
        existente.setQuantidadeResiduosKg(dto.quantidadeResiduosKg());
        existente.setTiposResiduo(new HashSet<TipoResiduo>(dto.tiposResiduos()));

        PontoColeta atualizado = pontoColetaRepository.save(existente);
        return pontoColetaMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        PontoColeta ponto = pontoColetaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ponto de coleta não encontrado: " + id));

        pontoColetaRepository.delete(ponto);
    }

    // ========= AUXILIAR =========

    private void validar(PontoColetaRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new NegocioException("O nome do ponto de coleta é obrigatório.");
        }
        if (dto.bairroId() == null) {
            throw new NegocioException("O bairro é obrigatório.");
        }
        if (dto.tiposResiduos() == null || dto.tiposResiduos().isEmpty()) {
            throw new NegocioException("Informe pelo menos um tipo de resíduo.");
        }
        if (dto.quantidadeResiduosKg() == null || dto.quantidadeResiduosKg() < 0) {
            throw new NegocioException("A quantidade de resíduos deve ser informada e não pode ser negativa.");
        }
    }
}
