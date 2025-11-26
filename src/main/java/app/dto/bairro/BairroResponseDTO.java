package app.dto.bairro;

import java.util.List;

import app.dto.ruaconexao.RuaConexaoResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;

public record BairroResponseDTO(
        Long id,
        String nome,
        List<RuaConexaoResponseDTO> ruas,
        List<PontoColetaResponseDTO> pontosColetas
) {
}
