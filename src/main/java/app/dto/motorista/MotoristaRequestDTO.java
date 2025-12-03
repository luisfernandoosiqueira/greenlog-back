package app.dto.motorista;

import app.enums.StatusMotorista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record MotoristaRequestDTO(

        @NotBlank(message = "O CPF é obrigatório")
        @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres")
        @Pattern(
                regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
                message = "O CPF deve estar no formato 000.000.000-00 ou 00000000000"
        )
        String cpf,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "A data é obrigatória")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "A data deve estar no formato yyyy-MM-dd"
        )
        String data,

        @NotBlank(message = "O telefone é obrigatório")
        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        @Pattern(
                regexp = "^(\\(\\d{2}\\)\\s?)?\\d{4,5}-?\\d{4}$",
                message = "Telefone inválido"
        )
        String telefone,

        @NotNull(message = "O status é obrigatório")
        StatusMotorista status
) {
}
