package app.mapper;

import app.dto.rota.TrechoRotaDTO;
import app.entity.RuaConexao;
import app.entity.TrechoRota;
import org.springframework.stereotype.Component;

@Component
public class TrechoRotaMapper {

    public TrechoRotaDTO toResponseDTO(TrechoRota trecho) {
        if (trecho == null) {
            return null;
        }

        RuaConexao rc = trecho.getRuaConexao();

        Long ruaConexaoId = rc != null ? rc.getId() : null;
        Long origemId = rc != null && rc.getOrigem() != null ? rc.getOrigem().getId() : null;
        Long destinoId = rc != null && rc.getDestino() != null ? rc.getDestino().getId() : null;
        Double distancia = rc != null ? rc.getDistanciaKm() : null;

        return new TrechoRotaDTO(
                trecho.getId(),
                ruaConexaoId,
                origemId,
                destinoId,
                distancia,
                trecho.getOrdem()
        );
    }
}
