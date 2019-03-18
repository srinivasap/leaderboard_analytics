package com.leaderboard.analytics.service;

import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.exception.InvalidEntryFormatException;
import com.leaderboard.analytics.persistence.MetricRepository;
import com.leaderboard.analytics.util.GenericFileParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for parsing and persisting aggregated metrics.
 *
 * @author Srinivasa Prasad Sunnapu
 */
@Service("analyticsService")
@ConfigurationProperties(prefix = "leaderboard")
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Autowired
    private MetricRepository metricRepository;

    @Override
    public Map<String, List<Metric>> parseAndLoadFromFile(InputStream fileAsStream, GenericFileParser fileParser) throws InputUnreadableException {
        Map<String, List<Metric>> dateMetricsMap = new HashMap<>();

        fileParser.init(fileAsStream);
        while(fileParser.hasNextLine()) {
            try {
                Metric metric = fileParser.parseLine(fileParser.getNextLine());
                if (metric == null) continue;
                List<Metric> bucket = dateMetricsMap.getOrDefault(metric.getDate(), new ArrayList<>());
                if (bucket.contains(metric)) {
                    Metric existMetric = bucket.get(bucket.indexOf(metric));
                    existMetric.setValue(existMetric.getValue() + metric.getValue());
                } else {
                   bucket.add(metric);
                }
                dateMetricsMap.put(metric.getDate(), bucket);
            } catch (InvalidEntryFormatException iefe) {
                LOG.warn(iefe.getMessage());
            }
        }
        // save aggregated metrics to the persistence store
        for (Map.Entry<String, List<Metric>> entry : dateMetricsMap.entrySet()) {
            entry.getValue().forEach(e -> metricRepository.save(e));
        }
        return dateMetricsMap;
    }


    @Override
    public List<Metric> fetchLeaderBoardByDate(String date) {
        throw new UnsupportedOperationException("Data query operations on DB not supported at this moment!");
    }

    @Override
    public List<Metric> fetchLeaderBoardBetweenRange(String fromDate, String toDate) {
        throw new UnsupportedOperationException("Data query operations on DB not supported at this moment!");
    }
}
