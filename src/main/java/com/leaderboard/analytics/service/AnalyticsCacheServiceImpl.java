package com.leaderboard.analytics.service;

import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.util.DateUtil;
import com.leaderboard.analytics.util.GenericFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service that caches aggregated metrics data.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@Service("analyticsServiceCache")
@ConfigurationProperties(prefix = "leaderboard")
public class AnalyticsCacheServiceImpl implements AnalyticsService {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsCacheServiceImpl.class);

    private static final ConcurrentHashMap<String, TreeSet> cache = new ConcurrentHashMap<>();

    private static final Comparator<Metric> SORT_BY_DOWNLOADS_REVERSE = Comparator.comparingDouble(Metric::getValue).reversed();

    @Autowired
    private AnalyticsService analyticsService;

    @Override
    public Map<String, List<Metric>> parseAndLoadFromFile(InputStream fileAsStream, GenericFileParser fileParser) throws InputUnreadableException {
        Map<String, List<Metric>> aggregatedMetrics = analyticsService.parseAndLoadFromFile(fileAsStream, fileParser);
        // write data to the cache
        for (Map.Entry<String, List<Metric>> entry : aggregatedMetrics.entrySet()) {
            TreeSet<Metric> bucket = new TreeSet(SORT_BY_DOWNLOADS_REVERSE);
            bucket.addAll(entry.getValue());
            cache.put(entry.getKey(), bucket);
        }

        return aggregatedMetrics;
    }

    @Override
    public Collection<Metric> fetchLeaderBoardByDate(String date) {
        return cache.get(date);
    }

    @Override
    public Collection<Metric> fetchLeaderBoardBetweenRange(String fromDate, String toDate) {
        List<String> dates = DateUtil.getDatesInRange(fromDate, toDate);
        List<Metric> result = new ArrayList<>();
        for (String date : dates) {
            TreeSet<Metric> bucket = cache.get(date);
            if (bucket != null && !bucket.isEmpty()) result.add(bucket.first());
        }
        return result;
    }
}
