package com.leaderboard.analytics.exception;

/**
 * Exception to indicate input file / stream is not readable.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public class InputUnreadableException extends Exception {

    public InputUnreadableException(String message) {
        super(message);
    }
}
