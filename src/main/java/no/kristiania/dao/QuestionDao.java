package no.kristiania.dao;

import no.kristiania.dao.entity.Alternative;
import no.kristiania.dao.entity.Question;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuestionDao extends AbstractDao<Question>{

    public QuestionDao(DataSource dataSource) {
        super(dataSource);
    }

    public void save(Question question) throws SQLException {
        long id = save(question, "insert into question (questionnaire_id, text) values (?, ?)");
        question.setId(id);
    }

    public void delete (long id) throws SQLException {
        delete(id, "UPDATE question SET deleted=true WHERE question_id = ?");

    }
    @Override
    public Question retrieve(long id) throws SQLException {
        return retrieve(id, "SELECT * FROM QUESTION where question_id = ? and deleted = false");
    }

    private List<Alternative> retrieveAlternatives(long id) throws SQLException {
        AbstractDao<Alternative> dao = new AlternativeDao(getDataSource());
        return dao.listWithId(id);
    }

    @Override
    protected Question mapFromResultSet(ResultSet rs) throws SQLException {
        Question question = new Question();
        question.setId(rs.getLong("question_id"));
        question.setQuestionText(rs.getString("text"));
        question.setQuestionnaireId(rs.getInt("questionnaire_id"));
        question.setAlternatives((ArrayList<Alternative>) retrieveAlternatives(question.getId()));
        return question;
    }


    @Override
    protected void mapToStatement(Question question, PreparedStatement stmt) throws SQLException {
        stmt.setLong(1, question.getQuestionnaireId());
        stmt.setString(2, question.getQuestionText());
    }

    @Override
    public List<Question> listAll() throws SQLException {
        return listAll("SELECT * FROM question");
    }

    public List<Question> listWithId(long id) throws SQLException {
        return listWithId(id, "SELECT * FROM question where questionnaire_id = ? and deleted = false");
    }

    @Override
    protected void mapToUpdate(Question entity, PreparedStatement statement) throws SQLException {
        statement.setString(1, entity.getQuestionText());
        statement.setLong(2, entity.getId());
    }

    @Override
    public void update(Question entity) throws SQLException {
        update(entity, "UPDATE question SET text = ? WHERE question_id = ?");
    }

    @Override
    public Question retrieve(String string) throws SQLException {
        return null;
    }
}
