package com.example.bilcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

import database.DatabaseManager;
import library.Event;

/**
 * Daily View Fragment
 *
 * @author Team of Ministler
 * @date 26.12.2019
 */
public class DailyView extends Fragment {
    ArrayList<Event> eventsInDay;
    Context context;
    Calendar day;

    /**
     * Constructor for daily view which takes current day as a parameter
     *
     * @param day current day
     */

    DailyView( Calendar day ){
        super();
        this.day = (Calendar) day.clone();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_event_container, container, false);

        int startTime;
        int length;
        TextView eventText;
        RelativeLayout layout = (RelativeLayout) view.findViewById( R.id.dailyRelativeLayout );
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        ArrayList<Event> allAttendedEvents;

        // Get events at current day
        eventsInDay = databaseManager.getAttendedEventsAtDay( day.get(Calendar.DAY_OF_MONTH), day.get(Calendar.MONTH) + 1, day.get(Calendar.YEAR));

        for( int k = 0; k < eventsInDay.size(); k++ ){ //draw the events in daily view
            startTime = eventsInDay.get(k).getStartHour() * 60 + eventsInDay.get(k).getStartMinute();
            length = ( eventsInDay.get(k).getEndHour() * 60 + eventsInDay.get(k).getEndMinute() ) - startTime;
            eventText = new TextView( context );
            length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, length, context.getResources().getDisplayMetrics());
            startTime = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, startTime, context.getResources().getDisplayMetrics());
            layout.addView( eventText, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, length ));
            eventText.setY( startTime );
            //event.setGravity(  );
            eventText.setText( eventsInDay.get(k).getEventName() );

            eventText.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_PopupMenu_Small);

            if( eventsInDay.get(k).getEventType() == 0 ){ // Draw Personal event
                eventText.setBackgroundColor( Color.parseColor( "#03dac6" ) );
                eventText.setTextColor( Color.parseColor( "#0069C0" ) );
            }
            else if( eventsInDay.get(k).getEventType() == 1 ){ // Draw social event
                eventText.setBackgroundColor( Color.GREEN );
                eventText.setTextColor( Color.parseColor( "#00600f" ) );
            }
            else if( eventsInDay.get(k).getEventType() == 2 ){ // Draw club event
                eventText.setBackgroundColor( Color.RED );
                eventText.setTextColor( Color.RED );
            }
            eventText.getBackground().setAlpha( 40 );

        }


        return view;
    }
}
