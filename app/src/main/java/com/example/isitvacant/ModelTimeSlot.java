package com.example.isitvacant;

public class ModelTimeSlot {

    public String date,time_slot;
    public ModelTimeSlot() {
    }

    public ModelTimeSlot(String date, String time_slot) {
        this.date = date;
        this.time_slot = time_slot;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }
}
