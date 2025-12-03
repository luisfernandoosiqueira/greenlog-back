package app.entity;

import app.enums.TipoResiduo;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "TB_PONTO_COLETA")
public class PontoColeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(length = 120)
    private String responsavel;

    @Column(length = 20)
    private String telefone;

    @Column(length = 120)
    private String email;

    @Column(length = 200)
    private String endereco;

    @Column(length = 5)
    private String horaEntrada;

    @Column(length = 5)
    private String horaSaida;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "TB_PONTO_COLETA_TIPO_RESIDUO",
            joinColumns = @JoinColumn(name = "ponto_coleta_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_residuo", nullable = false, length = 20)
    private Set<TipoResiduo> tiposResiduo = new HashSet<>();

    @Column(nullable = false)
    private Integer quantidadeResiduosKg = 0;

    public PontoColeta() {
    }

    public Long getId() {
        return id;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(String horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public Set<TipoResiduo> getTiposResiduo() {
        return tiposResiduo;
    }

    public void setTiposResiduo(Set<TipoResiduo> tiposResiduo) {
        this.tiposResiduo = tiposResiduo;
    }

    public Integer getQuantidadeResiduosKg() {
        return quantidadeResiduosKg;
    }

    public void setQuantidadeResiduosKg(Integer quantidadeResiduosKg) {
        this.quantidadeResiduosKg = quantidadeResiduosKg != null ? quantidadeResiduosKg : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PontoColeta that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
