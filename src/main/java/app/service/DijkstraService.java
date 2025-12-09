package app.service;

import app.entity.RuaConexao;
import app.exceptions.NegocioException;
import app.patterns.CalculadoraRotaAdapter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Serviço que implementa o Adapter para cálculo de rotas,
 * delegando o menor caminho ao DijkstraSingleton via GrafoService.
 *
 * Padrão de projeto: Adapter
 */
@Service
public class DijkstraService implements CalculadoraRotaAdapter {

    private final GrafoService grafoService;

    public DijkstraService(GrafoService grafoService) {
        this.grafoService = grafoService;
    }

    /**
     * Rota simples entre dois bairros:
     * retorna a sequência de IDs de bairros no percurso.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> calcularRotaSimples(Long origemId, Long destinoId) {
        if (origemId == null || destinoId == null) {
            throw new NegocioException("Origem e destino são obrigatórios.");
        }
        if (origemId.equals(destinoId)) {
            return List.of(origemId);
        }

        List<RuaConexao> caminho = grafoService.calcularMenorCaminho(origemId, destinoId);
        if (caminho == null || caminho.isEmpty()) {
            throw new NegocioException("Não foi encontrado caminho entre os bairros informados.");
        }

        List<Long> percurso = new ArrayList<>();
        percurso.add(origemId);

        for (RuaConexao rc : caminho) {
            if (rc.getDestino() != null && rc.getDestino().getId() != null) {
                percurso.add(rc.getDestino().getId());
            }
        }
        return percurso;
    }

    /**
     * Rota com múltiplas paradas.
     * Aplica uma estratégia de "vizinho mais próximo":
     * - começa no primeiro bairro da lista;
     * - sempre busca o próximo bairro mais próximo;
     * - ao final, adiciona o retorno ao ponto inicial.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Long> calcularRotaMultiParadas(List<Long> bairrosIds) {
        if (bairrosIds == null || bairrosIds.isEmpty()) {
            throw new NegocioException("A lista de bairros não pode ser vazia.");
        }
        List<Long> bairrosIdsAtualizado = new ArrayList<>();
        if (bairrosIds.size() == 1) {
            Long unico = bairrosIds.get(0);
            return List.of(1L, unico, 1L);
        } else {
            bairrosIdsAtualizado.add(1L);               // começo
            bairrosIdsAtualizado.addAll(bairrosIds);    // todos os bairros na ordem
        }


        Long inicio = bairrosIdsAtualizado.get(0);
        Long atual = inicio;

        // mantém a ordem de inserção, mas permite remoção
        Set<Long> restantes = new LinkedHashSet<>(bairrosIdsAtualizado.subList(1, bairrosIdsAtualizado.size()));

        List<Long> percurso = new ArrayList<>();
        percurso.add(inicio);

        while (!restantes.isEmpty()) {
            Long proximo = null;
            double menorDist = Double.MAX_VALUE;

            for (Long candidato : restantes) {
                List<RuaConexao> caminho = grafoService.calcularMenorCaminho(atual, candidato);
                if (caminho == null || caminho.isEmpty()) {
                    continue;
                }
                double dist = grafoService.calcularDistancia(caminho);
                if (dist < menorDist) {
                    menorDist = dist;
                    proximo = candidato;
                }
            }

            if (proximo == null) {
                throw new NegocioException("Não foi possível encontrar caminhos para todos os bairros informados.");
            }

            percurso.add(proximo);
            restantes.remove(proximo);
            atual = proximo;
        }

        // retorno direto ao ponto inicial
        if (!atual.equals(inicio)) {
            List<RuaConexao> caminhoVolta = grafoService.calcularMenorCaminho(atual, inicio);
            if (caminhoVolta == null || caminhoVolta.isEmpty()) {
                throw new NegocioException("Não foi possível calcular o retorno ao ponto de partida.");
            }
            percurso.add(inicio);
        }

        return percurso;
    }
}
