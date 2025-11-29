package app.patterns;

import app.dto.itinerario.ItinerarioRequestDTO;
import app.entity.Itinerario;

/**
 * Padrão Template Method aplicado ao processo de criação de itinerários.
 * Define a sequência de passos e delega os detalhes para subclasses.
 */
public abstract class AbstractItinerarioTemplate {

    /**
     * Método template que encapsula o fluxo completo de criação do itinerário.
     */
    public final Itinerario criarItinerario(ItinerarioRequestDTO dto) {
        validarDadosBasicos(dto);
        validarCaminhaoDisponivel(dto);
        validarCapacidadeResiduos(dto);
        montarRota(dto);
        return salvarItinerario(dto);
    }

    protected abstract void validarDadosBasicos(ItinerarioRequestDTO dto);

    protected abstract void validarCaminhaoDisponivel(ItinerarioRequestDTO dto);

    protected abstract void validarCapacidadeResiduos(ItinerarioRequestDTO dto);

    protected abstract void montarRota(ItinerarioRequestDTO dto);

    protected abstract Itinerario salvarItinerario(ItinerarioRequestDTO dto);
}
