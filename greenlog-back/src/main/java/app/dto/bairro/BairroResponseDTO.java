package app.dto.bairro;

import java.util.List;

import app.dto.pontocoleta.PontoColetaResponseDTO;

public record BairroResponseDTO(
        Long id,
        String nome,
        List<PontoColetaResponseDTO> pontosColetas
) {
}
