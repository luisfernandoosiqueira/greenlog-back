package app.mapper;

import app.dto.itinerario.ItinerarioResponseDTO;
import app.dto.rota.RotaResponseDTO;
import app.entity.Itinerario;
import org.springframework.stereotype.Component;

@Component
public class ItinerarioMapper {

    private final RotaMapper rotaMapper;

    public ItinerarioMapper(RotaMapper rotaMapper) {
        this.rotaMapper = rotaMapper;
    }

    public ItinerarioResponseDTO toResponseDTO(Itinerario itinerario) {
        if (itinerario == null) {
            return null;
        }

        String data = itinerario.getDataAgendamento() != null
                ? itinerario.getDataAgendamento().toString()
                : null;

        RotaResponseDTO rotaDTO = rotaMapper.toResponseDTO(itinerario.getRota());

        return new ItinerarioResponseDTO(
                itinerario.getId(),
                data,
                itinerario.getDistanciaTotal(),
                itinerario.getAtivo(),
                rotaDTO
        );
    }
}
