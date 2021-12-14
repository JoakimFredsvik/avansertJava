package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.http.Request;
import no.kristiania.http.Response;

import java.sql.SQLException;

/*
 * Abstract class to avoid duplication of code with get/post handling.
 * */
public abstract class HttpEntityController<T> implements HttpController {
    AbstractDao<T> dao;

    public HttpEntityController(AbstractDao<T> dao) {
        this.dao = dao;
    }

    public Response handle(Request request) {
        Response response;
        switch (request.getMethod()) {
            case "GET":
                response = handleGet(request);
                break;
            case "POST":
                response = handlePost(request);
                break;
            default:
                return new Response(500, "server-error");
        }
        return response;
    }

    protected Response handleGet(Request request) {
        Response response = new Response(200, "OK");
        try {
            T result = dao.retrieve(Long.parseLong(request.getQueryValue("id")));
            if (result == null) {
                response = new Response(404, "not-found");
                response.setHeader("Connection", "close");
            } else {
                response.setHeader("Connection", "close");
                response.setHeader("Content-Length", String.valueOf(result.toString().length()));
                response.setHeader("Content-Type", "text/html; charset=utf-8");
                response.setBody(result.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(500, "server-error");
        }
        return response;
    }

    protected Response handlePost(Request request) {
        if (request.getQueryValue("type") != null) {
            if (request.getQueryValue("type").equals("update")) {
                return handleUpdate(request);
            } else if (request.getQueryValue("type").equals("delete")) {
                return handleDelete(request);
            }
        }

        T entity = createEntity(request);

        try {
            dao.save(entity);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(500, "SERVER_ERROR");
        }
        Response response = new Response(303, "OK");
        response.setHeader("Connection", "close");
        response.setHeader("Location", "/view-all-questionnaires.html");
        return response;
    }


    protected Response handleUpdate(Request request) {
        T entity = createEntity(request);
        try {
            dao.update(entity);

        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(500, "SERVER_ERROR");
        }
        Response response = new Response(303, "see_other");
        response.setHeader("Location", "/view-all-questionnaires.html");
        return response;
    }

    protected Response handleDelete(Request request) {
        Response response = new Response(303, "NO_CONTENT");
        try {
            dao.delete(Long.parseLong(request.getQueryValue("id")));

            response.setHeader("Connection", "close");
        } catch (SQLException e) {
            e.printStackTrace();
            return new Response(404, "NOT_FOUND");
        }
        return response;
    }

    protected abstract T createEntity(Request request);
}