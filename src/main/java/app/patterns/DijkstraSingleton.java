package app.patterns;

import app.entity.RuaConexao;

import java.util.*;

/**
 * Implementação do padrão Singleton para o cálculo de menor caminho
 * entre bairros utilizando o algoritmo de Dijkstra.
 */
public class DijkstraSingleton {

    private static DijkstraSingleton instancia;

    private DijkstraSingleton() {
    }

    public static synchronized DijkstraSingleton getInstance() {
        if (instancia == null) {
            instancia = new DijkstraSingleton();
        }
        return instancia;
    }

    /**
     * Calcula o menor caminho entre dois bairros utilizando o grafo em memória.
     *
     * @param grafo     mapa de adjacências (id do bairro → conexões saindo dele)
     * @param origemId  id do bairro de origem
     * @param destinoId id do bairro de destino
     * @return lista de RuaConexao na ordem do percurso; vazia se não houver caminho
     */
    public List<RuaConexao> calcularMenorCaminho(Map<Long, List<RuaConexao>> grafo,
                                                 Long origemId,
                                                 Long destinoId) {

        Map<Long, Double> distancias = new HashMap<>();
        Map<Long, RuaConexao> anteriores = new HashMap<>();
        Set<Long> visitados = new HashSet<>();

        PriorityQueue<NoDist> fila = new PriorityQueue<>(Comparator.comparingDouble(NoDist::distancia));

        for (Long id : grafo.keySet()) {
            distancias.put(id, Double.POSITIVE_INFINITY);
        }
        distancias.put(origemId, 0.0);
        fila.add(new NoDist(origemId, 0.0));

        while (!fila.isEmpty()) {
            NoDist atual = fila.poll();
            Long bairroAtual = atual.id;

            if (!visitados.add(bairroAtual)) {
                continue;
            }
            if (bairroAtual.equals(destinoId)) {
                break;
            }

            List<RuaConexao> conexoes = grafo.getOrDefault(bairroAtual, Collections.emptyList());
            for (RuaConexao conexao : conexoes) {
                Long vizinhoId = conexao.getDestino().getId();
                double peso = conexao.getDistanciaKm() != null ? conexao.getDistanciaKm() : 0.0;
                double novaDist = distancias.getOrDefault(bairroAtual, Double.POSITIVE_INFINITY) + peso;

                if (novaDist < distancias.getOrDefault(vizinhoId, Double.POSITIVE_INFINITY)) {
                    distancias.put(vizinhoId, novaDist);
                    anteriores.put(vizinhoId, conexao);
                    fila.add(new NoDist(vizinhoId, novaDist));
                }
            }
        }

        List<RuaConexao> caminho = new ArrayList<>();
        if (!origemId.equals(destinoId) && !anteriores.containsKey(destinoId)) {
            return caminho;
        }

        Long atualId = destinoId;
        while (!atualId.equals(origemId)) {
            RuaConexao aresta = anteriores.get(atualId);
            if (aresta == null) {
                caminho.clear();
                break;
            }
            caminho.add(aresta);
            atualId = aresta.getOrigem().getId();
        }

        Collections.reverse(caminho);
        return caminho;
    }

    private static final class NoDist {
        private final Long id;
        private final double distancia;

        private NoDist(Long id, double distancia) {
            this.id = id;
            this.distancia = distancia;
        }

        public double distancia() {
            return distancia;
        }
    }
}
