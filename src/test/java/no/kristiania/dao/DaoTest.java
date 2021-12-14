package no.kristiania.dao;

import no.kristiania.dao.entity.*;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DaoTest {
    AbstractDao<Questionnaire> questionnaireAbstractDao;
    AbstractDao<Question> questionAbstractDao;
    AbstractDao<Answer> answerAbstractDao;
    AbstractDao<User> userAbstractDao;
    AbstractDao<Alternative> alternativeAbstractDao;
    DataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = testDataSource();

        questionnaireAbstractDao = new QuestionnaireDao(dataSource);
        questionAbstractDao = new QuestionDao(dataSource);
        answerAbstractDao = new AnswerDao(dataSource);
        userAbstractDao = new UserDao(dataSource);
        alternativeAbstractDao = new AlternativeDao(dataSource);
    }

    @Test
    void ShouldSaveQuestionnaireID3() throws SQLException {
        Questionnaire q = new Questionnaire();
        q.setName("BurdeFunke2");
        questionnaireAbstractDao.save(q);
        assertTrue(questionnaireAbstractDao.listAll().contains(q));
    }


    @Test
    void shouldSaveQuestion() throws SQLException {
        Question q = new Question();
        q.setQuestionText("text");
        q.setQuestionnaireId(1);
        questionAbstractDao.save(q);

        assertTrue(questionAbstractDao.listAll().contains(q));
    }


    @Test
    void shouldListAll() throws SQLException {
        Questionnaire q = new Questionnaire();
        q.setName("q1");
        Questionnaire q2 = new Questionnaire();
        q2.setName("q2");

        questionnaireAbstractDao.save(q);
        questionnaireAbstractDao.save(q2);
        List<Questionnaire> list = questionnaireAbstractDao.listAll();

        assertEquals(5, list.size());
    }

    @Test
    void shouldSaveAndReturnAlternative() throws SQLException {
        Alternative a = new Alternative();
        a.setText("one");
        a.setQuestion_id(1);

        alternativeAbstractDao.save(a);



        assertTrue(alternativeAbstractDao.listAll().contains(a));
    }

   @Test
    void shouldUpdateQuestionnaireName() throws SQLException {
        String before = "Eating Habits";

        Questionnaire updated = new Questionnaire();
        updated.setName("updatedName");
        updated.setId(1);
        questionnaireAbstractDao.update(updated);

        String after = questionnaireAbstractDao.retrieve(1).getName();

        assertEquals(after, "updatedName");
        assertNotEquals(before, after);
    }

    @Test
    void shouldUpdateQuestionText() throws SQLException {
        String before = questionAbstractDao.retrieve(2).getQuestionText();

        Question updated = new Question();
        updated.setQuestionText("updatedText");
        updated.setId(2);
        questionAbstractDao.update(updated);

        String after = questionAbstractDao.retrieve(2).getQuestionText();

        assertEquals(after, "updatedText");
        assertNotEquals(before, after);
    }

    @Test
    void shouldUpdateAlternativeText() throws SQLException {
        String before = alternativeAbstractDao.retrieve(2).getText();

        Alternative updated = new Alternative();
        updated.setText("updatedText");
        updated.setId(2);
        alternativeAbstractDao.update(updated);

        String after = alternativeAbstractDao.retrieve(2).getText();

        assertEquals(after, "updatedText");
        assertNotEquals(before, after);
    }


    @Test
    void shouldReturnUserByName() throws SQLException {
        User expected = userAbstractDao.retrieve(1);
        User result = userAbstractDao.retrieve("Roger");

        assertEquals(expected, result);
    }

    @Test
    void shouldCreateAndReturnUser() throws SQLException {
        User user = new User();
        user.setName("Arne");
        userAbstractDao.save(user);

        assertEquals(user, userAbstractDao.retrieve("Arne"));
    }

    @Test
    void shouldReturnAnswer() throws SQLException {
        User user = new User();
        user.setName("Bjarte");
        userAbstractDao.save(user);

        Answer answer = new Answer();
        answer.setAnswerText("Im the best");
        answer.setQuestionId(1);
        answer.setQuestionText("Do you eat Breakfast?");
        answer.setUserId(1);

        answerAbstractDao.save(answer);
        assertEquals(answer.getQuestionText(), answerAbstractDao.retrieve(1).getQuestionText());
    }

    @Test
    void shouldListAllAnswers() throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerText("Im the best");
        answer.setQuestionId(1);
        answer.setQuestionText("Do you eat Breakfast?");
        answer.setUserId(1);

        answerAbstractDao.save(answer);
        Answer answer2 = new Answer();
        answer2.setAnswerText("Im the best");
        answer2.setQuestionId(1);
        answer2.setQuestionText("Do you eat Breakfast?");
        answer2.setUserId(1);

        answerAbstractDao.save(answer2);
        List<Answer> result = answerAbstractDao.listAll();
        assertTrue(result.size()>=3);
    }

    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:exam;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}
