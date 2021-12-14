package no.kristiania.http;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpGetTest {
    @Test
    void shouldReturnStatus200() throws IOException {
        HttpGet get = new HttpGet("httpbin.org", 80, "/");
        Response response = (Response) get.response();

        assertEquals(200, response.getStatusCode());
    }

    @Test
    void shouldReturnHeader() throws IOException {
        HttpGet get = new HttpGet("httpbin.org", 80, "/");
        Response response = (Response) get.response();

        assertEquals("text/html; charset=utf-8", response.getHeader("Content-Type"));
    }

    @Test
    void shouldReturnResponseMessage() throws IOException {
        HttpGet get = new HttpGet("httpbin.org", 80, "/");
        Response response = (Response) get.response();

        assertEquals("OK", response.getStatusMessage());
    }

    @Test
    void shouldReadBody() throws IOException {
        HttpGet get = new HttpGet("httpbin.org", 80, "/");
        Response response = (Response) get.response();

        assertTrue(response.getBody().startsWith("<!DOCTYPE"));
    }
}