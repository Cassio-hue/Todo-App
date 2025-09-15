package h2factory.task;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Task")
public class Task implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "concluido")
    private Boolean concluido = false;

    public Task() {}

    public Task(String descricao) {
        this.descricao = descricao;
    }

    public Task(int id, String descricao,  Boolean concluido) {
        this.id = id;
        this.descricao = descricao;
        this.concluido = concluido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getConcluido() {
        return concluido;
    }

    public void setConcluido(Boolean concluido) {
        this.concluido = concluido;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(descricao, task.descricao) && Objects.equals(concluido, task.concluido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, concluido);
    }

    @Override
    public String toString() {
        return "Task ID: " + id + " | Descrição: " + descricao + " | Concluído: " + concluido;
    }
}
