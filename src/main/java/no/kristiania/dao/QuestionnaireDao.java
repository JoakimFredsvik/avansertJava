package no.kristiania.dao;

import no.kristiania.dao.entity.Question;
import no.kristiania.dao.entity.Questionnaire;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireDao extends AbstractDao<Questionnaire>{
    @Override
    public void delete(long qid) throws SQLException {
        delete(qid, "UPDATE questionnaire SET deleted=true WHERE questionnaire_id = ?");
    }

    public QuestionnaireDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Questionnaire entity) throws SQLException {
        long id = save(entity, "insert into questionnaire (name) values (?)");
        entity.setId(id);
    }

    @Override
    public Questionnaire retrieve(long id) throws SQLException {
        return retrieve(id, "SELECT * FROM QUESTIONNAIRE where questionnaire_id = ? and deleted = false");
    }

    @Override
    protected Questionnaire mapFromResultSet(ResultSet rs) throws SQLException {
        Questionnaire q = new Questionnaire();
        q.setName(rs.getString("name"));
        q.setId(rs.getLong("questionnaire_id"));
        q.setQuestions((ArrayList<Question>) retrieveQuestions(q.getId()));
        return q;
    }

    private List<Question> retrieveQuestions(long id) throws SQLException {
        AbstractDao<Question> dao = new QuestionDao(getDataSource());
        return dao.listWithId(id);
    }

    @Override
    protected void mapToStatement(Questionnaire entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getName());
    }

    @Override
    public List<Questionnaire> listWithId(long id) {
        return null;
    }

    public List<Questionnaire> listAll() throws SQLException {
        return listAll("SELECT * FROM questionnaire WHERE deleted = false");
    }

    @Override
    protected void mapToUpdate(Questionnaire entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setLong(2, entity.getId());
    }

    @Override
    public void update(Questionnaire entity) throws SQLException {
        update(entity,"UPDATE questionnaire SET name = ? WHERE questionnaire_id = ?");
    }

    @Override
    public Questionnaire retrieve(String string) throws SQLException {
        return null;
    }
}
