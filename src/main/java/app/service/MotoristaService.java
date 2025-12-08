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
import java.time.Period;
import java.time.format.DateTimeParseException;
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

        if (!validarCPF(dto.cpf())) {
            throw new NegocioException("O CPF informado é inválido.");
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

        // valida data de nascimento (formato ISO yyyy-MM-dd), não futura e idade mínima
        LocalDate dataNascimento;
        try {
            dataNascimento = LocalDate.parse(dto.data());
        } catch (DateTimeParseException e) {
            throw new NegocioException("Data de nascimento inválida. Use o formato yyyy-MM-dd.");
        }

        LocalDate hoje = LocalDate.now();

        if (dataNascimento.isAfter(hoje)) {
            throw new NegocioException("A data de nascimento não pode ser futura.");
        }

        int idade = Period.between(dataNascimento, hoje).getYears();
        if (idade < 18) {
            throw new NegocioException("O motorista deve ter pelo menos 18 anos.");
        }
    }

    // ========= VALIDAÇÃO DE CPF =========

    private static boolean validarCPF(String cpf) {
        if (cpf == null) {
            return false;
        }

        cpf = cpf.replaceAll("\\D", "");

        if (cpf.length() != 11) {
            return false;
        }

        boolean todosIguais = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        if (todosIguais) {
            return false;
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum = 0;
        int firstDigit, secondDigit;

        for (int i = 0, weight = 10; i < 9; i++, weight--) {
            sum += numbers[i] * weight;
        }
        firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) {
            firstDigit = 0;
        }

        sum = 0;
        for (int i = 0, weight = 11; i < 10; i++, weight--) {
            sum += numbers[i] * weight;
        }
        secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) {
            secondDigit = 0;
        }

        return firstDigit == numbers[9] && secondDigit == numbers[10];
    }

}
