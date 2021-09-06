package rudik.exception;

public class InvalidTokenException extends APIException{

    /**
     * Construction the class with an exception message.
     *
     * @param message a given exception message from the code.
     */
    public InvalidTokenException(String message) {
        super(message);
    }
}
