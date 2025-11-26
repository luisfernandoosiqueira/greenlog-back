package app.mapper;

import app.dto.pontocoleta.PontoColetaRequestDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.entity.Bairro;
import app.entity.PontoColeta;
import app.entity.TipoResiduoModel;
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
        ponto.setHorarioFuncionamento(dto.horarioFuncionamento());
        ponto.setQuantidadeResiduosKg(dto.quantidadeResiduosKg());
        // bairro e tipos de resíduo serão ajustados no service
        return ponto;
    }

    public PontoColetaResponseDTO toResponseDTO(PontoColeta ponto) {
        if (ponto == null) {
            return null;
        }

        Bairro bairro = ponto.getBairro();
        Long bairroId = bairro != null ? bairro.getId() : null;
        String bairroNome = bairro != null ? bairro.getNome() : null;

        List<Long> tiposIds = ponto.getTiposResiduo()
                .stream()
                .map(TipoResiduoModel::getId)
                .toList();

        return new PontoColetaResponseDTO(
                ponto.getId(),
                bairroId,
                bairroNome,
                ponto.getNome(),
                ponto.getResponsavel(),
                ponto.getTelefone(),
                ponto.getEmail(),
                ponto.getEndereco(),
                ponto.getHorarioFuncionamento(),
                ponto.getQuantidadeResiduosKg(),
                tiposIds
        );
    }
}
