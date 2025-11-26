package app.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "TB_TRECHO_ROTA")
public class TrechoRota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rota_id", nullable = false)
    private Rota rota;

    @ManyToOne(optional = false)
    @JoinColumn(name = "rua_conexao_id", nullable = false)
    private RuaConexao ruaConexao;

    @Column(nullable = false)
    private Integer ordem;

    public TrechoRota() {
    }

    public TrechoRota(Rota rota, RuaConexao ruaConexao, Integer ordem) {
        this.rota = rota;
        this.ruaConexao = ruaConexao;
        this.ordem = ordem;
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

    public RuaConexao getRuaConexao() {
        return ruaConexao;
    }

    public void setRuaConexao(RuaConexao ruaConexao) {
        this.ruaConexao = ruaConexao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrechoRota trechoRota)) return false;
        return Objects.equals(id, trechoRota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
