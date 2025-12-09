package app.mapper;

import app.dto.pontocoleta.PontoColetaRequestDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;
import app.entity.PontoColeta;
import app.enums.TipoResiduo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PontoColetaMapper {

    public PontoColeta toEntity(PontoColetaRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        PontoColeta ponto = new PontoColeta();
        ponto.setNome(dto.nome());
        ponto.setResponsavel(dto.responsavel());
        ponto.setTelefone(dto.telefone());
        ponto.setEmail(dto.email());
        ponto.setEndereco(dto.endereco());
        ponto.setHoraEntrada(dto.horaEntrada());
        ponto.setHoraSaida(dto.horaSaida());
        ponto.setQuantidadeResiduosKg(dto.quantidadeResiduosKg());
        return ponto;
    }

    public PontoColetaResponseDTO toResponseDTO(PontoColeta ponto) {
        if (ponto == null) {
            return null;
        }

        Bairro bairro = ponto.getBairro();
        Long bairroId = bairro != null ? bairro.getId() : null;

        List<TipoResiduo> tiposResiduos = ponto.getTiposResiduo()
                .stream()
                .toList();

        return new PontoColetaResponseDTO(
                ponto.getId(),
                bairroId,
                ponto.getNome(),
                ponto.getResponsavel(),
                ponto.getTelefone(),
                ponto.getEmail(),
                ponto.getEndereco(),
                ponto.getHoraEntrada(),
                ponto.getHoraSaida(),
                ponto.getQuantidadeResiduosKg(),
                tiposResiduos
        );
    }
}
