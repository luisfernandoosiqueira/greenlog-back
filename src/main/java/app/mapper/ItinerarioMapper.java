package app.mapper;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.itinerario.ItinerarioResponseDTO;
import app.dto.rota.RotaResponseDTO;
import app.entity.Itinerario;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItinerarioMapper {

    private final CaminhaoMapper caminhaoMapper;
    private final RotaMapper rotaMapper;

    public ItinerarioMapper(CaminhaoMapper caminhaoMapper, RotaMapper rotaMapper) {
        this.caminhaoMapper = caminhaoMapper;
        this.rotaMapper = rotaMapper;
    }

    public ItinerarioResponseDTO toResponseDTO(Itinerario itinerario, List<Long> pontosColetaIds) {
        if (itinerario == null) {
            return null;
        }

        CaminhaoResponseDTO caminhaoDTO = caminhaoMapper.toResponseDTO(itinerario.getCaminhao());
        RotaResponseDTO rotaDTO = rotaMapper.toResponseDTO(itinerario.getRota());

        String data = itinerario.getData() != null ? itinerario.getData().toString() : null;

        return new ItinerarioResponseDTO(
                itinerario.getId(),
                data,
                caminhaoDTO,
                rotaDTO,
                itinerario.getAtivo(),
                pontosColetaIds
        );
    }
}
