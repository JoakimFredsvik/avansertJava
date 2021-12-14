package no.kristiania.http;


import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/*
 * Used for making custom http-messages, and creating from from socket.
 * Request and Response classes inherits from HttpMessage, since the only distinction
 * between the two is the first line in the http-message.
 * */
abstract public class HttpMessage {
    protected String firstLine;
    protected HashMap<String, String> headers = new HashMap<>();
    protected String body = "";

    abstract void setFirstLine();

    public String getHeader(String headerKey) {
        return headers.get(headerKey);
    }

    public void setHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String headersToString() {
        StringBuilder buffer = new StringBuilder();
        for (String s : headers.keySet()) {
            buffer.append(s).append(": ").append(headers.get(s)).append("\r\n");
        }
        return buffer.toString();
    }

    @Override
    public String toString() {
        return firstLine + "\r\n" + headersToString() + "\r\n" + body;
    }

    protected void readHeaders(Socket socket) throws IOException {
        String headerLine;
        while (!(headerLine = readLine(socket)).isBlank()) {
            int colonPos = headerLine.indexOf(':');

            String headerField = headerLine.substring(0, colonPos);

            String headerValue = headerLine.substring(colonPos + 1).trim();
            headers.put(headerField, headerValue);
        }
    }

    protected void readBody(Socket socket) throws IOException {
        if (getHeader("Content-Length") == null) return;

        StringBuilder sb = new StringBuilder();
        int contentLength = Integer.parseInt(getHeader("Content-Length"));
        int c;
        for (int i = 0; i < contentLength; i++) {
            c = socket.getInputStream().read();
            sb.append((char) c);
        }

        this.body = decodeToUTF8(sb);
    }

    protected static String readLine(Socket socket) throws IOException {
        StringBuilder buffer = new StringBuilder();
        int c;
        while ((c = socket.getInputStream().read()) != '\r') {
            if (c == -1) return "";
            buffer.append((char) c);

        }
        int expectedNewline = socket.getInputStream().read();
        assert expectedNewline == '\n';

        return decodeToUTF8(buffer);
    }

    protected abstract void readFirstLine(Socket socket) throws IOException;

    public void send(Socket socket) throws IOException {
        socket.getOutputStream().write(this.toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String decodeToUTF8(StringBuilder buffer) {
        return URLDecoder.decode(buffer.toString(), StandardCharsets.UTF_8);
    }
}
