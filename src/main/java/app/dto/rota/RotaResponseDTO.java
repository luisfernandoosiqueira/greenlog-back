package app.dto.rota;

import java.util.List;

public record RotaResponseDTO(
        Long id,
        Long origemId,
        Long destinoId,
        Double distanciaTotal,
        List<TrechoRotaDTO> trechos
) {
}
