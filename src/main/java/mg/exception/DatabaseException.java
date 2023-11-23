package mg.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message) {
        super(message);
        // remove stack trace
        setStackTrace(new StackTraceElement[0]);
    }
}
