package com.leaderboard.analytics.util;

import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.exception.InvalidEntryFormatException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Generic file handing / processing.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public abstract class GenericFileParser {

    private InputStream fileInputStream;
    private Scanner scanner;

    public GenericFileParser() {}

    public void init(InputStream fileInputStream) throws InputUnreadableException {
        if (fileInputStream == null)
            throw new InputUnreadableException("Error reading input file! Make sure input is valid.");

        this.fileInputStream = fileInputStream;
        scanner = new Scanner(fileInputStream, StandardCharsets.UTF_8.name());
    }

    public String getNextLine() {
        return scanner.nextLine();
    }

    public boolean hasNextLine() {
        if (!scanner.hasNextLine()) {
            close();
            return false;
        }
        return true;
    }

    public void close() {
        scanner.close();
    }

    public abstract Metric parseLine(String line) throws InvalidEntryFormatException;

}
