package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.dao.entity.Questionnaire;
import no.kristiania.http.Request;
import no.kristiania.http.Response;

import java.sql.SQLException;
import java.util.List;

public class QuestionnaireController extends HttpEntityController<Questionnaire> {

    public QuestionnaireController(AbstractDao<Questionnaire> dao) {
        super(dao);
    }

    @Override
    protected Response handleGet(Request request) {
        Response response = new Response(200, "OK");
        if (request.getQueryValue("type").equals("all")) {
            try {
                List<Questionnaire> result = dao.listAll();

                if (result == null) {
                    response = new Response(404, "not-found");
                    response.setHeader("Connection", "close");
                } else {
                    if (request.getQueryValue("short") != null) {
                        if (request.getQueryValue("short").equals("true")) {
                            result.forEach(entity -> {
                                entity.setShort(true);
                            });
                        }
                    }
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
            Questionnaire entityFound;
            try {
                entityFound = dao.retrieve(Long.parseLong(request.getQueryValue("id")));
            } catch (SQLException e) {
                e.printStackTrace();
                return new Response(500, "server-error");
            }
            if (request.getQueryValue("editable").equals("true")) entityFound.setEditable(true);


            response.setBody(entityFound.toString());
            response.setHeader("Connection", "close");
            response.setHeader("Content-Length", String.valueOf(entityFound.toString().length()));
            response.setHeader("Content-Type", "text/html; charset=utf-8");
        } else {
            response = super.handleGet(request);
        }

        return response;
    }

    @Override
    protected Questionnaire createEntity(Request request) {
        Questionnaire q = new Questionnaire();
        q.setName(request.getQueryValue("name"));
        if (request.getQueryValue("id") != null) {
            q.setId(Long.parseLong(request.getQueryValue("id")));
        }
        return q;
    }

    @Override
    protected Response handleDelete(Request request) {
        Response response = super.handleDelete(request);
        response.setHeader("Location", "/view-all-questionnaires.html");

        return response;
    }
}
