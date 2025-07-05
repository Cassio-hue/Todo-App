package task;

public class Task {
    private int id;
    private String desc;

    public Task(int id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return desc;
    }

    public void setDescricao(String descricao) {
        this.desc = descricao;
    }
}
