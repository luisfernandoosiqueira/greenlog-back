package app.entity;

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

    @Column(length = 60)
    private String horarioFuncionamento;

    @ManyToMany
    @JoinTable(
            name = "TB_PONTO_COLETA_TIPO_RESIDUO",
            joinColumns = @JoinColumn(name = "ponto_coleta_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_residuo_id")
    )
    private Set<TipoResiduoModel> tiposResiduo = new HashSet<>();

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

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public Set<TipoResiduoModel> getTiposResiduo() {
        return tiposResiduo;
    }

    public void setTiposResiduo(Set<TipoResiduoModel> tiposResiduo) {
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
