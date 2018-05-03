package BotPkg.webbPkg;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
    private final BotPkg.webbPkg.Webb webb;
    final BotPkg.webbPkg.Request.Method method;
    final String uri;
    Map<String, Object> params;
    boolean multipleValues;
    Map<String, Object> headers;
    Object payload;
    boolean streamPayload;
    boolean useCaches;
    Integer connectTimeout;
    Integer readTimeout;
    Long ifModifiedSince;
    Boolean followRedirects;
    boolean ensureSuccess;
    boolean compress;
    int retryCount;
    boolean waitExponential;

    Request(BotPkg.webbPkg.Webb webb, BotPkg.webbPkg.Request.Method method, String uri) {
        this.webb = webb;
        this.method = method;
        this.uri = uri;
        this.followRedirects = webb.followRedirects;
    }

    public BotPkg.webbPkg.Request multipleValues() {
        this.multipleValues = true;
        return this;
    }

    public BotPkg.webbPkg.Request param(String name, Object value) {
        if(this.params == null) {
            this.params = new LinkedHashMap();
        }

        if(this.multipleValues) {
            Object currentValue = this.params.get(name);
            if(currentValue != null) {
                if(currentValue instanceof Collection) {
                    Collection<Object> values = (Collection)currentValue;
                    values.add(value);
                } else {
                    Collection<Object> values = new ArrayList();
                    values.add(currentValue);
                    values.add(value);
                    this.params.put(name, values);
                }

                return this;
            }
        }

        this.params.put(name, value);
        return this;
    }

    public BotPkg.webbPkg.Request param(String name, Iterable<Object> values) {
        if(this.params == null) {
            this.params = new LinkedHashMap();
        }

        this.params.put(name, values);
        return this;
    }

    public BotPkg.webbPkg.Request params(Map<String, Object> valueByName) {
        if(this.params == null) {
            this.params = new LinkedHashMap();
        }

        this.params.putAll(valueByName);
        return this;
    }

    public String getUri() {
        return this.uri;
    }

    public BotPkg.webbPkg.Request header(String name, Object value) {
        if(this.headers == null) {
            this.headers = new LinkedHashMap();
        }

        this.headers.put(name, value);
        return this;
    }

    public BotPkg.webbPkg.Request body(Object body) {
        if(this.method != BotPkg.webbPkg.Request.Method.GET && this.method != BotPkg.webbPkg.Request.Method.DELETE) {
            this.payload = body;
            this.streamPayload = body instanceof File || body instanceof InputStream;
            return this;
        } else {
            throw new IllegalStateException("body not allowed for request method " + this.method);
        }
    }

    public BotPkg.webbPkg.Request compress() {
        this.compress = true;
        return this;
    }

    public BotPkg.webbPkg.Request useCaches(boolean useCaches) {
        this.useCaches = useCaches;
        return this;
    }

    public BotPkg.webbPkg.Request ifModifiedSince(long ifModifiedSince) {
        this.ifModifiedSince = Long.valueOf(ifModifiedSince);
        return this;
    }

    public BotPkg.webbPkg.Request connectTimeout(int connectTimeout) {
        this.connectTimeout = Integer.valueOf(connectTimeout);
        return this;
    }

    public BotPkg.webbPkg.Request readTimeout(int readTimeout) {
        this.readTimeout = Integer.valueOf(readTimeout);
        return this;
    }

    public BotPkg.webbPkg.Request followRedirects(boolean auto) {
        this.followRedirects = Boolean.valueOf(auto);
        return this;
    }

    public BotPkg.webbPkg.Request ensureSuccess() {
        this.ensureSuccess = true;
        return this;
    }

    public BotPkg.webbPkg.Request retry(int retryCount, boolean waitExponential) {
        if(retryCount < 0) {
            retryCount = 0;
        }

        if(retryCount > 10) {
            retryCount = 10;
        }

        if(retryCount > 3 && !waitExponential) {
            throw new IllegalArgumentException("retries > 3 only valid with wait");
        } else {
            this.retryCount = retryCount;
            this.waitExponential = waitExponential;
            return this;
        }
    }

    public BotPkg.webbPkg.Response<String> asString() {
        return this.webb.execute(this, String.class);
    }

    public BotPkg.webbPkg.Response<JSONObject> asJsonObject() {
        return this.webb.execute(this, JSONObject.class);
    }

    public BotPkg.webbPkg.Response<JSONArray> asJsonArray() {
        return this.webb.execute(this, JSONArray.class);
    }

    public BotPkg.webbPkg.Response<byte[]> asBytes() {
        return this.webb.execute(this, BotPkg.webbPkg.Const.BYTE_ARRAY_CLASS);
    }

    public BotPkg.webbPkg.Response<InputStream> asStream() {
        return this.webb.execute(this, InputStream.class);
    }

    public Response<Void> asVoid() {
        return this.webb.execute(this, Void.class);
    }

    public static enum Method {
        GET,
        POST,
        PUT,
        DELETE;

        private Method() {
        }
    }
}
