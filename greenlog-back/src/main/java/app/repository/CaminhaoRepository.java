package app.repository;

import app.entity.Caminhao;
import app.enums.StatusCaminhao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaminhaoRepository extends JpaRepository<Caminhao, Long> {

    boolean existsByPlacaIgnoreCase(String placa); // usado no criar

    Optional<Caminhao> findByPlacaIgnoreCase(String placa); // usado no buscar/atualizar/excluir

    List<Caminhao> findByStatus(StatusCaminhao status);
}
