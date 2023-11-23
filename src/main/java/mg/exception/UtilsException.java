package mg.exception;

public class UtilsException extends RuntimeException {
    public UtilsException(String message) {
        super(message);
        // remove stack trace
        setStackTrace(new StackTraceElement[0]);
    }
}
