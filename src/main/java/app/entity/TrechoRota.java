package app.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
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
    @JoinColumn(name = "origem_bairro_id", nullable = false)
    private Bairro origem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destino_bairro_id", nullable = false)
    private Bairro destino;

    @ManyToMany
    @JoinTable(
            name = "TB_TRECHO_RUA_CONEXAO",
            joinColumns = @JoinColumn(name = "trecho_id"),
            inverseJoinColumns = @JoinColumn(name = "rua_conexao_id")
    )
    private List<RuaConexao> ruas = new ArrayList<>();

    public TrechoRota() {
    }

    public TrechoRota(Rota rota, Bairro origem, Bairro destino) {
        this.rota = rota;
        this.origem = origem;
        this.destino = destino;
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

    public List<RuaConexao> getRuas() {
        return ruas;
    }

    public void setRuas(List<RuaConexao> ruas) {
        this.ruas = ruas;
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
