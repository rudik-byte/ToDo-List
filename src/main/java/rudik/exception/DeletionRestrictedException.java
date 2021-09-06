package rudik.exception;

public class DeletionRestrictedException extends APIException {

    /**
     * Construction the class with an exception message.
     *
     * @param message a given exception message from the code.
     */
    public DeletionRestrictedException(String message) {
        super(message);
    }
}