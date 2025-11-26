package app.mapper;

import app.dto.usuario.UsuarioRequestDTO;
import app.dto.usuario.UsuarioResponseDTO;
import app.entity.Usuario;
import app.enums.PerfilUsuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.username());
        usuario.setSenhaHash(dto.senha()); // o hash pode ser tratado no service
        usuario.setPerfil(dto.perfil() != null ? dto.perfil() : PerfilUsuario.OPERADOR);
        return usuario;
    }

    public UsuarioResponseDTO toResponseDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        String perfil = usuario.getPerfil() != null ? usuario.getPerfil().name() : null;

        return new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getUsername(),
                perfil
        );
    }

    public void updateEntity(Usuario destino, UsuarioRequestDTO dto) {
        if (destino == null || dto == null) {
            return;
        }

        if (dto.username() != null) {
            destino.setUsername(dto.username());
        }
        if (dto.senha() != null) {
            destino.setSenhaHash(dto.senha());
        }
        if (dto.perfil() != null) {
            destino.setPerfil(dto.perfil());
        }
    }
}
