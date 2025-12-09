package app.dto.bairro;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BairroRequestDTO(

        @NotBlank(message = "O nome do bairro é obrigatório")
        @Size(max = 80, message = "O nome do bairro deve ter no máximo 80 caracteres")
        String nome
) {
}
