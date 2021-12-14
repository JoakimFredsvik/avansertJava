package no.kristiania.dao;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class AbstractDao<T> {
    private final DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected void delete(long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                statement.execute();
            }
        }
    }

    protected long save(T entity, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS

            )) {
                mapToStatement(entity, statement);
                statement.executeUpdate();

                ResultSet generatedKeys = statement.getGeneratedKeys();
                generatedKeys.next();
                return generatedKeys.getLong(trimClassName() + "_id");
            }
        }
    }

    private String trimClassName() {
        String classname = this.getClass().getSimpleName().toLowerCase(Locale.ROOT);
        return classname.substring(0, classname.length() - 3);
    }

    protected void update(T entity, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                mapToUpdate(entity, statement);
                statement.execute();
            }
        }
    }

    protected abstract void mapToUpdate(T entity, PreparedStatement statement) throws SQLException;

    public abstract void update(T entity) throws SQLException;

    public abstract void save(T entity) throws SQLException;

    public abstract T retrieve(long id) throws SQLException;

    public abstract T retrieve(String string) throws SQLException;

    protected T retrieve(long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    protected T retrieve(String string, String sql) throws SQLException {
        try (Connection connection = getDataSource().getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, string);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return mapFromResultSet(rs);
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    public List<T> listAll(String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    ArrayList<T> result = new ArrayList<>();

                    while (rs.next()) {
                        result.add(mapFromResultSet(rs));
                    }
                    Collections.sort((ArrayList) result);
                    return result;
                }
            }
        }
    }

    protected List<T> listWithId(long id, String sql) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    List<T> result = new ArrayList<>();
                    while (rs.next()) {
                        result.add(mapFromResultSet(rs));
                    }
                    Collections.sort((ArrayList) result);
                    return result;

                }
            }
        }
    }

    protected DataSource getDataSource() {
        return this.dataSource;
    }

    public abstract List<T> listAll() throws SQLException;

    protected abstract T mapFromResultSet(ResultSet rs) throws SQLException;

    protected abstract void mapToStatement(T member, PreparedStatement stmt) throws SQLException;

    public abstract List<T> listWithId(long id) throws SQLException;

    public abstract void delete(long qid) throws SQLException;
}