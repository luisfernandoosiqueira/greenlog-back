package app.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "TB_RUA_CONEXAO")
public class RuaConexao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bairro_origem_id", nullable = false)
    private Bairro origem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bairro_destino_id", nullable = false)
    private Bairro destino;

    @Column(nullable = false)
    private Double distanciaKm;

    public RuaConexao() {
    }

    public RuaConexao(Bairro origem, Bairro destino, Double distanciaKm) {
        this.origem = origem;
        this.destino = destino;
        this.distanciaKm = distanciaKm;
    }

    public Long getId() {
        return id;
    }

    public Bairro getOrigem() {
        return origem;
    }

    public void setOrigem(Bairro origem) {
        this.origem = origem;
    }

    public Bairro getDestino() {
        return destino;
    }

    public void setDestino(Bairro destino) {
        this.destino = destino;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RuaConexao that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
