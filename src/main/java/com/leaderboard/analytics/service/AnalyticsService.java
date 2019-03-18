package com.leaderboard.analytics.service;

import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.util.GenericFileParser;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Operations supported by Analytics Service.
 *
 * @author Srinivasa Prasad Sunnapu
 */
public interface AnalyticsService {

    Map<String, List<Metric>> parseAndLoadFromFile(InputStream fileAsStream, GenericFileParser fileParser) throws InputUnreadableException;

    Collection<Metric> fetchLeaderBoardByDate(String date);

    Collection<Metric> fetchLeaderBoardBetweenRange(String fromDate, String toDate);

}
