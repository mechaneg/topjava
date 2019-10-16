package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimeFilter {
    private LocalDate fromDate = LocalDate.MIN;
    private LocalDate toDate = LocalDate.MAX;
    private LocalTime fromTime = LocalTime.MIN;
    private LocalTime toTime = LocalTime.MAX;

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public void setFromTime(LocalTime fromTime) {
        this.fromTime = fromTime;
    }

    public void setToTime(LocalTime toTime) {
        this.toTime = toTime;
    }
}
