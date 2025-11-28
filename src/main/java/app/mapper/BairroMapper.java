package app.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import app.dto.bairro.BairroRequestDTO;
import app.dto.bairro.BairroResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;

@Component
public class BairroMapper {

    public Bairro toEntity(BairroRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Bairro bairro = new Bairro();
        bairro.setNome(dto.nome());
        return bairro;
    }

    public BairroResponseDTO toResponseDTO(Bairro bairro, List<PontoColetaResponseDTO> pontosColetas) {
        if (bairro == null) {
            return null;
        }

        return new BairroResponseDTO(
                bairro.getId(),
                bairro.getNome(),
                pontosColetas
        );
    }
}
