package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Bairro;
import app.entity.PontoColeta;

@Repository
public interface PontoColetaRepository extends JpaRepository<PontoColeta, Long> {

    List<PontoColeta> findByBairro(Bairro bairro);

    List<PontoColeta> findByNomeContainingIgnoreCase(String nome);
}
