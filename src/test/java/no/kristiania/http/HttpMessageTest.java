package no.kristiania.http;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpMessageTest {
    @Test
    void shouldReturnCorrectRequest() {
        HttpMessage request = new Request("GET", "/");
        String expected = "GET / HTTP/1.1\r\n";
        assertTrue(request.toString().contains(expected));
    }

    @Test
    void shouldReturnCorrectResponse() {
        HttpMessage response = new Response(200, "OK");
        String expected = "HTTP/1.1 200 OK";
        assertTrue(response.toString().contains(expected));
    }
}
