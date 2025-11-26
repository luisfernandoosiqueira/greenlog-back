package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Rota;
import app.entity.TrechoRota;

@Repository
public interface TrechoRotaRepository extends JpaRepository<TrechoRota, Long> {

    List<TrechoRota> findByRotaOrderByOrdemAsc(Rota rota);
}
