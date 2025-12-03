package app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(

        @NotBlank(message = "O nome de usuário é obrigatório")
        @Size(max = 60, message = "O nome de usuário deve ter no máximo 60 caracteres")
        String username,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, max = 120, message = "A senha deve ter entre 6 e 120 caracteres")
        String senha
) {
}
