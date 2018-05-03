package BotPkg.webbPkg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class WebbUtils {
    protected WebbUtils() {
    }

    public static String queryString(Map<String, Object> values) {
        StringBuilder sbuf = new StringBuilder();
        String separator = "";
        Iterator var3 = values.entrySet().iterator();

        while(true) {
            while(var3.hasNext()) {
                Entry<String, Object> entry = (Entry)var3.next();
                Object entryValue = entry.getValue();
                if(entryValue instanceof Object[]) {
                    Object[] var10 = (Object[])((Object[])entryValue);
                    int var11 = var10.length;

                    for(int var8 = 0; var8 < var11; ++var8) {
                        Object value = var10[var8];
                        appendParam(sbuf, separator, (String)entry.getKey(), value);
                        separator = "&";
                    }
                } else if(entryValue instanceof Iterable) {
                    for(Iterator var6 = ((Iterable)entryValue).iterator(); var6.hasNext(); separator = "&") {
                        Object multiValue = var6.next();
                        appendParam(sbuf, separator, (String)entry.getKey(), multiValue);
                    }
                } else {
                    appendParam(sbuf, separator, (String)entry.getKey(), entryValue);
                    separator = "&";
                }
            }

            return sbuf.toString();
        }
    }

    private static void appendParam(StringBuilder sbuf, String separator, String entryKey, Object value) {
        String sValue = value == null?"":String.valueOf(value);
        sbuf.append(separator);
        sbuf.append(urlEncode(entryKey));
        sbuf.append('=');
        sbuf.append(urlEncode(sValue));
    }

    public static JSONObject toJsonObject(byte[] bytes) {
        try {
            String json = new String(bytes, "utf-8");
            return new JSONObject(json);
        } catch (UnsupportedEncodingException var3) {
            throw new WebbException(var3);
        } catch (JSONException var4) {
            throw new WebbException("payload is not a valid JSON object", var4);
        }
    }

    public static JSONArray toJsonArray(byte[] bytes) {
        try {
            String json = new String(bytes, "utf-8");
            return new JSONArray(json);
        } catch (UnsupportedEncodingException var3) {
            throw new WebbException(var3);
        } catch (JSONException var4) {
            throw new WebbException("payload is not a valid JSON array", var4);
        }
    }

    public static byte[] readBytes(InputStream is) throws IOException {
        if(is == null) {
            return null;
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            copyStream(is, baos);
            return baos.toByteArray();
        }
    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];

        int count;
        while((count = input.read(buffer)) != -1) {
            output.write(buffer, 0, count);
        }

    }

    public static DateFormat getRfc1123DateFormat() {
        DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        format.setLenient(false);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException var2) {
            return value;
        }
    }

    static void addRequestProperties(HttpURLConnection connection, Map<String, Object> map) {
        if(map != null && !map.isEmpty()) {
            Iterator var2 = map.entrySet().iterator();

            while(var2.hasNext()) {
                Entry<String, Object> entry = (Entry)var2.next();
                addRequestProperty(connection, (String)entry.getKey(), entry.getValue());
            }

        }
    }

    static void addRequestProperty(HttpURLConnection connection, String name, Object value) {
        if(name != null && name.length() != 0 && value != null) {
            String valueAsString;
            if(value instanceof Date) {
                valueAsString = getRfc1123DateFormat().format((Date)value);
            } else if(value instanceof Calendar) {
                valueAsString = getRfc1123DateFormat().format(((Calendar)value).getTime());
            } else {
                valueAsString = value.toString();
            }

            connection.addRequestProperty(name, valueAsString);
        } else {
            throw new IllegalArgumentException("name and value must not be empty");
        }
    }

    static void ensureRequestProperty(HttpURLConnection connection, String name, Object value) {
        if(!connection.getRequestProperties().containsKey(name)) {
            addRequestProperty(connection, name, value);
        }

    }

    static byte[] getPayloadAsBytesAndSetContentType(HttpURLConnection connection, Request request, boolean compress, int jsonIndentFactor) throws JSONException, UnsupportedEncodingException {
        byte[] requestBody = null;
        String bodyStr = null;
        if(request.params != null) {
            ensureRequestProperty(connection, "Content-Type", "application/x-www-form-urlencoded");
            bodyStr = queryString(request.params);
        } else {
            if(request.payload == null) {
                return null;
            }

            if(request.payload instanceof JSONObject) {
                ensureRequestProperty(connection, "Content-Type", "application/json");
                bodyStr = jsonIndentFactor >= 0?((JSONObject)request.payload).toString(jsonIndentFactor):request.payload.toString();
            } else if(request.payload instanceof JSONArray) {
                ensureRequestProperty(connection, "Content-Type", "application/json");
                bodyStr = jsonIndentFactor >= 0?((JSONArray)request.payload).toString(jsonIndentFactor):request.payload.toString();
            } else if(request.payload instanceof byte[]) {
                ensureRequestProperty(connection, "Content-Type", "application/octet-stream");
                requestBody = (byte[])((byte[])request.payload);
            } else {
                ensureRequestProperty(connection, "Content-Type", "text/plain");
                bodyStr = request.payload.toString();
            }
        }

        if(bodyStr != null) {
            requestBody = bodyStr.getBytes("utf-8");
        }

        if(requestBody == null) {
            throw new IllegalStateException();
        } else {
            if(compress && requestBody.length > 80) {
                byte[] compressedBody = gzip(requestBody);
                if(requestBody.length - compressedBody.length > 80) {
                    requestBody = compressedBody;
                    connection.setRequestProperty("Content-Encoding", "gzip");
                }
            }

            connection.setFixedLengthStreamingMode(requestBody.length);
            return requestBody;
        }
    }

    static void setContentTypeAndLengthForStreaming(HttpURLConnection connection, Request request, boolean compress) {
        long length;
        if(request.payload instanceof File) {
            length = compress?-1L:((File)request.payload).length();
        } else {
            if(!(request.payload instanceof InputStream)) {
                throw new IllegalStateException();
            }

            length = -1L;
        }

        if(length > 2147483647L) {
            length = -1L;
        }

        ensureRequestProperty(connection, "Content-Type", "application/octet-stream");
        if(length < 0L) {
            connection.setChunkedStreamingMode(-1);
            if(compress) {
                connection.setRequestProperty("Content-Encoding", "gzip");
            }
        } else {
            connection.setFixedLengthStreamingMode((int)length);
        }

    }

    static byte[] gzip(byte[] input) {
        GZIPOutputStream gzipOS = null;

        byte[] var3;
        try {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            gzipOS = new GZIPOutputStream(byteArrayOS);
            gzipOS.write(input);
            gzipOS.flush();
            gzipOS.close();
            gzipOS = null;
            var3 = byteArrayOS.toByteArray();
        } catch (Exception var12) {
            throw new WebbException(var12);
        } finally {
            if(gzipOS != null) {
                try {
                    gzipOS.close();
                } catch (Exception var11) {
                    ;
                }
            }

        }

        return var3;
    }

    static InputStream wrapStream(String contentEncoding, InputStream inputStream) throws IOException {
        if(contentEncoding != null && !"identity".equalsIgnoreCase(contentEncoding)) {
            if("gzip".equalsIgnoreCase(contentEncoding)) {
                return new GZIPInputStream(inputStream);
            } else if("deflate".equalsIgnoreCase(contentEncoding)) {
                return new InflaterInputStream(inputStream, new Inflater(false), 512);
            } else {
                throw new WebbException("unsupported content-encoding: " + contentEncoding);
            }
        } else {
            return inputStream;
        }
    }

    static <T> void parseResponseBody(Class<T> clazz, Response<T> response, InputStream responseBodyStream) throws UnsupportedEncodingException, IOException {
        if(responseBodyStream != null && clazz != Void.class) {
            if(clazz == InputStream.class) {
                response.setBody(responseBodyStream);
            } else {
                byte[] responseBody = readBytes(responseBodyStream);
                if(clazz == String.class) {
                    response.setBody(new String(responseBody, "utf-8"));
                } else if(clazz == Const.BYTE_ARRAY_CLASS) {
                    response.setBody(responseBody);
                } else if(clazz == JSONObject.class) {
                    response.setBody(toJsonObject(responseBody));
                } else if(clazz == JSONArray.class) {
                    response.setBody(toJsonArray(responseBody));
                }

            }
        }
    }

    static <T> void parseErrorResponse(Class<T> clazz, Response<T> response, InputStream responseBodyStream) throws UnsupportedEncodingException, IOException {
        if(responseBodyStream != null) {
            if(clazz == InputStream.class) {
                response.errorBody = responseBodyStream;
            } else {
                byte[] responseBody = readBytes(responseBodyStream);
                String contentType = response.connection.getContentType();
                if(contentType != null && !contentType.startsWith("application/octet-stream") && clazz != Const.BYTE_ARRAY_CLASS) {
                    if(contentType.startsWith("application/json") && clazz == JSONObject.class) {
                        try {
                            response.errorBody = toJsonObject(responseBody);
                            return;
                        } catch (Exception var7) {
                            ;
                        }
                    }

                    try {
                        response.errorBody = new String(responseBody, "utf-8");
                    } catch (Exception var6) {
                        response.errorBody = responseBody;
                    }
                } else {
                    response.errorBody = responseBody;
                }
            }
        }
    }
}
