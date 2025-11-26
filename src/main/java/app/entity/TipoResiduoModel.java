package app.entity;

import app.enums.TipoResiduo;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "TB_TIPO_RESIDUO")
public class TipoResiduoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 40)
    private TipoResiduo tipoResiduo;

    public TipoResiduoModel() {
    }

    public TipoResiduoModel(TipoResiduo tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public Long getId() {
        return id;
    }

    public TipoResiduo getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(TipoResiduo tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoResiduoModel that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
