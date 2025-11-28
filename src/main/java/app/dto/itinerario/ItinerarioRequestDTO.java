package app.dto.itinerario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ItinerarioRequestDTO(

        @NotNull(message = "A rota é obrigatória")
        Long rotaId,

        @NotBlank(message = "A data é obrigatória")
        @Pattern(
                regexp = "^\\d{4}-\\d{2}-\\d{2}$",
                message = "A data deve estar no formato yyyy-MM-dd"
        )
        String data
) {
}
