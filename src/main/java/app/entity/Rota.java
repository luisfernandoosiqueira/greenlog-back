package app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "TB_ROTA")
public class Rota {

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
    private Double distanciaTotal;

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrechoRota> trechos = new ArrayList<>();

    public Rota() {
    }

    public Rota(Bairro origem, Bairro destino, Double distanciaTotal) {
        this.origem = origem;
        this.destino = destino;
        this.distanciaTotal = distanciaTotal;
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

    public Double getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(Double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public List<TrechoRota> getTrechos() {
        return trechos;
    }

    public void setTrechos(List<TrechoRota> trechos) {
        this.trechos = trechos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rota rota)) return false;
        return Objects.equals(id, rota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
