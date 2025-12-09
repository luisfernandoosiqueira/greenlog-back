package app.dto.caminhao;

import app.enums.StatusCaminhao;
import app.enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record CaminhaoRequestDTO(

        @NotBlank(message = "A placa é obrigatória")
        @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres")
        @Pattern(
                regexp = "^([A-Z]{3}[0-9][A-Z][0-9]{2}|[A-Z]{3}[0-9]{4})$",
                message = "A placa deve estar no padrão Mercosul (AAA1A11) ou no padrão antigo (AAA1111)"
        )
        String placa,

        @NotBlank(message = "O CPF do motorista é obrigatório")
        @Size(max = 14, message = "O CPF deve ter no máximo 14 caracteres")
        @Pattern(
                regexp = "^(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11})$",
                message = "O CPF deve estar no formato 000.000.000-00 ou 00000000000"
        )
        String motoristaCpf,

        @NotNull(message = "A capacidade é obrigatória")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacidadeKg,

        @NotNull(message = "O status é obrigatório")
        StatusCaminhao status,

        @NotEmpty(message = "Informe pelo menos um tipo de resíduo")
        List<@NotNull(message = "O tipo de resíduo é obrigatório") TipoResiduo> tiposResiduos
) {
}
