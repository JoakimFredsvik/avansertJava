package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;

public class HttpPost {
    private Response response;

    public HttpPost(String host, int port, String requestTarget, String body) throws IOException {
        Socket socket = new Socket(host, port);
        HttpMessage request = new Request("POST", requestTarget);
        request.setHeader("Connection", "close");
        request.setHeader("Host", host);
        request.setHeader("Content-Type", "text/html; charset=utf-8");
        request.setHeader("Content-Length", String.valueOf(body.length()));
        request.setBody(body);
        request.send(socket);
        this.response = new Response(socket);
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}