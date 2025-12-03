package app.dto.rota;

import app.enums.TipoResiduo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RotaRequestDTO(

        @NotBlank(message = "O nome da rota é obrigatório")
        String nome,

        @NotBlank(message = "A placa do caminhão é obrigatória")
        String caminhaoPlaca,

        @NotNull(message = "O tipo de resíduo é obrigatório")
        TipoResiduo tipoResiduo,

        @NotEmpty(message = "Informe pelo menos um ponto de coleta")
        List<@NotNull(message = "O id do ponto de coleta é obrigatório") Long> pontosColetaIds
) {
}
