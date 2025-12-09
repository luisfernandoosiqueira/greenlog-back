package app.mapper;

import app.dto.caminhao.CaminhaoRequestDTO;
import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.motorista.MotoristaResponseDTO;
import app.entity.Caminhao;
import app.entity.Motorista;
import app.enums.TipoResiduo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CaminhaoMapper {

    private final MotoristaMapper motoristaMapper;

    public CaminhaoMapper(MotoristaMapper motoristaMapper) {
        this.motoristaMapper = motoristaMapper;
    }

    public Caminhao toEntity(CaminhaoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Caminhao caminhao = new Caminhao();
        caminhao.setPlaca(dto.placa());
        caminhao.setStatus(dto.status());
        caminhao.setCapacidadeKg(dto.capacidadeKg());
        return caminhao;
    }

    public CaminhaoResponseDTO toResponseDTO(Caminhao caminhao) {
        if (caminhao == null) {
            return null;
        }

        Motorista motorista = caminhao.getMotorista();
        MotoristaResponseDTO motoristaDTO = motorista != null
                ? motoristaMapper.toResponseDTO(motorista)
                : null;

        List<TipoResiduo> tiposResiduos = caminhao.getTiposResiduo()
                .stream()
                .toList();

        return new CaminhaoResponseDTO(
                caminhao.getPlaca(),
                motoristaDTO,
                caminhao.getCapacidadeKg(),
                caminhao.getStatus(),
                tiposResiduos
        );
    }
}
