package app.dto.rota;

import app.enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RotaRequestDTO(

        @NotBlank(message = "O nome da rota é obrigatório")
        @Size(max = 120, message = "O nome da rota deve ter no máximo 120 caracteres")
        String nome,

        @NotBlank(message = "A placa do caminhão é obrigatória")
        @Size(max = 10, message = "A placa deve ter no máximo 10 caracteres")
        @Pattern(
                regexp = "^([A-Z]{3}[0-9][A-Z][0-9]{2}|[A-Z]{3}[0-9]{4})$",
                message = "A placa deve estar no padrão Mercosul (AAA1A11) ou no padrão antigo (AAA1111)"
        )
        String caminhaoPlaca,

        @NotNull(message = "O tipo de resíduo é obrigatório")
        TipoResiduo tipoResiduo,

        @NotEmpty(message = "Informe pelo menos um ponto de coleta")
        List<@NotNull(message = "O id do ponto de coleta é obrigatório") Long> pontosColetaIds
) {
}
