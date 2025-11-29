package app.mapper;

import app.dto.rota.TrechoRotaDTO;
import app.dto.ruaconexao.RuaConexaoResponseDTO;
import app.entity.TrechoRota;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrechoRotaMapper {

    private final RuaConexaoMapper ruaConexaoMapper;

    public TrechoRotaMapper(RuaConexaoMapper ruaConexaoMapper) {
        this.ruaConexaoMapper = ruaConexaoMapper;
    }

    public TrechoRotaDTO toResponseDTO(TrechoRota trecho) {
        if (trecho == null) {
            return null;
        }

        List<RuaConexaoResponseDTO> ruasDTO = trecho.getRuas()
                .stream()
                .map(ruaConexaoMapper::toResponseDTO)
                .toList();

        return new TrechoRotaDTO(ruasDTO);
    }
}
