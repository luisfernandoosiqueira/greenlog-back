package app.dto.pontocoleta;

import java.util.List;

import app.entity.TipoResiduoModel;

public record PontoColetaResponseDTO(
        Long id,
        Long bairroId,
        String nome,
        String responsavel,
        String telefone,
        String email,
        String endereco,
        String horarioFuncionamento,
        Integer quantidadeResiduosKg,
        List<TipoResiduoModel> tiposResiduos
) {
}
