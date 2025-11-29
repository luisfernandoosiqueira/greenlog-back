package app.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TB_ROTA")
public class Rota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_residuo_id", nullable = false)
    private TipoResiduoModel tipoResiduo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "caminhao_placa", nullable = false)
    private Caminhao caminhao;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    @Column(nullable = false)
    private Double pesoTotal; // em kg

    @Column(nullable = false)
    private Double distanciaTotal; // em km

    @OneToMany(mappedBy = "rota", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrechoRota> trechos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "TB_ROTA_PONTO_COLETA",
            joinColumns = @JoinColumn(name = "rota_id"),
            inverseJoinColumns = @JoinColumn(name = "ponto_coleta_id")
    )
    private Set<PontoColeta> pontosColeta = new HashSet<>();

    public Rota() {
    }

    public Rota(String nome,
                TipoResiduoModel tipoResiduo,
                Caminhao caminhao,
                LocalDateTime dataCriacao,
                Double pesoTotal,
                Double distanciaTotal) {
        this.nome = nome;
        this.tipoResiduo = tipoResiduo;
        this.caminhao = caminhao;
        this.dataCriacao = dataCriacao;
        this.pesoTotal = pesoTotal;
        this.distanciaTotal = distanciaTotal;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoResiduoModel getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(TipoResiduoModel tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public Caminhao getCaminhao() {
        return caminhao;
    }

    public void setCaminhao(Caminhao caminhao) {
        this.caminhao = caminhao;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Double getPesoTotal() {
        return pesoTotal;
    }

    public void setPesoTotal(Double pesoTotal) {
        this.pesoTotal = pesoTotal;
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

    public Set<PontoColeta> getPontosColeta() {
        return pontosColeta;
    }

    public void setPontosColeta(Set<PontoColeta> pontosColeta) {
        this.pontosColeta = pontosColeta;
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
