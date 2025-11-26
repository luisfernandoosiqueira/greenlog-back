package app.mapper;

import app.dto.rota.RotaResponseDTO;
import app.dto.rota.TrechoRotaDTO;
import app.entity.Rota;
import app.entity.TrechoRota;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RotaMapper {

    private final TrechoRotaMapper trechoRotaMapper;

    public RotaMapper(TrechoRotaMapper trechoRotaMapper) {
        this.trechoRotaMapper = trechoRotaMapper;
    }

    public RotaResponseDTO toResponseDTO(Rota rota) {
        if (rota == null) {
            return null;
        }

        Long origemId = rota.getOrigem() != null ? rota.getOrigem().getId() : null;
        Long destinoId = rota.getDestino() != null ? rota.getDestino().getId() : null;

        List<TrechoRotaDTO> trechos = rota.getTrechos()
                .stream()
                .map(trechoRotaMapper::toResponseDTO)
                .toList();

        return new RotaResponseDTO(
                rota.getId(),
                origemId,
                destinoId,
                rota.getDistanciaTotal(),
                trechos
        );
    }

    public void atualizarTrechos(Rota rota, List<TrechoRota> trechos) {
        rota.getTrechos().clear();
        rota.getTrechos().addAll(trechos);
    }
}
