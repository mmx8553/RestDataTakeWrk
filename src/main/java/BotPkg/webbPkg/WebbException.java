package BotPkg.webbPkg;

public class WebbException extends RuntimeException {
    private Response response;

    public WebbException(String message) {
        super(message);
    }

    public WebbException(String message, Response response) {
        super(message);
        this.response = response;
    }

    public WebbException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebbException(Throwable cause) {
        super(cause);
    }

    public Response getResponse() {
        return this.response;
    }
}
