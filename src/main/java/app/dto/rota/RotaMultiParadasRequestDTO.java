package app.dto.rota;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RotaMultiParadasRequestDTO(

        @NotEmpty(message = "A lista de bairros não pode ser vazia")
        @Size(min = 2, message = "Informe pelo menos dois bairros para calcular a rota")
        List<@NotNull(message = "O id do bairro é obrigatório") Long> bairrosIds
) {
}
