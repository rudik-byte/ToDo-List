package rudik.exception;

public abstract class APIException extends RuntimeException {

    /**
     * Construction the class with an exception message.
     *
     * @param message a given exception message from the inheritors of this class.
     */
    public APIException(String message) {
        super(message);
    }
}
