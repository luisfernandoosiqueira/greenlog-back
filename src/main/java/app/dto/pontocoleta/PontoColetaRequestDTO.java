package app.dto.pontocoleta;

import app.enums.TipoResiduo;
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
        @Pattern(
                regexp = "^(?:$|(?:\\(\\d{2}\\)\\s?\\d{4,5}-?\\d{4}|\\d{2}\\d{4,5}\\d{4}))$",
                message = "Telefone inválido"
        )
        String telefone,

        @Email(message = "E-mail inválido")
        @Size(max = 120, message = "O e-mail deve ter no máximo 120 caracteres")
        String email,

        @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
        String endereco,

        @Size(max = 5, message = "A hora de entrada deve ter no máximo 5 caracteres")
        @Pattern(
                regexp = "^(|(?:[01]\\d|2[0-3]):[0-5]\\d)$",
                message = "A hora de entrada deve estar no formato HH:mm"
        )
        String horaEntrada,

        @Size(max = 5, message = "A hora de saída deve ter no máximo 5 caracteres")
        @Pattern(
                regexp = "^(|(?:[01]\\d|2[0-3]):[0-5]\\d)$",
                message = "A hora de saída deve estar no formato HH:mm"
        )
        String horaSaida,

        @NotEmpty(message = "Informe pelo menos um tipo de resíduo")
        List<@NotNull(message = "O tipo de resíduo é obrigatório") TipoResiduo> tiposResiduos,

        @NotNull(message = "A quantidade de resíduos é obrigatória")
        @PositiveOrZero(message = "A quantidade de resíduos não pode ser negativa")
        Integer quantidadeResiduosKg
) {
}
