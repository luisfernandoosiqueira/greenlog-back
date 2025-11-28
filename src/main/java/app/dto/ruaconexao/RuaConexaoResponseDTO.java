package app.dto.ruaconexao;

import app.entity.Bairro;

public record RuaConexaoResponseDTO(
        Long id,
        Bairro origem,
        Bairro destino,
        Double distanciaKm
) {
}
