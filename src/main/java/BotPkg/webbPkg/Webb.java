package BotPkg.webbPkg;

import org.json.JSONArray;
import org.json.JSONObject;
import BotPkg.webbPkg.Request.Method;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class Webb {
    public static final String DEFAULT_USER_AGENT = "BotPkg.webbPkg.Webb/1.0";
    public static final String APP_FORM = "application/x-www-form-urlencoded";
    public static final String APP_JSON = "application/json";
    public static final String APP_BINARY = "application/octet-stream";
    public static final String TEXT_PLAIN = "text/plain";
    public static final String HDR_CONTENT_TYPE = "Content-Type";
    public static final String HDR_CONTENT_ENCODING = "Content-Encoding";
    public static final String HDR_ACCEPT = "Accept";
    public static final String HDR_ACCEPT_ENCODING = "Accept-Encoding";
    public static final String HDR_USER_AGENT = "User-Agent";
    public static final String HDR_AUTHORIZATION = "Authorization";
    static final Map<String, Object> globalHeaders = new LinkedHashMap();
    static String globalBaseUri;
    static Integer connectTimeout = Integer.valueOf(10000);
    static Integer readTimeout = Integer.valueOf(180000);
    static int jsonIndentFactor = -1;
    Boolean followRedirects;
    String baseUri;
    Map<String, Object> defaultHeaders;
    SSLSocketFactory sslSocketFactory;
    HostnameVerifier hostnameVerifier;
    RetryManager retryManager;
    Proxy proxy;
    protected Webb() {
    }

    public static Webb create() {
        return new Webb();
    }

    public static void setGlobalHeader(String name, Object value) {
        if(value != null) {
            globalHeaders.put(name, value);
        } else {
            globalHeaders.remove(name);
        }

    }

    public static void setGlobalBaseUri(String globalBaseUri) {
        globalBaseUri = globalBaseUri;
    }

    public static void setJsonIndentFactor(int indentFactor) {
        jsonIndentFactor = indentFactor;
    }

    public static void setConnectTimeout(int globalConnectTimeout) {
        connectTimeout = globalConnectTimeout > 0?Integer.valueOf(globalConnectTimeout):null;
    }

    public static void setReadTimeout(int globalReadTimeout) {
        readTimeout = globalReadTimeout > 0?Integer.valueOf(globalReadTimeout):null;
    }

    public void setFollowRedirects(boolean auto) {
        this.followRedirects = Boolean.valueOf(auto);
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public String getBaseUri() {
        return this.baseUri;
    }

    public void setDefaultHeader(String name, Object value) {
        if(this.defaultHeaders == null) {
            this.defaultHeaders = new HashMap();
        }

        if(value == null) {
            this.defaultHeaders.remove(name);
        } else {
            this.defaultHeaders.put(name, value);
        }

    }

    public void setRetryManager(RetryManager retryManager) {
        this.retryManager = retryManager;
    }

    public Request get(String pathOrUri) {
        return new Request(this, Method.GET, this.buildPath(pathOrUri));
    }

    public Request post(String pathOrUri) {
        return new Request(this, Method.POST, this.buildPath(pathOrUri));
    }

    public Request put(String pathOrUri) {
        return new Request(this, Method.PUT, this.buildPath(pathOrUri));
    }

    public Request delete(String pathOrUri) {
        return new Request(this, Method.DELETE, this.buildPath(pathOrUri));
    }

    private String buildPath(String pathOrUri) {
        if(pathOrUri == null) {
            throw new IllegalArgumentException("pathOrUri must not be null");
        } else if(!pathOrUri.startsWith("http://") && !pathOrUri.startsWith("https://")) {
            String myBaseUri = this.baseUri != null?this.baseUri:globalBaseUri;
            return myBaseUri == null?pathOrUri:myBaseUri + pathOrUri;
        } else {
            return pathOrUri;
        }
    }

    <T> Response<T> execute(Request request, Class<T> clazz) {
        Response<T> response = null;
        if(request.retryCount == 0) {
            response = this._execute(request, clazz);
        } else {
            if(this.retryManager == null) {
                this.retryManager = RetryManager.DEFAULT;
            }

            for(int tries = 0; tries <= request.retryCount; ++tries) {
                try {
                    response = this._execute(request, clazz);
                    if(tries >= request.retryCount || !this.retryManager.isRetryUseful(response)) {
                        break;
                    }
                } catch (WebbException var6) {
                    if(tries >= request.retryCount || !this.retryManager.isRecoverable(var6)) {
                        throw var6;
                    }
                }

                if(request.waitExponential) {
                    this.retryManager.wait(tries);
                }
            }
        }

        if(response == null) {
            throw new IllegalStateException();
        } else {
            if(request.ensureSuccess) {
                response.ensureSuccess();
            }

            return response;
        }
    }

    public void setProxy(Proxy proxy) {
                this.proxy = proxy;
            }

    private <T> Response<T> _execute(Request request, Class<T> clazz) {
        Response<T> response = new Response(request);
        InputStream is = null;
        boolean closeStream = true;
        HttpURLConnection connection = null;

        Response var26;
        try {
            String uri = request.uri;
            if(request.method == Method.GET && !uri.contains("?") && request.params != null && !request.params.isEmpty()) {
                uri = uri + "?" + WebbUtils.queryString(request.params);
            }

            URL apiUrl = new URL(uri);

            //connection = (HttpURLConnection)apiUrl.openConnection();

            if (proxy != null) {
                connection = (HttpURLConnection) apiUrl.openConnection(proxy);
            } else {
                connection = (HttpURLConnection) apiUrl.openConnection();
            }

            this.prepareSslConnection(connection);
            connection.setRequestMethod(request.method.name());
            if(request.followRedirects != null) {
                connection.setInstanceFollowRedirects(request.followRedirects.booleanValue());
            }

            connection.setUseCaches(request.useCaches);
            this.setTimeouts(request, connection);
            if(request.ifModifiedSince != null) {
                connection.setIfModifiedSince(request.ifModifiedSince.longValue());
            }

            WebbUtils.addRequestProperties(connection, this.mergeHeaders(request.headers));
            if(clazz == JSONObject.class || clazz == JSONArray.class) {
                WebbUtils.ensureRequestProperty(connection, "Accept", "application/json");
            }

            if(request.method != Method.GET && request.method != Method.DELETE) {
                if(request.streamPayload) {
                    WebbUtils.setContentTypeAndLengthForStreaming(connection, request, request.compress);
                    connection.setDoOutput(true);
                    this.streamBody(connection, request.payload, request.compress);
                } else {
                    byte[] requestBody = WebbUtils.getPayloadAsBytesAndSetContentType(connection, request, request.compress, jsonIndentFactor);
                    if(requestBody != null) {
                        connection.setDoOutput(true);
                        this.writeBody(connection, requestBody);
                    }
                }
            } else {
                connection.connect();
            }

            response.connection = connection;
            response.statusCode = connection.getResponseCode();
            response.responseMessage = connection.getResponseMessage();
            is = response.isSuccess()?connection.getInputStream():connection.getErrorStream();
            is = WebbUtils.wrapStream(connection.getContentEncoding(), is);
            if(clazz == InputStream.class) {
                is = new Webb.AutoDisconnectInputStream(connection, (InputStream)is);
            }

            if(response.isSuccess()) {
                WebbUtils.parseResponseBody(clazz, response, (InputStream)is);
            } else {
                WebbUtils.parseErrorResponse(clazz, response, (InputStream)is);
            }

            if(clazz == InputStream.class) {
                closeStream = false;
            }

            var26 = response;
        } catch (WebbException var22) {
            throw var22;
        } catch (Exception var23) {
            throw new WebbException(var23);
        } finally {
            if(closeStream) {
                if(is != null) {
                    try {
                        ((InputStream)is).close();
                    } catch (Exception var21) {
                        ;
                    }
                }

                if(connection != null) {
                    try {
                        connection.disconnect();
                    } catch (Exception var20) {
                        ;
                    }
                }
            }

        }

        return var26;
    }

    private void setTimeouts(Request request, HttpURLConnection connection) {
        if(request.connectTimeout != null || connectTimeout != null) {
            connection.setConnectTimeout((request.connectTimeout != null?request.connectTimeout:connectTimeout).intValue());
        }

        if(request.readTimeout != null || readTimeout != null) {
            connection.setReadTimeout((request.readTimeout != null?request.readTimeout:readTimeout).intValue());
        }

    }

    private void writeBody(HttpURLConnection connection, byte[] body) throws IOException {
        OutputStream os = null;

        try {
            os = connection.getOutputStream();
            os.write(body);
            os.flush();
        } finally {
            if(os != null) {
                try {
                    os.close();
                } catch (Exception var10) {
                    ;
                }
            }

        }

    }

    private void streamBody(HttpURLConnection connection, Object body, boolean compress) throws IOException {
        Object is;
        boolean closeStream;
        if(body instanceof File) {
            is = new FileInputStream((File)body);
            closeStream = true;
        } else {
            is = (InputStream)body;
            closeStream = false;
        }

        Object os = null;

        try {
            os = connection.getOutputStream();
            if(compress) {
                os = new GZIPOutputStream((OutputStream)os);
            }

            WebbUtils.copyStream((InputStream)is, (OutputStream)os);
            ((OutputStream)os).flush();
        } finally {
            if(os != null) {
                try {
                    ((OutputStream)os).close();
                } catch (Exception var16) {
                    ;
                }
            }

            if(is != null && closeStream) {
                try {
                    ((InputStream)is).close();
                } catch (Exception var15) {
                    ;
                }
            }

        }

    }

    private void prepareSslConnection(HttpURLConnection connection) {
        if((this.hostnameVerifier != null || this.sslSocketFactory != null) && connection instanceof HttpsURLConnection) {
            HttpsURLConnection sslConnection = (HttpsURLConnection)connection;

            if(this.hostnameVerifier != null) {
                sslConnection.setHostnameVerifier(this.hostnameVerifier);
            }

            if(this.sslSocketFactory != null) {
                sslConnection.setSSLSocketFactory(this.sslSocketFactory);
            }
        }

    }

    Map<String, Object> mergeHeaders(Map<String, Object> requestHeaders) {
        Map<String, Object> headers = null;
        if(!globalHeaders.isEmpty()) {
            headers = new LinkedHashMap();
            ((Map)headers).putAll(globalHeaders);
        }

        if(this.defaultHeaders != null) {
            if(headers == null) {
                headers = new LinkedHashMap();
            }

            ((Map)headers).putAll(this.defaultHeaders);
        }

        if(requestHeaders != null) {
            if(headers == null) {
                headers = requestHeaders;
            } else {
                ((Map)headers).putAll(requestHeaders);
            }
        }

        return (Map)headers;
    }

    private static class AutoDisconnectInputStream extends FilterInputStream {
        private final HttpURLConnection connection;

        protected AutoDisconnectInputStream(HttpURLConnection connection, InputStream in) {
            super(in);
            this.connection = connection;
        }

        public void close() throws IOException {
            try {
                super.close();
            } finally {
                this.connection.disconnect();
            }

        }
    }
}

