package app.entity;

import app.enums.StatusCaminhao;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "TB_CAMINHAO",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_CAMINHAO_PLACA", columnNames = "placa")
        }
)
public class Caminhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id técnico
    private Long id;

    @Column(nullable = false, length = 10, unique = true) // placa única
    private String placa;

    @ManyToOne
    @JoinColumn(name = "motorista_cpf", nullable = false)
    private Motorista motorista;

    @Column(nullable = false)
    private Integer capacidadeKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusCaminhao status;

    @ManyToMany
    @JoinTable(
            name = "TB_CAMINHAO_TIPO_RESIDUO",
            joinColumns = @JoinColumn(name = "caminhao_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_residuo_id")
    )
    private Set<TipoResiduoModel> tiposResiduo = new HashSet<>();

    public Caminhao() {
    }

    public Caminhao(String placa, Motorista motorista, Integer capacidadeKg, StatusCaminhao status) {
        this.placa = placa;
        this.motorista = motorista;
        this.capacidadeKg = capacidadeKg;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Integer getCapacidadeKg() {
        return capacidadeKg;
    }

    public void setCapacidadeKg(Integer capacidadeKg) {
        this.capacidadeKg = capacidadeKg;
    }

    public StatusCaminhao getStatus() {
        return status;
    }

    public void setStatus(StatusCaminhao status) {
        this.status = status;
    }

    public Set<TipoResiduoModel> getTiposResiduo() {
        return tiposResiduo;
    }

    public void setTiposResiduo(Set<TipoResiduoModel> tiposResiduo) {
        this.tiposResiduo = tiposResiduo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caminhao caminhao)) return false;
        return Objects.equals(id, caminhao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
