package app.mapper;

import app.dto.motorista.MotoristaRequestDTO;
import app.dto.motorista.MotoristaResponseDTO;
import app.entity.Motorista;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MotoristaMapper {

    public Motorista toEntity(MotoristaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Motorista motorista = new Motorista();
        motorista.setNome(dto.nome());
        motorista.setCpf(dto.cpf());
        motorista.setTelefone(dto.telefone());
        motorista.setStatus(dto.status());

        if (dto.data() != null && !dto.data().isBlank()) {
            motorista.setData(LocalDate.parse(dto.data()));
        }

        return motorista;
    }

    public MotoristaResponseDTO toResponseDTO(Motorista motorista) {
        if (motorista == null) {
            return null;
        }

        String data = motorista.getData() != null ? motorista.getData().toString() : null;

        return new MotoristaResponseDTO(
                motorista.getCpf(),
                motorista.getNome(),
                data,
                motorista.getTelefone(),
                motorista.getStatus()
        );
    }
}
