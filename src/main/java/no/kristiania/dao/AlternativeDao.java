package no.kristiania.dao;

import no.kristiania.dao.entity.Alternative;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AlternativeDao extends AbstractDao<Alternative>{
    public AlternativeDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Alternative entity) throws SQLException {
        long id = save(entity, "insert into alternative (alternative_text, question_id) values (?, ?)");
        entity.setId(id);
    }

    @Override
    public Alternative retrieve(long id) throws SQLException {
        return retrieve(id, "SELECT * FROM alternative where alternative_id = ?");
    }

    @Override
    public List<Alternative> listAll() throws SQLException {
        return listAll("select * from alternative");
    }
    @Override
    public List<Alternative> listWithId(long id) throws SQLException {
        return listWithId(id,"select * from alternative where question_id = ?");
    }

    @Override
    protected Alternative mapFromResultSet(ResultSet rs) throws SQLException {
        Alternative alternative = new Alternative();
        alternative.setId(rs.getLong("alternative_id"));
        alternative.setText(rs.getString("alternative_text"));
        alternative.setQuestion_id(rs.getInt("question_id"));

        return alternative;
    }

    @Override
    protected void mapToStatement(Alternative entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getText());
        stmt.setLong(2, entity.getQuestion_id());
    }

    @Override
    public void delete(long qid) throws SQLException {

    }

    @Override
    protected void mapToUpdate(Alternative entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getText());
        statement.setLong(2, entity.getId());
    }

    @Override
    public void update(Alternative entity) throws SQLException {
        update(entity, "UPDATE alternative SET alternative_text = ? WHERE alternative_id = ?");
    }

    @Override
    public Alternative retrieve(String string) throws SQLException {
        return null;
    }
}
