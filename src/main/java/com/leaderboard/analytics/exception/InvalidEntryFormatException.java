package com.leaderboard.analytics.exception;

/**
 * Exception indicating bad / unexpected log entry.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public class InvalidEntryFormatException extends Exception {

    public InvalidEntryFormatException(String message) {
        super(message);
    }
}
