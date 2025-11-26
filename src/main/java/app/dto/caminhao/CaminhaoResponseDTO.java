package app.dto.caminhao;

import app.dto.motorista.MotoristaResponseDTO;
import app.enums.StatusCaminhao;

import java.util.List;

public record CaminhaoResponseDTO(
        String placa,
        MotoristaResponseDTO motorista,
        Integer capacidadeKg,
        StatusCaminhao status,
        List<Long> tiposResiduoIds
) {
}
