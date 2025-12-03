package app.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.dto.rota.RotaResponseDTO;
import app.dto.rota.TrechoRotaDTO;
import app.entity.Rota;
import app.enums.TipoResiduo;

@Component
public class RotaMapper {

    private final TrechoRotaMapper trechoRotaMapper;
    private final CaminhaoMapper caminhaoMapper;
    private final PontoColetaMapper pontoColetaMapper;

    public RotaMapper(TrechoRotaMapper trechoRotaMapper,
                      CaminhaoMapper caminhaoMapper,
                      PontoColetaMapper pontoColetaMapper) {
        this.trechoRotaMapper = trechoRotaMapper;
        this.caminhaoMapper = caminhaoMapper;
        this.pontoColetaMapper = pontoColetaMapper;
    }

    public RotaResponseDTO toResponseDTO(Rota rota) {
        if (rota == null) {
            return null;
        }

        String dataCriacao = rota.getDataCriacao() != null
                ? rota.getDataCriacao().toString()
                : null;

        CaminhaoResponseDTO caminhaoDTO =
                caminhaoMapper.toResponseDTO(rota.getCaminhao());

        List<TrechoRotaDTO> trechosDTO = rota.getTrechos()
                .stream()
                .map(trechoRotaMapper::toResponseDTO)
                .toList();

        List<PontoColetaResponseDTO> pontosDTO = rota.getPontosColeta()
                .stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();

        TipoResiduo tipoResiduo = rota.getTipoResiduo();

        return new RotaResponseDTO(
                rota.getId(),
                rota.getNome(),
                rota.getPesoTotal(),
                dataCriacao,
                tipoResiduo,
                caminhaoDTO,
                rota.getDistanciaTotal(),
                trechosDTO,
                pontosDTO
        );
    }
}
