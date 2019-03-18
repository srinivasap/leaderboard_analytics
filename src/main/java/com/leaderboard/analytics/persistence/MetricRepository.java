package com.leaderboard.analytics.persistence;

import com.leaderboard.analytics.domain.Metric;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring JPA persistence repository for Metrics data
 *
 * @author Srinivasa Prasad Sunnapu
 */
@Repository
public interface MetricRepository extends CrudRepository<Metric, String> {

}
