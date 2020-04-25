package com.example.bilcalendar;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import library.Event;
/**
 * Adapter for dayCagri
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class DayCustomAdapter {

    public int day;
    int month;
    int year;
    public ArrayList<Event> events = new ArrayList<>();

    /**
     * Constructor of the adapter for day cagri
     * @param day
     * @param month
     * @param year
     */
    public DayCustomAdapter(int day,int month,int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public String toString() {
        return (day+"/"+month+"/"+year);
    }
}
