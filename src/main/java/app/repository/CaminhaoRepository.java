package app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.Caminhao;
import app.enums.StatusCaminhao;

@Repository
public interface CaminhaoRepository extends JpaRepository<Caminhao, String> {

    List<Caminhao> findByStatus(StatusCaminhao status);
}
