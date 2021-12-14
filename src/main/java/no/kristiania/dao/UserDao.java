package no.kristiania.dao;

import no.kristiania.dao.entity.User;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDao extends AbstractDao<User>{

    public UserDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(User entity) throws SQLException {
        long id = save(entity, "insert into user_table (user_name) values (?)");
        entity.setId(id);
    }

    @Override
    public User retrieve(long id) throws SQLException {
        return retrieve(id, "SELECT * FROM user_table WHERE user_id = ?");
    }


    public User retrieve(String name) throws SQLException {
        return retrieve(name, "SELECT * FROM user_table WHERE user_name = ?");
    }

    @Override
    public List<User> listAll() throws SQLException {
        return listAll("SELECT * FROM user_table");
    }

    @Override
    protected User mapFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setName(rs.getString("user_name"));
        user.setId(rs.getLong("user_id"));
        return user;
    }

    @Override
    protected void mapToStatement(User user, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, user.getName());
    }

    @Override
    public List<User> listWithId(long id) throws SQLException {
        return null;
    }

    @Override
    public void delete(long id) throws SQLException {
        delete(id, "DELETE FROM user WHERE user_id = ?");
    }

    @Override
    protected void mapToUpdate(User entity, PreparedStatement statement) throws SQLException {

    }

    @Override
    public void update(User entity) throws SQLException {

    }
}
