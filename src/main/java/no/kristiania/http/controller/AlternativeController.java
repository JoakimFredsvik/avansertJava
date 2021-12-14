package no.kristiania.http.controller;

import no.kristiania.dao.AbstractDao;
import no.kristiania.dao.entity.Alternative;
import no.kristiania.http.Request;

public class AlternativeController extends HttpEntityController<Alternative> {
    public AlternativeController(AbstractDao<Alternative> dao) {
        super(dao);
    }

    @Override
    protected Alternative createEntity(Request request) {
        Alternative entity = new Alternative();
        if (request.getQueryValue("id") != null) {
            entity.setId(Long.parseLong(request.getQueryValue("id")));
        }
        entity.setText(request.getQueryValue("text"));
        if (request.getQueryValue("question_id") != null) {
            entity.setQuestion_id(Long.parseLong(request.getQueryValue("question_id")));
        }
        return entity;
    }
}
