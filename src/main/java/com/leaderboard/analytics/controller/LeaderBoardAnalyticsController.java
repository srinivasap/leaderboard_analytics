package com.leaderboard.analytics.controller;

import com.leaderboard.analytics.domain.Message;
import com.leaderboard.analytics.domain.Metric;
import com.leaderboard.analytics.exception.InputUnreadableException;
import com.leaderboard.analytics.exception.UnsupportedFileTypeException;
import com.leaderboard.analytics.service.AnalyticsService;
import com.leaderboard.analytics.util.Constants;
import com.leaderboard.analytics.util.DateUtil;
import com.leaderboard.analytics.util.SupportedInputFileEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Handles analyst request to process data files and return query results
 *
 * @author Srinivasa Prasad Sunnapu
 */
@RestController
public class LeaderBoardAnalyticsController {

    private static final Logger LOG = LoggerFactory.getLogger(LeaderBoardAnalyticsController.class);

    @Autowired
    private AnalyticsService analyticsServiceCache;

    /**
     * Endpoint to upload file.
     *
     * @param files parse and load analytics from file
     */
    @RequestMapping(
            value = "/leaderboard/analytics/upload",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadPhotos(
            @RequestParam("files") MultipartFile[] files) {
        ResponseEntity<?> response = null;

        try {
            // process the file passed as attachment
            List<Message> result = new ArrayList<>();
            for (MultipartFile file : files) {
                try {
                    String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                    analyticsServiceCache.parseAndLoadFromFile(file.getInputStream(), SupportedInputFileEnum.fromString(ext).getFileParser());
                    result.add(new Message(Message.Status.SUCCESS, "Processed file " + file.getOriginalFilename()));
                } catch (UnsupportedFileTypeException ufte) {
                    result.add(new Message(Message.Status.ERROR, "Unsupported file format "+file.getOriginalFilename()));
                } catch (InputUnreadableException iue) {
                    result.add(new Message(Message.Status.ERROR, "Unable to read file "+file.getOriginalFilename()));
                }
            }
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException ioe) {
            LOG.error("Error reading file attachments", ioe);
            Message errorMessage = new Message(Message.Status.ERROR, Constants.ERROR_MESSAGE_RETRY);
            response = new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    /**
     * Endpoint to query leader board by date, date range
     *
     * @return List<Metric> search results by date, date range
     */
    @RequestMapping(
            value = "/leaderboard/analytics/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<?> downloadPhoto(
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "from_date", required = false) String fromDate,
            @RequestParam(value = "to_date", required = false) String toDate) {
        ResponseEntity<?> response = null;
        try {
            Collection<Metric> metrics = null;
            if (date == null || date.isEmpty()) {
                // set default date as today
                String today = DateUtil.nowDateAsString();
                if ((fromDate == null || fromDate.isEmpty()) && (toDate == null || toDate.isEmpty())) {
                    // if no date or fromDate / toDate passed, query by today date
                    metrics = analyticsServiceCache.fetchLeaderBoardByDate(today);
                } else {
                    // query by date range
                    metrics = analyticsServiceCache.fetchLeaderBoardBetweenRange(fromDate, toDate);
                }
            } else {
                // query by date passed
                metrics = analyticsServiceCache.fetchLeaderBoardByDate(date);
            }
            metrics = metrics == null ? new ArrayList<>() : metrics;
            response = new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (Exception e) {
            response = new ResponseEntity<>(new Message(Message.Status.ERROR, e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }

}
