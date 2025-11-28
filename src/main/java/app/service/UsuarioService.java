package app.service;

import app.dto.auth.LoginRequestDTO;
import app.dto.auth.LoginResponseDTO;
import app.dto.usuario.UsuarioRequestDTO;
import app.dto.usuario.UsuarioResponseDTO;
import app.entity.Usuario;
import app.enums.PerfilUsuario;
import app.exceptions.AutenticacaoException;
import app.exceptions.NegocioException;
import app.exceptions.RecursoNaoEncontradoException;
import app.mapper.UsuarioMapper;
import app.repository.UsuarioRepository;
import app.utils.HashUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
    }

    // ====== CONSULTAS ======

    public List<UsuarioResponseDTO> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toResponseDTO)
                .toList();
    }

    public UsuarioResponseDTO buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));
        return usuarioMapper.toResponseDTO(usuario);
    }

    // ====== AUTENTICAÇÃO ======

    public LoginResponseDTO autenticar(LoginRequestDTO dto) {
        if (dto == null
                || dto.username() == null || dto.username().isBlank()
                || dto.senha() == null || dto.senha().isBlank()) {
            throw new AutenticacaoException("Usuário e senha são obrigatórios.");
        }

        Usuario usuario = usuarioRepository.findByUsername(dto.username())
                .orElseThrow(() -> new AutenticacaoException("Credenciais inválidas."));

        String hashInformado = HashUtil.gerarHash(dto.senha());
        if (!hashInformado.equals(usuario.getSenhaHash())) {
            throw new AutenticacaoException("Credenciais inválidas.");
        }

        PerfilUsuario perfil = usuario.getPerfil();
        String perfilNome = (perfil != null ? perfil.name() : null);

        return new LoginResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                perfilNome
        );
    }

    // ====== CRIAR / ATUALIZAR ======

    @Transactional
    public UsuarioResponseDTO criar(UsuarioRequestDTO dto) {
        validarNovo(dto);

        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new NegocioException("Nome de usuário já cadastrado.");
        }

        Usuario usuario = usuarioMapper.toEntity(dto);
        String senhaHash = HashUtil.gerarHash(dto.senha());
        usuario.setSenhaHash(senhaHash);

        Usuario salvo = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDTO(salvo);
    }

    @Transactional
    public UsuarioResponseDTO atualizar(Long id, UsuarioRequestDTO dto) {
        validarAtualizacao(dto);

        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado: " + id));

        if (!existente.getUsername().equalsIgnoreCase(dto.username())
                && usuarioRepository.existsByUsername(dto.username())) {
            throw new NegocioException("Nome de usuário já cadastrado.");
        }

        UsuarioRequestDTO dtoParaMapper = dto;

        // Se veio uma nova senha, gera o hash antes de delegar ao mapper
        if (dto.senha() != null && !dto.senha().isBlank()) {
            String novaSenhaHash = HashUtil.gerarHash(dto.senha());
            dtoParaMapper = new UsuarioRequestDTO(
                    dto.username(),
                    novaSenhaHash,
                    dto.perfil()
            );
        }

        usuarioMapper.updateEntity(existente, dtoParaMapper);
        Usuario atualizado = usuarioRepository.save(existente);
        return usuarioMapper.toResponseDTO(atualizado);
    }

    // ====== AUXILIARES ======

    private void validarNovo(UsuarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.username() == null || dto.username().isBlank()) {
            throw new NegocioException("O nome de usuário é obrigatório.");
        }
        if (dto.senha() == null || dto.senha().isBlank()) {
            throw new NegocioException("A senha é obrigatória.");
        }
        if (dto.senha().length() < 8) {
            throw new NegocioException("A senha deve conter pelo menos 8 caracteres.");
        }
        if (dto.perfil() == null) {
            throw new NegocioException("O perfil é obrigatório.");
        }
    }

    private void validarAtualizacao(UsuarioRequestDTO dto) {
        if (dto == null) {
            throw new NegocioException("Dados obrigatórios não informados.");
        }
        if (dto.username() == null || dto.username().isBlank()) {
            throw new NegocioException("O nome de usuário é obrigatório.");
        }
        if (dto.senha() != null && !dto.senha().isBlank() && dto.senha().length() < 8) {
       	
            throw new NegocioException("A senha deve conter pelo menos 8 caracteres.");
        }
        if (dto.perfil() == null) {
            throw new NegocioException("O perfil é obrigatório.");
        }
    }
}
