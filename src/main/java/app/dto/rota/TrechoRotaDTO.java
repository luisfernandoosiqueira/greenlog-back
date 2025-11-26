package app.dto.rota;

public record TrechoRotaDTO(
        Long id,
        Long ruaConexaoId,
        Long origemId,
        Long destinoId,
        Double distanciaKm,
        Integer ordem
) {
}
