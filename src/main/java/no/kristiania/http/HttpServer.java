package no.kristiania.http;

import no.kristiania.dao.*;
import no.kristiania.dao.entity.*;
import no.kristiania.http.controller.*;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

public class HttpServer {
    private final Thread thread;
    private final ServerSocket serverSocket;
    private final HashMap<String, HttpController> controllers = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public HttpServer(int serverPort) throws IOException {
        serverSocket = new ServerSocket(serverPort);

        thread = new Thread(this::handleClients);
        thread.start();
    }

    public void join() throws InterruptedException {
        thread.join();
    }

    public static void main(String[] args) {
        try {
            HttpServer server = new HttpServer(1968);
            DataSource dataSource = createDataSource();

            setupControllers(server, dataSource);
            logger.info("Server started");
            logger.info("Visit http://localhost:{}/index.html", server.getPort());

            server.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void setupControllers(HttpServer server, DataSource dataSource) {
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

    public void addController(String target, HttpController controller) {
        controllers.put(target, controller);
    }

    private void handleClients() {
        try {
            while (true) {
                handleClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient() throws IOException {
        Socket clientSocket = serverSocket.accept();
        Request request = new Request(clientSocket);

        Response response;

        if (controllers.containsKey(request.getTarget())) {
            response = controllers.get(request.getTarget()).handle(request);
            response.send(clientSocket);
        } else {
            response = new FileController(clientSocket).handle(request);
            if (response != null) response.send(clientSocket);
        }
        clientSocket.close();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    private static DataSource createDataSource() throws IOException {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader("pgr203.properties")) {
            properties.load(reader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty(
                "dataSource.url"
        ));
        dataSource.setUser(properties.getProperty("dataSource.username", "postgres"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));
        Flyway.configure().dataSource(dataSource).load().migrate();
        return dataSource;
    }
}