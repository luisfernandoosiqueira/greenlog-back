package app.service;

import app.dto.bairro.BairroRequestDTO;
import app.dto.bairro.BairroResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;
import app.entity.PontoColeta;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.BairroMapper;
import app.mapper.PontoColetaMapper;
import app.repository.BairroRepository;
import app.repository.PontoColetaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BairroService {

    private final BairroRepository bairroRepository;
    private final PontoColetaRepository pontoColetaRepository;
    private final BairroMapper bairroMapper;
    private final PontoColetaMapper pontoColetaMapper;

    public BairroService(BairroRepository bairroRepository,
                         PontoColetaRepository pontoColetaRepository,
                         BairroMapper bairroMapper,
                         PontoColetaMapper pontoColetaMapper) {
        this.bairroRepository = bairroRepository;
        this.pontoColetaRepository = pontoColetaRepository;
        this.bairroMapper = bairroMapper;
        this.pontoColetaMapper = pontoColetaMapper;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<BairroResponseDTO> listarTodos() {
        List<Bairro> bairros = bairroRepository.findAll(Sort.by("nome").ascending());
        return bairros.stream()
                .map(this::toResponseComPontos)
                .toList();
    }

    @Transactional(readOnly = true)
    public BairroResponseDTO buscarPorId(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro não encontrado: " + id));
        return toResponseComPontos(bairro);
    }

    // ========= CRIAR / ATUALIZAR / EXCLUIR =========

    @Transactional
    public BairroResponseDTO criar(BairroRequestDTO dto) {
        validar(dto);

        if (bairroRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new NegocioException("Já existe um bairro cadastrado com esse nome.");
        }

        Bairro bairro = bairroMapper.toEntity(dto);
        Bairro salvo = bairroRepository.save(bairro);
        return toResponseComPontos(salvo);
    }

    @Transactional
    public BairroResponseDTO atualizar(Long id, BairroRequestDTO dto) {
        validar(dto);

        Bairro existente = bairroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro não encontrado: " + id));

        if (!existente.getNome().equalsIgnoreCase(dto.nome())
                && bairroRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new NegocioException("Já existe um bairro cadastrado com esse nome.");
        }

        existente.setNome(dto.nome());
        Bairro atualizado = bairroRepository.save(existente);
        return toResponseComPontos(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        Bairro bairro = bairroRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bairro não encontrado: " + id));

        List<PontoColeta> pontos = pontoColetaRepository.findByBairro(bairro);
        if (!pontos.isEmpty()) {
            throw new NegocioException("Não é possível excluir o bairro, pois há pontos de coleta vinculados.");
        }

        bairroRepository.delete(bairro);
    }

    // ========= AUXILIAR =========

    private void validar(BairroRequestDTO dto) {
        if (dto == null || dto.nome() == null || dto.nome().isBlank()) {
            throw new NegocioException("O nome do bairro é obrigatório.");
        }
    }

    private BairroResponseDTO toResponseComPontos(Bairro bairro) {
        List<PontoColeta> pontos = pontoColetaRepository.findByBairro(bairro);

        List<PontoColetaResponseDTO> pontosDTO = pontos.stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();

        return bairroMapper.toResponseDTO(bairro, pontosDTO);
    }
}
