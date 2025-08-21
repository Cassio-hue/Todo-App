package Task;

import java.util.Objects;

public class Task {
    private int id;
    private String descricao;
    private Boolean concluido;

    public Task() {}

    public Task(String descricao) {
        this.descricao = descricao;
    }

    public Task(int id, String descricao,  Boolean concluido) {
        this.id = id;
        this.descricao = descricao;
        this.concluido = concluido;
    }

    public int getId() {
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
