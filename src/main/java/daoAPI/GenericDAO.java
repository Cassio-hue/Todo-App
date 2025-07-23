package daoAPI;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public abstract class GenericDAO<T> {

    protected final String tableName;
    protected Connection con;

    protected GenericDAO(Connection con, String tableName) {
        this.tableName = tableName;
        this.con = con;
    }

    protected abstract  Optional<T> get(long id);

    public abstract List<T> getAll();

    protected abstract void save(T t);

    protected abstract  void update(T t, String[] params);

    protected abstract  void delete(T t);
}