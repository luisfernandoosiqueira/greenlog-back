package app.dto.pontocoleta;

import app.enums.TipoResiduo;

import java.util.List;

public record PontoColetaResponseDTO(
        Long id,
        Long bairroId,
        String nome,
        String responsavel,
        String telefone,
        String email,
        String endereco,
        String horaEntrada,
        String horaSaida,
        Integer quantidadeResiduosKg,
        List<TipoResiduo> tiposResiduos
) {
}
