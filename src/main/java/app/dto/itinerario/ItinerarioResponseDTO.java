package app.dto.itinerario;

import app.dto.rota.RotaResponseDTO;

public record ItinerarioResponseDTO(
		Long id,
        String data,
        Double distanciaTotal,
        Boolean ativo,
        RotaResponseDTO rota
) {
}
