package app.dto.caminhao;

import app.dto.motorista.MotoristaResponseDTO;
import app.enums.StatusCaminhao;
import app.enums.TipoResiduo;

import java.util.List;

public record CaminhaoResponseDTO(
        String placa,
        MotoristaResponseDTO motorista,
        Integer capacidadeKg,
        StatusCaminhao status,
        List<TipoResiduo> tiposResiduos
) {
}
