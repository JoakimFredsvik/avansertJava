package no.kristiania.http.controller;

import no.kristiania.http.Request;
import no.kristiania.http.Response;

public interface HttpController {
    Response handle(Request request);
}
