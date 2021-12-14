package no.kristiania.http.controller;

import no.kristiania.http.Request;
import no.kristiania.http.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileController implements HttpController {
    private final Socket clientSocket;

    public FileController(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public Response handle(Request request) {
        try {
            return provideFile(request.getTarget());
        } catch (IOException e) {
            e.printStackTrace();
            return new Response(500, "SERVER_ERROR");
        }
    }

    private Response provideFile(String target) throws IOException {
        if (target.equals("/")) {
            return provideFile("/index.html");
        }
        InputStream fileResource = getClass().getResourceAsStream(target);
        target = target.substring(1);
        Response response = new Response(200, "OK");

        if (fileResource != null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            fileResource.transferTo(buffer);
            String responseText = buffer.toString();

            String contentType;
            if (target.endsWith(".html")) {
                contentType = "text/html";
                response.setHeader("Content-Type", contentType);
                response.setHeader("Content-Length", String.valueOf(responseText.length()));
                response.setBody(responseText);
            } else if (target.endsWith(".css")) {
                contentType = "text/css";
                response.setHeader("Content-Type", contentType);
                response.setHeader("Content-Length", String.valueOf(responseText.length()));
                response.setBody(responseText);
            } else if (target.equals("favicon.ico")) {
                byte[] content = buffer.toByteArray();

                response = new Response(200, "OK");
                response.setHeader("Content-Length", String.valueOf(content.length));
                response.setHeader("Content-Type", "img/ico");

                response.send(clientSocket);
                clientSocket.getOutputStream().write(content);
                return null;
            } else {
                response = makeNotFoundResponse();
            }
        } else {
            response = makeNotFoundResponse();
        }
        return response;
    }

    private Response makeNotFoundResponse() throws IOException {
        Response response;
        InputStream fileResource = getClass().getResourceAsStream("/file-not-found.html");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        fileResource.transferTo(buffer);
        String content = buffer.toString();
        response = new Response(404, "NOT FOUND");

        response.setHeader("Content-Length", String.valueOf(content.length()));
        response.setHeader("Content-Type", "text/html; charset=utf-8");
        response.setBody(content);
        return response;
    }
}