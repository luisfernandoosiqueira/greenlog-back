package app.dto.rota;

import java.util.List;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.TipoResiduoModel;

public record RotaResponseDTO(
        Long id,
        String nome,
        Double pesoTotal,
        String dataCriacao,
        TipoResiduoModel tipoResiduo,
        CaminhaoResponseDTO caminhao,
        Double distanciaTotal,
        List<TrechoRotaDTO> trechos,
        List<PontoColetaResponseDTO> pontosColeta
) {
}
