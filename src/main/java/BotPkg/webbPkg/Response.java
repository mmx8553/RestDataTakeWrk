package BotPkg.webbPkg;

import java.net.HttpURLConnection;

public class Response<T> {
    final Request request;
    int statusCode;
    String responseMessage;
    T body;
    Object errorBody;
    HttpURLConnection connection;

    Response(Request request) {
        this.request = request;
    }

    void setBody(Object body) {
        this.body = (T) body;
    }

    public Request getRequest() {
        return this.request;
    }

    public T getBody() {
        return this.body;
    }

    public Object getErrorBody() {
        return this.errorBody;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusLine() {
        return this.connection.getHeaderField((String)null);
    }

    public boolean isSuccess() {
        return this.statusCode / 100 == 2;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public String getContentType() {
        return this.connection.getContentType();
    }

    public long getDate() {
        return this.connection.getDate();
    }

    public long getExpiration() {
        return this.connection.getExpiration();
    }

    public long getLastModified() {
        return this.connection.getLastModified();
    }

    public String getHeaderField(String name) {
        return this.connection.getHeaderField(name);
    }

    public long getHeaderFieldDate(String field, long defaultValue) {
        return this.connection.getHeaderFieldDate(field, defaultValue);
    }

    public int getHeaderFieldInt(String field, int defaultValue) {
        return this.connection.getHeaderFieldInt(field, defaultValue);
    }

    public HttpURLConnection getConnection() {
        return this.connection;
    }

    public void ensureSuccess() {
        if(!this.isSuccess()) {
            throw new WebbException("Request failed: " + this.statusCode + " " + this.responseMessage, this);
        }
    }
}
