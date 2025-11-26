package app.dto.pontocoleta;

import jakarta.validation.constraints.*;

import java.util.List;

public record PontoColetaRequestDTO(

        @NotNull(message = "O bairro é obrigatório")
        Long bairroId,

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 120, message = "O nome deve ter no máximo 120 caracteres")
        String nome,

        @Size(max = 120, message = "O responsável deve ter no máximo 120 caracteres")
        String responsavel,

        @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
        String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
        String email,

        @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
        String endereco,

        @Size(max = 60, message = "O horário de funcionamento deve ter no máximo 60 caracteres")
        String horarioFuncionamento,

        @NotEmpty(message = "Informe pelo menos um tipo de resíduo")
        List<@NotNull(message = "O id do tipo de resíduo é obrigatório") Long> tiposResiduoIds,

        @NotNull(message = "A quantidade de resíduos é obrigatória")
        @PositiveOrZero(message = "A quantidade de resíduos não pode ser negativa")
        Integer quantidadeResiduosKg
) {
}
