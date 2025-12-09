package app.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import app.dto.caminhao.CaminhaoResponseDTO;
import app.dto.pontocoleta.PontoColetaResponseDTO;
import app.dto.rota.RotaResponseDTO;
import app.dto.rota.TrechoRotaDTO;
import app.entity.Bairro;
import app.entity.Rota;
import app.entity.RuaConexao;
import app.entity.TrechoRota;
import app.enums.TipoResiduo;
import app.exceptions.NegocioException;
import app.service.DijkstraService;
import app.service.GrafoService;

@Component
public class RotaMapper {

    private final TrechoRotaMapper trechoRotaMapper;
    private final CaminhaoMapper caminhaoMapper;
    private final PontoColetaMapper pontoColetaMapper;
    private final DijkstraService dijkstraService;
    private final GrafoService grafoService;

    public RotaMapper(TrechoRotaMapper trechoRotaMapper,
                      CaminhaoMapper caminhaoMapper,
                      DijkstraService dijkstraService,
                       GrafoService grafoService,
                      PontoColetaMapper pontoColetaMapper) {
        this.trechoRotaMapper = trechoRotaMapper;
        this.caminhaoMapper = caminhaoMapper;
        this.pontoColetaMapper = pontoColetaMapper;
        this.grafoService = grafoService;
        this.dijkstraService = dijkstraService;
    }

    public RotaResponseDTO toResponseDTO(Rota rota) {
        if (rota == null) {
            return null;
        }

        String dataCriacao = rota.getDataCriacao() != null
                ? rota.getDataCriacao().toString()
                : null;

        CaminhaoResponseDTO caminhaoDTO =
                caminhaoMapper.toResponseDTO(rota.getCaminhao());

        List<PontoColetaResponseDTO> pontosDTO = rota.getPontosColeta()
                .stream()
                .map(pontoColetaMapper::toResponseDTO)
                .toList();

        // TROCA ESSE TRECHO
        List<Long> bairrosIds = new ArrayList<>();
        for (PontoColetaResponseDTO p : pontosDTO) {
                bairrosIds.add(p.bairroId());
        }
        
        List<Long> ordemBairros = dijkstraService.calcularRotaMultiParadas(bairrosIds);
        System.out.println("=== Ordem dos Bairros: " + ordemBairros);
        List<List<RuaConexao>> caminhosPorTrecho = montarCaminhosPorTrecho(ordemBairros);

        List<TrechoRota> trechos = new ArrayList<>();

        for (List<RuaConexao> caminho : caminhosPorTrecho) {
            if (caminho == null || caminho.isEmpty()) {
                continue;
            }

            RuaConexao primeira = caminho.get(0);
            RuaConexao ultima = caminho.get(caminho.size() - 1);

            Bairro origem = primeira.getOrigem();
            Bairro destino = ultima.getDestino();

            TrechoRota trecho = new TrechoRota(rota, origem, destino);
            trecho.setRuas(caminho);
            trechos.add(trecho);
        }

        List<TrechoRotaDTO> trechosDTO = trechos.stream()
                .map(trechoRotaMapper::toResponseDTO)
                .toList();

        TipoResiduo tipoResiduo = rota.getTipoResiduo();

        return new RotaResponseDTO(
                rota.getId(),
                rota.getNome(),
                rota.getPesoTotal(),
                dataCriacao,
                tipoResiduo,
                caminhaoDTO,
                rota.getDistanciaTotal(),
                trechosDTO,
                pontosDTO
        );
    }

    private List<List<RuaConexao>> montarCaminhosPorTrecho(List<Long> ordemBairros) {
        if (ordemBairros == null || ordemBairros.size() < 2) {
            throw new NegocioException("A lista de bairros da rota é insuficiente.");
        }

        List<List<RuaConexao>> caminhos = new ArrayList<>();

        for (int i = 0; i < ordemBairros.size() - 1; i++) {
            Long origemId = ordemBairros.get(i);
            Long destinoId = ordemBairros.get(i + 1);

            List<RuaConexao> caminho = grafoService.calcularMenorCaminho(origemId, destinoId);
            if (caminho == null || caminho.isEmpty()) {
                throw new NegocioException("Não foi encontrado caminho entre os bairros selecionados.");
            }

            caminhos.add(caminho);
        }

        return caminhos;
    }
}
