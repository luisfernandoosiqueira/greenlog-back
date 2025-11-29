package app.service;

import app.dto.auth.LoginRequestDTO;
import app.dto.auth.LoginResponseDTO;
import app.entity.Usuario;
import app.enums.PerfilUsuario;
import app.exceptions.AutenticacaoException;
import app.repository.UsuarioRepository;
import app.utils.HashUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

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
}
