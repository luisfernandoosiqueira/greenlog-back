package app.dto.ruaconexao;

public record RuaConexaoResponseDTO(
        Long id,
        Long origemId,
        Long destinoId,
        Double distanciaKm
) {
}
