package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;

public class HttpGet {
    private final HttpMessage response;

    public HttpGet(String host, int port, String requestTarget) throws IOException {
        Socket socket = new Socket(host, port);
        HttpMessage request = new Request("GET", requestTarget);
        request.setHeader("Connection", "close");
        request.setHeader("Host", host);
        request.send(socket);
        this.response = new Response(socket);
    }

    public HttpMessage response() {
        return this.response;
    }
}
