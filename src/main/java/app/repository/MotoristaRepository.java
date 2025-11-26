package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Motorista;
import app.enums.StatusMotorista;

@Repository
public interface MotoristaRepository extends JpaRepository<Motorista, String> {

    List<Motorista> findByStatus(StatusMotorista status);

    List<Motorista> findByNomeContainingIgnoreCase(String nome);
}
