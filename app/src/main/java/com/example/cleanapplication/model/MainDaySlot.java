package com.example.cleanapplication.model;

import java.util.List;

public class MainDaySlot {
    String dayname;
    String date;
    List<SlotsModel> slotsList;

    public MainDaySlot(String dayname, String date, List<SlotsModel> slotsList) {
        this.dayname = dayname;
        this.date = date;
        this.slotsList = slotsList;
    }
    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<SlotsModel> getSlotsList() {
        return slotsList;
    }

    public void setSlotsList(List<SlotsModel> slotsList) {
        this.slotsList = slotsList;
    }
}