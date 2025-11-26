package app.dto.motorista;

import app.enums.StatusMotorista;

public record MotoristaResponseDTO(
        String cpf,
        String nome,
        String data,
        String telefone,
        StatusMotorista status
) {
}
