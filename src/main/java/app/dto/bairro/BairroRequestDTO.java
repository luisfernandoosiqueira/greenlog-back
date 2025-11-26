package app.dto.bairro;

import jakarta.validation.constraints.NotBlank;

public record BairroRequestDTO(

        @NotBlank(message = "O nome do bairro é obrigatório")
        String nome
) {
}
