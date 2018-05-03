package BotPkg.webbPkg;

import javax.net.ssl.SSLException;
import java.net.SocketTimeoutException;

public class RetryManager {
    protected static final int[] BACKOFF = new int[]{1, 2, 4, 7, 12, 20, 30, 60, 120};
    static final RetryManager DEFAULT = new RetryManager();

    public RetryManager() {
    }

    public boolean isRetryUseful(Response response) {
        int statusCode = response.getStatusCode();
        return statusCode == 503 || statusCode == 504 || statusCode >= 520;
    }

    public boolean isRecoverable(WebbException webbException) {
        Throwable cause = webbException.getCause();
        if(cause == null) {
            return false;
        } else {
            if(cause instanceof SSLException) {
                SSLException sslException = (SSLException)cause;
                if(sslException.toString().toLowerCase().contains("connection reset by peer")) {
                    return true;
                }
            }

            return cause instanceof SocketTimeoutException;
        }
    }

    public void wait(int retry) {
        long sleepMillis = (long)BACKOFF[Math.min(retry, BACKOFF.length - 1)] * 1000L;

        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException var5) {
            Thread.currentThread().interrupt();
            throw new WebbException(var5);
        }
    }
}
