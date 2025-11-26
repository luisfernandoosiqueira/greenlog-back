package app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Caminhao;
import app.entity.Itinerario;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    boolean existsByCaminhaoAndData(Caminhao caminhao, LocalDate data);

    List<Itinerario> findByData(LocalDate data);
}
