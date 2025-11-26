package app.dto.itinerario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record ItinerarioRequestDTO(

        @NotBlank(message = "A data é obrigatória")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "A data deve estar no formato yyyy-MM-dd"
        )
        String data,

        @NotBlank(message = "A placa do caminhão é obrigatória")
        String caminhaoPlaca,

        @NotEmpty(message = "Informe pelo menos um ponto de coleta")
        List<@NotNull(message = "O id do ponto de coleta é obrigatório") Long> pontosColetaIds
) {
}
