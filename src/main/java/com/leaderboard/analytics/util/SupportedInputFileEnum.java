package com.leaderboard.analytics.util;

import com.leaderboard.analytics.exception.UnsupportedFileTypeException;

/**
 * Supported file formats.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public enum SupportedInputFileEnum {

    CSV(new CsvFileParser());

    private GenericFileParser fileParser;

    SupportedInputFileEnum(GenericFileParser fileParser) {
        this.fileParser = fileParser;
    }

    public GenericFileParser getFileParser() {
        return fileParser;
    }

    public static SupportedInputFileEnum fromString(String extension) throws UnsupportedFileTypeException {
        SupportedInputFileEnum type;
        try {
            type = SupportedInputFileEnum.valueOf(extension.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedFileTypeException("File "+extension+" not supported!");
        }
        return type;
    }
}
