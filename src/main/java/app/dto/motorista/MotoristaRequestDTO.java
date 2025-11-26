package app.dto.motorista;

import app.enums.StatusMotorista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MotoristaRequestDTO(

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "A data é obrigatória")
        // Exemplo de formato esperado: 2025-11-25
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "A data deve estar no formato yyyy-MM-dd"
        )
        String data,

        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @NotNull(message = "O status é obrigatório")
        StatusMotorista status
) {
}
