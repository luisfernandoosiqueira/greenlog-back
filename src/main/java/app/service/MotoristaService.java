package app.service;

import app.dto.motorista.MotoristaRequestDTO;
import app.dto.motorista.MotoristaResponseDTO;
import app.entity.Motorista;
import app.enums.StatusMotorista;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.MotoristaMapper;
import app.repository.MotoristaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MotoristaService {

    private final MotoristaRepository motoristaRepository;
    private final MotoristaMapper motoristaMapper;

    public MotoristaService(MotoristaRepository motoristaRepository,
                            MotoristaMapper motoristaMapper) {
        this.motoristaRepository = motoristaRepository;
        this.motoristaMapper = motoristaMapper;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<MotoristaResponseDTO> listarTodos() {
        return motoristaRepository.findAll(Sort.by("nome").ascending())
                .stream()
                .map(motoristaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MotoristaResponseDTO> listarPorStatus(StatusMotorista status) {
        return motoristaRepository.findByStatus(status)
                .stream()
                .map(motoristaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MotoristaResponseDTO> listarPorNome(String nome) {
        return motoristaRepository.findByNomeContainingIgnoreCase(nome)
                .stream()
                .map(motoristaMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public MotoristaResponseDTO buscarPorCpf(String cpf) {
        Motorista motorista = motoristaRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado: " + cpf));
        return motoristaMapper.toResponseDTO(motorista);
    }

    // ========= CRIAR / ATUALIZAR / EXCLUIR =========

    @Transactional
    public MotoristaResponseDTO criar(MotoristaRequestDTO dto) {
        validar(dto);

        if (motoristaRepository.existsById(dto.cpf())) {
            throw new NegocioException("Já existe um motorista cadastrado com esse CPF.");
        }

        Motorista motorista = motoristaMapper.toEntity(dto);
        Motorista salvo = motoristaRepository.save(motorista);
        return motoristaMapper.toResponseDTO(salvo);
    }

    @Transactional
    public MotoristaResponseDTO atualizar(String cpf, MotoristaRequestDTO dto) {
        validar(dto);

        Motorista existente = motoristaRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado: " + cpf));

        if (dto.cpf() != null && !dto.cpf().equals(cpf)) {
            throw new NegocioException("Não é permitido alterar o CPF do motorista.");
        }

        existente.setNome(dto.nome());
        existente.setTelefone(dto.telefone());
        existente.setStatus(dto.status());

        if (dto.data() != null && !dto.data().isBlank()) {
            existente.setData(LocalDate.parse(dto.data()));
        }

        Motorista atualizado = motoristaRepository.save(existente);
        return motoristaMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(String cpf) {
        Motorista motorista = motoristaRepository.findById(cpf)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Motorista não encontrado: " + cpf));

        // Caso seja necessário impedir exclusão se houver vínculo com caminhões,
        // a validação pode ser incluída aqui futuramente.
        motoristaRepository.delete(motorista);
    }

    // ========= AUXILIAR =========

    private void validar(MotoristaRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.cpf() == null || dto.cpf().isBlank()) {
            throw new NegocioException("O CPF é obrigatório.");
        }
        if (dto.nome() == null || dto.nome().isBlank()) {
            throw new NegocioException("O nome é obrigatório.");
        }
        if (dto.data() == null || dto.data().isBlank()) {
            throw new NegocioException("A data é obrigatória.");
        }
        if (dto.telefone() == null || dto.telefone().isBlank()) {
            throw new NegocioException("O telefone é obrigatório.");
        }
        if (dto.status() == null) {
            throw new NegocioException("O status é obrigatório.");
        }
    }
}
