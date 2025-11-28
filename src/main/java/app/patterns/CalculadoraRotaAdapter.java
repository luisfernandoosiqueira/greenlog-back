package app.patterns;

import java.util.List;

/**
 * Padrão Adapter para cálculo de rotas.
 * Abstrai o algoritmo utilizado (por exemplo, DijkstraSingleton)
 * e expõe uma interface simples para o restante da aplicação.
 */
public interface CalculadoraRotaAdapter {

    /**
     * Calcula o caminho entre dois bairros.
     *
     * @param origemId  id do bairro de origem
     * @param destinoId id do bairro de destino
     * @return lista de ids de bairros na ordem do percurso
     */
    List<Long> calcularRotaSimples(Long origemId, Long destinoId);

    /**
     * Calcula o caminho para uma rota com múltiplas paradas.
     *
     * @param bairrosIds lista ordenada de ids de bairros
     * @return lista de ids de bairros na ordem final do percurso
     */
    List<Long> calcularRotaMultiParadas(List<Long> bairrosIds);
}
