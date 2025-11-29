package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entity.TipoResiduoModel;
import app.enums.TipoResiduo;

@Repository
public interface TipoResiduoModelRepository extends JpaRepository<TipoResiduoModel, Long> {

    Optional<TipoResiduoModel> findByTipoResiduo(TipoResiduo tipoResiduo);

    boolean existsByTipoResiduo(TipoResiduo tipoResiduo);
}
