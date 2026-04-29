package com.nitin.enrollhub.support;

/**
 * Raised when an enrollee look-up yields no result.
 *
 * @author Nitin
 */
public class NoSuchEnrolleeException extends RuntimeException {

    private final long enrolleeId;

    public NoSuchEnrolleeException(long enrolleeId) {
        super("Enrollee with id " + enrolleeId + " does not exist");
        this.enrolleeId = enrolleeId;
    }

    public long getEnrolleeId() {
        return enrolleeId;
    }
}
