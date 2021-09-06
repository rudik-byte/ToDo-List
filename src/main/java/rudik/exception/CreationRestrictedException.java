package rudik.exception;

public class CreationRestrictedException extends APIException {
    /**
     * Construction the class with an exception message.
     *
     * @param message a given exception message from the code.
     */
    public CreationRestrictedException(String message) {
        super(message);
    }
}
