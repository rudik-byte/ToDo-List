package rudik.exception;

public class NotFoundException extends APIException {
    /**
     * Construction the class with an exception message.
     *
     * @param message a given exception message from the code.
     */
    public NotFoundException(String message) {
        super(message);
    }
}
