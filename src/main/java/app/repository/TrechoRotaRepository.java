package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Rota;
import app.entity.TrechoRota;

@Repository
public interface TrechoRotaRepository extends JpaRepository<TrechoRota, Long> {

    // Trechos da rota em ordem de criação (ID crescente)
    List<TrechoRota> findByRotaOrderByIdAsc(Rota rota);
}
