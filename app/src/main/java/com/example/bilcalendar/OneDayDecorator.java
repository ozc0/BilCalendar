package com.example.bilcalendar;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorates a day by making the text big and bold
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class OneDayDecorator implements DayViewDecorator {

    private final int[] colors;
    private final HashSet<CalendarDay> dates;

    /**
     * Constructor -
     * @param dates, dates that will be decorated
     * @param colors, colors that will be used in decoration
     */
    public OneDayDecorator(Collection<CalendarDay> dates, int[] colors) {
        //this.color = color;
        int[] whiteColors;
        whiteColors = new int[colors.length];

        for (int i=0; i <colors.length; i++)
            whiteColors[i] = Color.parseColor("#f40000");

        this.dates = new HashSet<>(dates);

        this.colors = colors;



    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return dates.contains(day);
    }


    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan((new CustmMultipleDotSpan(9, colors)));
    }

    /**
     * Selects drawables.
     * @param kek, drawable
     */
    public void setSelectionDrawable(Drawable kek) {
        
    }
}
