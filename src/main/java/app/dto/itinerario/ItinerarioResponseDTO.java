package app.dto.itinerario;

import java.util.List;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.rota.RotaResponseDTO;

public record ItinerarioResponseDTO(
        Long id,
        String data,
        CaminhaoResponseDTO caminhao,
        RotaResponseDTO rota,
        Boolean ativo,
        List<Long> pontosColetaIds
) {
}
