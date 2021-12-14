package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.dao.entity.Answer;
import no.kristiania.http.Request;
import no.kristiania.http.Response;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnswerController extends HttpEntityController<Answer> {
    public AnswerController(AbstractDao<Answer> dao) {
        super(dao);
    }

    @Override
    protected Response handlePost(Request request) {
        Response response = new Response(303, "see_other");
        if (request.getQueryValue("type") != null) {
            if (request.getQueryValue("type").equals("update")) {
                return handleUpdate(request);
            }
        }

        List<Answer> entites = createEntities(request);

        entites.forEach(entity -> {
            try {
                dao.save(entity);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        response.setHeader("Location", "/view-your-answers.html");
        return response;
    }

    protected List<Answer> createEntities(Request request) {
        HashMap<String, String> params = request.getQueryParams();
        ArrayList<Answer> answers = new ArrayList<>();

        for (String s : params.keySet()) {
            if (!s.equals("user_id")) {
                Answer a = new Answer();
                a.setQuestionId(Long.parseLong(s.substring(8)));
                a.setAnswerText(params.get(s));
                a.setUserId(Long.parseLong(params.get("user_id")));
                answers.add(a);
            }
        }

        return answers;
    }

    @Override
    protected Answer createEntity(Request request) {
        return null;
    }

    @Override
    protected Response handleGet(Request request) {

        Response response = new Response(200, "OK");
        if (request.getQueryValue("type").equals("all")) {
            try {
                List<Answer> result = dao.listAll();

                if (result == null) {
                    response = new Response(404, "not-found");
                    response.setHeader("Connection", "close");
                } else {
                    StringBuilder buffer = new StringBuilder();
                    result.forEach(item -> buffer.append(item.toString()));
                    response.setBody(buffer.toString());
                    response.setHeader("Connection", "close");
                    response.setHeader("Content-Length", String.valueOf(buffer.length()));
                    response.setHeader("Content-Type", "text/html; charset=utf-8");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(500, "server-error");
            }
        } else if (request.getQueryValue("type").equals("id")) {
            try {
                List<Answer> result = dao.listWithId(Long.parseLong(request.getQueryValue("id")));

                if (result == null) {
                    response = new Response(404, "not-found");
                    response.setHeader("Connection", "close");
                } else {
                    StringBuilder buffer = new StringBuilder();
                    result.forEach(item -> buffer.append(item.toString()));
                    response.setBody(buffer.toString());
                    response.setHeader("Connection", "close");
                    response.setHeader("Content-Length", String.valueOf(buffer.length()));
                    response.setHeader("Content-Type", "text/html; charset=utf-8");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(500, "server-error");
            }
        } else {
            response = super.handleGet(request);
        }

        return response;
    }
}
