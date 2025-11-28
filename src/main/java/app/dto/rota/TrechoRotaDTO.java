package app.dto.rota;

import java.util.List;

import app.dto.ruaconexao.RuaConexaoResponseDTO;

public record TrechoRotaDTO(
       
		List<RuaConexaoResponseDTO> ruas
		
) {
}
