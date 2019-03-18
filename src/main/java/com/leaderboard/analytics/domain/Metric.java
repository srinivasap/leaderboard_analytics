package com.leaderboard.analytics.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;

/**
 * Data model for metric
 *
 * @author Srinivasa Prasad Sunnapu
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "METRIC")
public class Metric {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "PHOTO_ID")
    private String uuid;

    @Column(name = "DATE")
    private String date;

    @Column(name = "COMPANY")
    private String company;

    @Transient
    @JsonIgnore
    @Column(name = "PLATFORM")
    private String platform;

    @Transient
    @JsonIgnore
    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "KPI_NAME")
    private String name;

    @Column(name = "KPI_VALUE")
    private double value;

    public Metric(String date, String company, String platform, String country, String name, double value) {
        this.date = date;
        this.company = company;
        this.platform = platform;
        this.country = country;
        this.name = name;
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metric)) return false;
        Metric metric = (Metric) o;
        return Objects.equals(getCompany(), metric.getCompany());
                //&& Objects.equals(getPlatform(), metric.getPlatform())
                //&& Objects.equals(getCountry(), metric.getCountry());
    }

    @Override
    public int hashCode() {
        //return Objects.hash(getCompany(), getPlatform(), getCountry());
        return Objects.hash(getCompany());
    }
}
