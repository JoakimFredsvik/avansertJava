package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpServerTest {
    private final HttpServer server = new HttpServer(0);

    public HttpServerTest() throws IOException {
    }

    @Test
    void shouldReturn200() throws IOException {
        HttpGet get = new HttpGet("localhost", server.getPort(), "/");
        Response response = (Response) get.response();
        System.out.println();
        assertEquals(200, response.getStatusCode());
    }

    @Test
    void ServerResponseShouldReturnCorrectString() {
        Response response = new Response(200, "OK");
        response.setHeader("Connection", "close");
        String expected = "HTTP/1.1 200 OK\r\nConnection: close\r\n\r\n";

        assertEquals(expected, response.toString());
    }

    @Test
    void shouldRespondWithHtmlFile() throws IOException {
        HttpGet get = new HttpGet("localhost", server.getPort(), "/");
        Response response = (Response) get.response();
        String expected = "<!DOCTYPE html>";

        assertTrue(response.getBody().contains(expected));

    }

    @Test
    void shouldRespond404() throws IOException {
        HttpGet get = new HttpGet("localhost", server.getPort(), "/does_not_exist.html");
        Response response = (Response) get.response();

        assertEquals(404, response.getStatusCode());
    }
}
