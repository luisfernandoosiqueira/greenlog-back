package app.events;

/**
 * Evento de domínio disparado sempre que o grafo de bairros/ruas é alterado.
 *
 * Padrão de projeto: Observer (Publisher/Subscriber)
 * - Publisher: serviços que alteram o grafo (ex.: RuaConexaoService)
 * - Subscriber: GrafoEventListener (recarrega/invalida o grafo em memória)
 */
public class GrafoAtualizadoEvent {

    private final String origem;

    public GrafoAtualizadoEvent(String origem) {
        this.origem = origem;
    }

    public String getOrigem() {
        return origem;
    }
}
