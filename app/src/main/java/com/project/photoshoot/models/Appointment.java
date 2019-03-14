package com.project.photoshoot.models;

import com.google.firebase.database.Exclude;

public class Appointment {
    private String bookingName, email, date, time, status;

    public Appointment() {
    }

    public Appointment(String bookingName, String email, String date, String time, String status) {
        this.bookingName = bookingName;
        this.email = email;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getBookingName() {
        return bookingName;
    }

    public void setBookingName(String bookingName) {
        this.bookingName = bookingName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Exclude
    public String getAllValues() {
        return bookingName + date + time + email + status;
    }
}
