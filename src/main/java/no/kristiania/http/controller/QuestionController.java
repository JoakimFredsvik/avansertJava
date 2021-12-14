package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.dao.entity.Question;
import no.kristiania.http.Request;
import no.kristiania.http.Response;

public class QuestionController extends HttpEntityController<Question> {

    public QuestionController(AbstractDao<Question> dao) {
        super(dao);
    }

    @Override
    protected Question createEntity(Request request) {
        Question question = new Question();

        question.setQuestionText(request.getQueryValue("text"));
        if (request.getQueryValue("questionnaire_id") != null) {
            question.setQuestionnaireId(Long.parseLong(request.getQueryValue("questionnaire_id")));
        }
        if (request.getQueryValue("id") != null) {
            question.setId(Long.parseLong(request.getQueryValue("id")));
        }
        return question;
    }

    @Override
    protected Response handleDelete(Request request) {
        Response response = super.handleDelete(request);
        response.setHeader("Location", "/view-all-questionnaires.html");

        return response;
    }
}