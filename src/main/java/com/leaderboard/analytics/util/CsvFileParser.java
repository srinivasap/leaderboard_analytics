package com.leaderboard.analytics.util;

import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.exception.InvalidEntryFormatException;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/**
 * Logic and rules to process files in csv format
 *
 * @author Srinivasa Prasad Sunnapu
 */
public class CsvFileParser extends GenericFileParser {

    private static final String DELIMITER = ",";
    private static final Set<String> VALID_KPI = new HashSet<>();

    public CsvFileParser() {
        VALID_KPI.add("downloads");
    }

    public CsvFileParser(InputStream inputStream) throws InputUnreadableException {
        init(inputStream);
    }

    @Override
    public Metric parseLine(String line) throws InvalidEntryFormatException {
        if (line == null || line.isEmpty())
            throw new InvalidEntryFormatException("Invalid line! Expected non-null/empty line!!");

        String[] tokens = line.split(DELIMITER);
        if (tokens == null || tokens.length < 6)
            throw new InvalidEntryFormatException("Invalid line! Expected at least 6 fields but found "+tokens.length+" - <"+line+">");

        // if kpi is not one of supported ones, just return null
        if (!VALID_KPI.contains(tokens[4].toLowerCase())) return null;

        return new Metric(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], Double.parseDouble(tokens[5]));
    }
}
