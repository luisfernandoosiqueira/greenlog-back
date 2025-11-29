package app.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import app.dto.pontocoleta.PontoColetaRequestDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;
import app.entity.PontoColeta;
import app.entity.TipoResiduoModel;

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
        ponto.setHorarioFuncionamento(dto.horarioFuncionamento());
        ponto.setQuantidadeResiduosKg(dto.quantidadeResiduosKg());
        // bairro e tipos de resíduo serão ajustados no service
        return ponto;
    }

    public PontoColetaResponseDTO toResponseDTO(PontoColeta ponto, List<TipoResiduoModel> tiposResiduos) {
        if (ponto == null) {
            return null;
        }

        Bairro bairro = ponto.getBairro();
        Long bairroId = bairro != null ? bairro.getId() : null;

        return new PontoColetaResponseDTO(
                ponto.getId(),
                bairroId,
                ponto.getNome(),
                ponto.getResponsavel(),
                ponto.getTelefone(),
                ponto.getEmail(),
                ponto.getEndereco(),
                ponto.getHorarioFuncionamento(),
                ponto.getQuantidadeResiduosKg(),
                tiposResiduos
        );
    }
}
