package no.kristiania.http;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Locale;

public class Request extends HttpMessage {
    private String method;
    private String target;
    private HashMap<String, String> queryParams = new HashMap<>();

    public Request(String method, String target) {
        this.method = method;
        this.target = target;
        setFirstLine();
    }

    public Request(Socket socket) {
        try {
            readFirstLine(socket);
            readHeaders(socket);
            readBody(socket);
            if (this.method.toLowerCase(Locale.ROOT).equals("post")) {
                parseQueryParameters(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    @Override
    void setFirstLine() {
        super.firstLine = method + " " + target + " HTTP/1.1";
    }

    void setFirstLine(String method, String target) {
        super.firstLine = method + " " + target + " HTTP/1.1";
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    protected void readFirstLine(Socket socket) throws IOException {
        String[] firstLine = readLine(socket).split(" ");

        setFirstLine(firstLine[0], firstLine[1]);
        this.method = firstLine[0];
        this.target = firstLine[1];

        if (!this.method.toLowerCase(Locale.ROOT).equals("post")) {
            if (target.contains("?")) {
                int queryPosition = firstLine[1].indexOf('?');
                parseQueryParameters(this.target.substring(queryPosition + 1));
                this.target = firstLine[1].substring(0, queryPosition);
            }
        }
    }

    private void parseQueryParameters(String unparsed) {
        String[] modified;
        if (unparsed.contains("&")) {
            modified = unparsed.split("&");
            for (String s : modified) {
                String[] keyValue = s.split("=");
                queryParams.put(keyValue[0], keyValue[1]);
            }
        } else {
            modified = unparsed.split("=");
            queryParams.put(modified[0], modified[1]);
        }
    }

    public String getQueryValue(String param) {
        return queryParams.get(param);
    }

    public HashMap<String, String> getQueryParams() {
        return queryParams;
    }
}
