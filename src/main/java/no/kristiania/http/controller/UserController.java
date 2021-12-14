package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.dao.entity.User;
import no.kristiania.http.Request;
import no.kristiania.http.Response;
import java.sql.SQLException;

public class UserController extends HttpEntityController<User> {
    public UserController(AbstractDao<User> dao) {
        super(dao);
    }

    @Override
    protected User createEntity(Request request) {
        User user = new User();
        user.setName(request.getQueryValue("name"));
        if (request.getQueryValue("id") != null) {
            user.setId(Long.parseLong(request.getQueryValue("id")));
        }
        return user;
    }

    @Override
    protected Response handlePost(Request request) {
        Response response = super.handlePost(request);

        try {
            User u = dao.retrieve(request.getQueryValue("name"));

            response.setHeader("Set-Cookie", "id=" + u.getId() + ";path=/");
            response.setHeader("Content-Type", "text/html; charset=utf-8");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return response;
    }
}