package net.study.resume.exception;

public class EmailNotConfirmedException extends RuntimeException {

    public EmailNotConfirmedException(String message) {
        super(message);
    }

    public EmailNotConfirmedException(Throwable cause) {
        super(cause);
    }

    public EmailNotConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }
}