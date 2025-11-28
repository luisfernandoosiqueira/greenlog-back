package app.mapper;

import app.dto.ruaconexao.RuaConexaoResponseDTO;
import app.entity.RuaConexao;
import org.springframework.stereotype.Component;

@Component
public class RuaConexaoMapper {

    public RuaConexao toEntity(Double distanciaKm) {
        RuaConexao ruaConexao = new RuaConexao();
        ruaConexao.setDistanciaKm(distanciaKm);
        return ruaConexao;
    }

    public RuaConexaoResponseDTO toResponseDTO(RuaConexao ruaConexao) {
        if (ruaConexao == null) {
            return null;
        }

        return new RuaConexaoResponseDTO(
                ruaConexao.getId(),
                ruaConexao.getOrigem(),
                ruaConexao.getDestino(),
                ruaConexao.getDistanciaKm()
        );
    }
}
