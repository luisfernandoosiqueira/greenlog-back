package app.events;

import app.entity.Itinerario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listener que reage à criação de novos itinerários.
 *
 * Padrão de projeto: Observer
 * - Observa eventos do tipo ItinerarioCriadoEvent.
 * - Pode registrar logs, acionar notificações externas etc.
 */
@Component
public class ItinerarioEventListener {

    private static final Logger log = LoggerFactory.getLogger(ItinerarioEventListener.class);

    @EventListener
    public void onItinerarioCriado(ItinerarioCriadoEvent event) {
        Itinerario it = event.getItinerario();
        if (it != null && it.getRota() != null) {
            log.info("Novo itinerário criado. ID: {}, Rota: {}, Data: {}",
                    it.getId(),
                    it.getRota().getNome(),
                    it.getDataAgendamento());
        } else {
            log.info("Novo itinerário criado.");
        }
    }
}
