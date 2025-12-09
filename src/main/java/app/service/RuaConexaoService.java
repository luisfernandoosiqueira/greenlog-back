package app.service;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.dto.ruaconexao.RuaConexaoRequestDTO;
import app.dto.ruaconexao.RuaConexaoResponseDTO;
import app.entity.Bairro;
import app.entity.RuaConexao;
import app.events.GrafoAtualizadoEvent;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.RuaConexaoMapper;
import app.repository.BairroRepository;
import app.repository.RuaConexaoRepository;

@Service
public class RuaConexaoService {

    private final RuaConexaoRepository ruaConexaoRepository;
    private final BairroRepository bairroRepository;
    private final RuaConexaoMapper ruaConexaoMapper;
    private final ApplicationEventPublisher eventPublisher;

    public RuaConexaoService(RuaConexaoRepository ruaConexaoRepository,
                             BairroRepository bairroRepository,
                             RuaConexaoMapper ruaConexaoMapper,
                             ApplicationEventPublisher eventPublisher) {
        this.ruaConexaoRepository = ruaConexaoRepository;
        this.bairroRepository = bairroRepository;
        this.ruaConexaoMapper = ruaConexaoMapper;
        this.eventPublisher = eventPublisher;
    }

    // ========= CONSULTAS =========

    @Transactional(readOnly = true)
    public List<RuaConexaoResponseDTO> listarTodas() {
        return ruaConexaoRepository.findAllComBairros()
                .stream()
                .map(ruaConexaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RuaConexaoResponseDTO> listarPorOrigem(Long origemId) {
        Bairro origem = bairroRepository.findById(origemId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Bairro de origem não encontrado: " + origemId));

        return ruaConexaoRepository.findByOrigem(origem)
                .stream()
                .map(ruaConexaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RuaConexaoResponseDTO> listarPorDestino(Long destinoId) {
        Bairro destino = bairroRepository.findById(destinoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Bairro de destino não encontrado: " + destinoId));

        return ruaConexaoRepository.findByDestino(destino)
                .stream()
                .map(ruaConexaoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public RuaConexaoResponseDTO buscarPorId(Long id) {
        RuaConexao rc = ruaConexaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Conexão de rua não encontrada: " + id));
        return ruaConexaoMapper.toResponseDTO(rc);
    }

    // ========= CRIAR / ATUALIZAR / EXCLUIR =========

    @Transactional
    public RuaConexaoResponseDTO criar(RuaConexaoRequestDTO dto) {
        validar(dto);

        Bairro origem = bairroRepository.findById(dto.origemId())
                .orElseThrow(() -> new NegocioException("Bairro de origem não encontrado."));

        Bairro destino = bairroRepository.findById(dto.destinoId())
                .orElseThrow(() -> new NegocioException("Bairro de destino não encontrado."));

        RuaConexao rc = ruaConexaoMapper.toEntity(dto.distanciaKm());
        rc.setOrigem(origem);
        rc.setDestino(destino);

        RuaConexao salvo = ruaConexaoRepository.save(rc);

        // Observer: notifica que o grafo foi alterado (padrão Observer via eventos do Spring)
        eventPublisher.publishEvent(new GrafoAtualizadoEvent("RuaConexaoService.criar"));

        return ruaConexaoMapper.toResponseDTO(salvo);
    }

    @Transactional
    public RuaConexaoResponseDTO atualizar(Long id, RuaConexaoRequestDTO dto) {
        validar(dto);

        RuaConexao existente = ruaConexaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Conexão de rua não encontrada: " + id));

        Bairro origem = bairroRepository.findById(dto.origemId())
                .orElseThrow(() -> new NegocioException("Bairro de origem não encontrado."));

        Bairro destino = bairroRepository.findById(dto.destinoId())
                .orElseThrow(() -> new NegocioException("Bairro de destino não encontrado."));

        existente.setOrigem(origem);
        existente.setDestino(destino);
        existente.setDistanciaKm(dto.distanciaKm());

        RuaConexao atualizado = ruaConexaoRepository.save(existente);

        // Observer: notifica que o grafo foi alterado
        eventPublisher.publishEvent(new GrafoAtualizadoEvent("RuaConexaoService.atualizar"));

        return ruaConexaoMapper.toResponseDTO(atualizado);
    }

    @Transactional
    public void excluir(Long id) {
        RuaConexao rc = ruaConexaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Conexão de rua não encontrada: " + id));

        // Se futuramente for necessário impedir exclusão quando a conexão
        // estiver em rotas, a validação pode ser feita aqui.
        ruaConexaoRepository.delete(rc);

        // Observer: notifica que o grafo foi alterado
        eventPublisher.publishEvent(new GrafoAtualizadoEvent("RuaConexaoService.excluir"));
    }

    // ========= AUXILIAR =========

    private void validar(RuaConexaoRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.origemId() == null) {
            throw new NegocioException("O bairro de origem é obrigatório.");
        }
        if (dto.destinoId() == null) {
            throw new NegocioException("O bairro de destino é obrigatório.");
        }
        // origem e destino não podem ser o mesmo bairro
        if (dto.origemId() != null && dto.destinoId() != null
                && dto.origemId().equals(dto.destinoId())) {
            throw new NegocioException("Origem e destino não podem ser o mesmo bairro.");
        }
        if (dto.distanciaKm() == null || dto.distanciaKm() <= 0) {
            throw new NegocioException("A distância deve ser maior que zero.");
        }
    }
}
