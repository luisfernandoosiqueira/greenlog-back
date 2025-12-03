package app.dto.rota;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.enums.TipoResiduo;

import java.util.List;

public record RotaResponseDTO(
        Long id,
        String nome,
        Double pesoTotal,
        String dataCriacao,
        TipoResiduo tipoResiduo,
        CaminhaoResponseDTO caminhao,
        Double distanciaTotal,
        List<TrechoRotaDTO> trechos,
        List<PontoColetaResponseDTO> pontosColeta
) {
}
