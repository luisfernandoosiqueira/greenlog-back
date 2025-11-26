package app.dto.rota;

import jakarta.validation.constraints.NotNull;

public record RotaRequestDTO(

        @NotNull(message = "O bairro de origem é obrigatório")
        Long origemId,

        @NotNull(message = "O bairro de destino é obrigatório")
        Long destinoId
) {
}
