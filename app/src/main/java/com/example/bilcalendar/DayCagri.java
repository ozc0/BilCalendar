package com.example.bilcalendar;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import library.Event;
/**
 * A class representing a day.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class DayCagri {
    public int day;
    public int month;
    public int year;
    public ArrayList<Event> eventCagrises = new ArrayList<>();

    /**
     * Specific day class for cagri
     *
     * @param day
     * @param month
     * @param year
     */
    public DayCagri(int day, int month,int year){
        this.day =day;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public String toString() {
        return (day+"/"+month+"/"+year);
    }
}
