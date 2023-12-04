package mg.exception;

public class ScaffoldingException extends RuntimeException {
  public ScaffoldingException(String message) {
    super(message);
    // remove stack trace
    setStackTrace(new StackTraceElement[0]);
  }
}
