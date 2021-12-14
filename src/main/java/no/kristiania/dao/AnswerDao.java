package no.kristiania.dao;

import no.kristiania.dao.entity.Answer;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AnswerDao extends AbstractDao<Answer> {
    public AnswerDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(Answer entity) throws SQLException {
        long id = save(entity, "insert into answer (answer_text, question_id, user_id) values (?, ?, ?)");
        entity.setId(id);
    }

    @Override //Wierd? would return more than one answer?!?!?!?
    public Answer retrieve(long id) throws SQLException {
        return retrieve(id, "select q.text, a.answer_text, u.user_name,  qu.name\n" +
                "from user_table u\n" +
                "join answer a on u.user_id = a.user_id\n" +
                "join question q on q.question_id = a.question_id\n" +
                "join questionnaire qu on qu.questionnaire_id = q.questionnaire_id\n" +
                "where u.user_id = ?;");
    }

    @Override
    public List<Answer> listAll() throws SQLException {
        return listAll("select q.text, a.answer_text, u.user_name,  qu.name\n" +
                "from user_table u\n" +
                "join answer a on u.user_id = a.user_id\n" +
                "join question q on q.question_id = a.question_id\n" +
                "join questionnaire qu on qu.questionnaire_id = q.questionnaire_id\n");
    }

    @Override
    protected Answer mapFromResultSet(ResultSet rs) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerText(rs.getString("answer_text"));
        answer.setUsername(rs.getString("user_name"));
        answer.setQuestionText(rs.getString("text"));
        answer.setQuestionnaireName(rs.getString("name"));

        return answer;
    }

    @Override
    protected void mapToStatement(Answer entity, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, entity.getAnswerText());
        stmt.setLong(2, entity.getQuestionId());
        stmt.setLong(3, entity.getUserId());
    }

    @Override
    public List<Answer> listWithId(long id) throws SQLException {
        return listWithId(id, "select q.text, a.answer_text, u.user_name,  qu.name\n" +
                "from user_table u\n" +
                "join answer a on u.user_id = a.user_id\n" +
                "join question q on q.question_id = a.question_id\n" +
                "join questionnaire qu on qu.questionnaire_id = q.questionnaire_id\n"+
                "where u.user_id = ?");
    }

    @Override
    public void delete(long qid) throws SQLException {

    }

    @Override
    protected void mapToUpdate(Answer entity, PreparedStatement statement) throws SQLException {

    }

    @Override
    public void update(Answer entity) throws SQLException {

    }

    @Override
    public Answer retrieve(String string) throws SQLException {
        return null;
    }
}
