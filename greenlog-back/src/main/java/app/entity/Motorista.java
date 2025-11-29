package app.entity;

import app.enums.StatusMotorista;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "TB_MOTORISTA")
public class Motorista {

    @Id
    @Column(length = 14)
    private String cpf;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column
    private LocalDate data;

    @Column(length = 20)
    private String telefone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusMotorista status;

    public Motorista() {
    }

    public Motorista(String cpf, String nome, LocalDate data, String telefone, StatusMotorista status) {
        this.cpf = cpf;
        this.nome = nome;
        this.data = data;
        this.telefone = telefone;
        this.status = status;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public StatusMotorista getStatus() {
        return status;
    }

    public void setStatus(StatusMotorista status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Motorista motorista)) return false;
        return Objects.equals(cpf, motorista.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }
}
