package app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Caminhao;
import app.entity.Itinerario;
import app.entity.Rota;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    // consulta pela data de agendamento (campo real da entidade)
    List<Itinerario> findByDataAgendamento(LocalDate dataAgendamento);

    // navega pela associação Itinerario -> Rota -> Caminhao
    boolean existsByRota_CaminhaoAndDataAgendamento(Caminhao caminhao, LocalDate dataAgendamento);

    // para bloquear exclusão de rota quando houver itinerário vinculado
    boolean existsByRota(Rota rota);
}
