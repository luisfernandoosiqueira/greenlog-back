package app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "TB_ITINERARIO",
        indexes = @Index(name = "idx_itinerario_data", columnList = "data_agendamento")
)
public class Itinerario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Rota selecionada (já contém caminhão, tipo de resíduo, trechos etc.)
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;

    // Data agendada para executar a rota (não pode ser no passado)
    @NotNull
    @FutureOrPresent
    @Column(name = "data_agendamento", nullable = false)
    private LocalDate dataAgendamento;

    // Distância total da rota (cópia da rota, útil para histórico)
    @Positive
    @Column(name = "distancia_total", nullable = false)
    private Double distanciaTotal;

    @Column(nullable = false)
    private Boolean ativo = true;

    public Itinerario() {
    }

    public Itinerario(Rota rota, LocalDate dataAgendamento) {
        this.rota = rota;
        this.dataAgendamento = dataAgendamento;
        this.distanciaTotal = rota.getDistanciaTotal();
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public Rota getRota() {
        return rota;
    }

    public void setRota(Rota rota) {
        this.rota = rota;
    }

    public LocalDate getDataAgendamento() {
        return dataAgendamento;
    }

    public void setDataAgendamento(LocalDate dataAgendamento) {
        this.dataAgendamento = dataAgendamento;
    }

    public Double getDistanciaTotal() {
        return distanciaTotal;
    }

    public void setDistanciaTotal(Double distanciaTotal) {
        this.distanciaTotal = distanciaTotal;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Itinerario that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
