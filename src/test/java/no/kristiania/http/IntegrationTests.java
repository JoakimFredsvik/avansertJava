package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.dao.entity.*;
import no.kristiania.http.controller.*;
import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTests {
    private final HttpServer server = new HttpServer(0);

    public IntegrationTests() throws IOException {
    }

    @BeforeEach
    void setUp() {
        DataSource dataSource = testDataSource();

        AbstractDao<Questionnaire> questionnaireDao = new QuestionnaireDao(dataSource);
        HttpController questionnaireController = new QuestionnaireController(questionnaireDao);

        AbstractDao<User> userDao = new UserDao(dataSource);
        HttpController userController = new UserController(userDao);

        AbstractDao<Answer> answerDao = new AnswerDao(dataSource);
        HttpController answerController = new AnswerController(answerDao);

        AbstractDao<Question> questionDao = new QuestionDao(dataSource);
        HttpController questionController = new QuestionController(questionDao);

        AbstractDao<Alternative> alternativeDao = new AlternativeDao(dataSource);
        HttpController alternativeController = new AlternativeController(alternativeDao);

        server.addController("/api/user", userController);
        server.addController("/api/questionnaire", questionnaireController);
        server.addController("/api/answer", answerController);
        server.addController("/api/question", questionController);
        server.addController("/api/alternative", alternativeController);

    }

    @Test
    void shouldAddUserAndRespondWithCookie() throws IOException {
        HttpPost client = new HttpPost("localhost",
                server.getPort(),
                "/api/user",
                "name=test");
        String expected = ";path=/";
        assertTrue(client.getResponse().getHeader("Set-Cookie").contains(expected));
    }

    @Test
    void shouldAddQuestionnaire() throws IOException {
        new HttpPost("localhost",
                server.getPort(),
                "/api/questionnaire",
                "name=test");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=all&editable=false");

        assertTrue(getClient.response().toString().contains("<p>test</p>"));
    }

    @Test
    void shouldAddQuestion() throws IOException {
        new HttpPost("localhost",
                server.getPort(),
                "/api/question",
                "text=shouldexist&questionnaire_id=1");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=id&id=1&editable=false");

        String expected = "shouldexist";
        assertTrue(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldAddAlternative() throws IOException {
        new HttpPost("localhost",
                server.getPort(),
                "/api/alternative",
                "text=shouldexistalso&question_id=1");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=id&id=1&editable=false");

        String expected = "shouldexistalso";

        assertTrue(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldRemoveQuestion() throws IOException {
        String expected = "Do you eat Breakfast";
        new HttpPost("localhost",
                server.getPort(),
                "/api/question",
                "type=delete&id=1");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=all&editable=false");

        assertFalse(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldRemoveQuestionnaire() throws IOException {
        String expected = "Media Usage";
        new HttpPost("localhost",
                server.getPort(),
                "/api/questionnaire",
                "type=delete&id=2");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=all&editable=false");


        assertFalse(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldAddAnswer() throws IOException {
        new HttpPost("localhost",
                server.getPort(),
                "/api/answer",
                "question1=shouldexist&user_id=1");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/answer?type=all");


        String expected = "shouldexist";

        assertTrue(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldGetAnswersForUser1() throws IOException {
        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/answer?type=id&id=1");
        String expected = "Roger";

        assertTrue(getClient.response().toString().contains(expected));
    }

    @Test
    void shouldUpdateQuestionnaireName() throws IOException {
        String shouldNotBeThere = "shouldbechanged";
        String shouldNowBeThere = "updated";

        new HttpPost("localhost",
                server.getPort(),
                "/api/questionnaire",
                "name=updated&id=1&type=update");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=all&editable=false");

        assertFalse(getClient.response().toString().contains(shouldNotBeThere));
        assertTrue(getClient.response().toString().contains(shouldNowBeThere));
    }

    @Test
    void shouldUpdateQuestionText() throws IOException {
        String shouldNotBeThere = "Breakfast is the most important meal of the day";
        String shouldNowBeThere = "updatedquestion";

        new HttpPost("localhost",
                server.getPort(),
                "/api/question",
                "text=updatedquestion&id=2&type=update");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=id&id=1&editable=false");

        assertFalse(getClient.response().toString().contains(shouldNotBeThere));
        assertTrue(getClient.response().toString().contains(shouldNowBeThere));
    }

    @Test
    void shouldUpdateAlternativeText() throws IOException {
        String shouldNowBeThere = "updatedalternative";

        new HttpPost("localhost",
                server.getPort(),
                "/api/alternative",
                "text=updatedalternative&id=3&type=update");

        HttpGet getClient = new HttpGet("localhost",
                server.getPort(),
                "/api/questionnaire?type=all&editable=false");

        assertTrue(getClient.response().toString().contains(shouldNowBeThere));
    }

    public static DataSource testDataSource() {
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setUrl("jdbc:h2:mem:exam;DB_CLOSE_DELAY=-1");
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}