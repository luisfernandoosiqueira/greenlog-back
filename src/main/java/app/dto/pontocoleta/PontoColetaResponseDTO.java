package app.dto.pontocoleta;

import java.util.List;

public record PontoColetaResponseDTO(
        Long id,
        Long bairroId,
        String bairroNome,
        String nome,
        String responsavel,
        String telefone,
        String email,
        String endereco,
        String horarioFuncionamento,
        Integer quantidadeResiduosKg,
        List<Long> tiposResiduoIds
) {
}
