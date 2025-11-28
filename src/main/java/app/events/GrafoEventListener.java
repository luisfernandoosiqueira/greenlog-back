package app.events;

import app.service.GrafoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener responsável por reagir às mudanças no grafo da cidade.
 *
 * Padrão de projeto: Observer
 * - Observa eventos do tipo GrafoAtualizadoEvent.
 * - Ao receber o evento, invalida (ou recarrega) o cache do grafo
 *   utilizado pelo DijkstraSingleton.
 */
@Component
public class GrafoEventListener {

    private static final Logger log = LoggerFactory.getLogger(GrafoEventListener.class);

    private final GrafoService grafoService;

    public GrafoEventListener(GrafoService grafoService) {
        this.grafoService = grafoService;
    }

    @EventListener
    public void onGrafoAtualizado(GrafoAtualizadoEvent event) {
        log.info("Grafo atualizado. Origem da alteração: {}", event.getOrigem());

        // Invalida o cache para que o próximo cálculo de rota use os dados atualizados.
        grafoService.invalidarCache();

        // Se preferir já carregar de imediato, poderia chamar:
        // grafoService.carregarGrafo();
    }
}
