package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Rota;

@Repository
public interface RotaRepository extends JpaRepository<Rota, Long> {

 
}
