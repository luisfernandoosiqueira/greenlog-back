package app.dto.ruaconexao;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RuaConexaoRequestDTO(

        @NotNull(message = "O bairro de origem é obrigatório")
        Long origemId,

        @NotNull(message = "O bairro de destino é obrigatório")
        Long destinoId,

        @NotNull(message = "A distância é obrigatória")
        @Positive(message = "A distância deve ser maior que zero")
        Double distanciaKm
) {
}
