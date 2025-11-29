package app.dto.caminhao;

import app.enums.StatusCaminhao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record CaminhaoRequestDTO(

        @NotBlank(message = "A placa é obrigatória")
        String placa,

        @NotBlank(message = "O CPF do motorista é obrigatório")
        String motoristaCpf,

        @NotNull(message = "A capacidade é obrigatória")
        @Positive(message = "A capacidade deve ser maior que zero")
        Integer capacidadeKg,

        @NotNull(message = "O status é obrigatório")
        StatusCaminhao status,

        @NotEmpty(message = "Informe pelo menos um tipo de resíduo")
        List<@NotNull(message = "O id do tipo de resíduo é obrigatório") Long> tiposResiduoIds
) {
}
