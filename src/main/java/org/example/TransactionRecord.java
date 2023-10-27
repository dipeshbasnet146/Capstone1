package org.example;

import java.time.LocalDate;
import java.time.LocalTime;

public class TransactionRecord {

    private double amount;
    private LocalDate date;
    private String description;
    private String vendor;
    private LocalTime time;

    public TransactionRecord(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.description = description;
        this.vendor = vendor;
        this.time = time;
        this.amount = amount;

    }

    public double getAmount() {
        return amount;
    }


    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

public String getAmountFormatted(){

        return "$"+amount;
}

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
