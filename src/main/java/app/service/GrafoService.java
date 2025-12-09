package app.service;

import app.entity.RuaConexao;
import app.patterns.DijkstraSingleton;
import app.repository.RuaConexaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Serviço responsável por montar e fornecer o grafo em memória
 * utilizado pelo DijkstraSingleton (padrão Singleton).
 */
@Service
public class GrafoService {

    private final RuaConexaoRepository ruaConexaoRepository;

    // Cache simples em memória do grafo atual
    private Map<Long, List<RuaConexao>> grafoCache = new HashMap<>();

    public GrafoService(RuaConexaoRepository ruaConexaoRepository) {
        this.ruaConexaoRepository = ruaConexaoRepository;
    }

    /**
     * Retorna o grafo atual. Se o cache estiver vazio,
     * recarrega os dados a partir do banco.
     */
    @Transactional(readOnly = true)
    public Map<Long, List<RuaConexao>> obterGrafoAtual() {
        if (grafoCache == null || grafoCache.isEmpty()) {
            carregarGrafo();
        }
        return grafoCache;
    }

    /**
     * Recarrega o grafo a partir das conexões de rua persistidas.
     * Cada bairro de origem aponta para a lista de conexões saindo dele.
     */
    @Transactional(readOnly = true)
    public Map<Long, List<RuaConexao>> carregarGrafo() {
        List<RuaConexao> conexoes = ruaConexaoRepository.findAllComBairros();

        Map<Long, List<RuaConexao>> grafo = new HashMap<>();
        for (RuaConexao rc : conexoes) {
            if (rc.getOrigem() == null || rc.getOrigem().getId() == null) {
                continue;
            }
            Long origemId = rc.getOrigem().getId();
            grafo.computeIfAbsent(origemId, k -> new ArrayList<>())
                 .add(rc);
        }

        this.grafoCache = grafo;
        return grafo;
    }

    /**
     * Utiliza o DijkstraSingleton para calcular o menor caminho
     * entre dois bairros, com base no grafo em memória.
     */
    @Transactional(readOnly = true)
    public List<RuaConexao> calcularMenorCaminho(Long origemId, Long destinoId) {
        Map<Long, List<RuaConexao>> grafo = obterGrafoAtual();
        return DijkstraSingleton.getInstance()
                .calcularMenorCaminho(grafo, origemId, destinoId);
    }

    /**
     * Calcula a distância total (em km) de um caminho de conexões.
     */
    public double calcularDistancia(List<RuaConexao> caminho) {
        if (caminho == null || caminho.isEmpty()) {
            return 0.0;
        }
        return caminho.stream()
                .mapToDouble(rc -> rc.getDistanciaKm() != null ? rc.getDistanciaKm() : 0.0)
                .sum();
    }

    /**
     * Invalida o cache do grafo, forçando recarga na próxima consulta.
     * Pode ser chamado pelo listener de eventos quando o grafo é alterado.
     */
    public void invalidarCache() {
        this.grafoCache = new HashMap<>();
    }
}
