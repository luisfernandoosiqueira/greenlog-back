package app.events;

import app.entity.Itinerario;

/**
 * Evento disparado quando um novo itinerário é criado.
 *
 * Padrão de projeto: Observer
 * - Publisher: ItinerarioService
 * - Subscriber: ItinerarioEventListener (pode registrar logs, enviar notificações etc.)
 */
public class ItinerarioCriadoEvent {

    private final Itinerario itinerario;

    public ItinerarioCriadoEvent(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }
}
