package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Bairro;
import app.entity.Rota;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {

    List<Rota> findByOrigemAndDestino(Bairro origem, Bairro destino);
}
