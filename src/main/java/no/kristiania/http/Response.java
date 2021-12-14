package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;


public class Response extends HttpMessage {
    private int statusCode;
    private String statusMessage;

    public Response(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        setFirstLine();
    }

    public Response(Socket socket) {
        try {
            readFirstLine(socket);
            readHeaders(socket);
            readBody(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void readFirstLine(Socket socket) throws IOException {
        String[] firstLine = readLine(socket).split(" ");
        setFirstLine(firstLine[0], firstLine[1]);
        this.statusCode = Integer.parseInt(firstLine[1]);
        this.statusMessage = firstLine[2];

    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    @Override
    void setFirstLine() {
        setFirstLine(String.valueOf(this.statusCode), this.statusMessage);
    }

    void setFirstLine(String statusCode, String statusMessage) {
        super.firstLine = "HTTP/1.1 " + statusCode + " " + statusMessage;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}