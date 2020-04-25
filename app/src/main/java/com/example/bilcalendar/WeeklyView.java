package com.example.bilcalendar;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
 * Class for creating weekly view fragment
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class WeeklyView extends Fragment {
    ArrayList<Event>[] eventsInWeek;
    Context context;
    Calendar selectedDay;
    int firstWeekDay;
    Button[] weekDays;
    ArrayList<Event> currentDaysEvents;
    TextView event;

    /**
     * Constructor for weekly view which takes current day as a parameter
     *
     * @param date current day
     */
    public WeeklyView( Calendar date ){
        super();

        // initialise variables
        firstWeekDay = date.get( Calendar.DAY_OF_WEEK );
        selectedDay = (Calendar) date.clone();
        eventsInWeek = new ArrayList[7];

        // initialise arraylists in array
        for(int i = 0; i < 7; i++ ){
            eventsInWeek[i] = new ArrayList<Event>();
        }

        // Find first Day of week
        if( firstWeekDay == Calendar.MONDAY ){ } //compare date is first date
        else if( firstWeekDay == Calendar.TUESDAY ){ // compare - 1
            selectedDay.add( Calendar.DAY_OF_MONTH, -1 );
        }
        else if( firstWeekDay == Calendar.WEDNESDAY ){ // compare - 2
            selectedDay.add( Calendar.DAY_OF_MONTH, -2 );
        }
        else if( firstWeekDay == Calendar.THURSDAY ){ // compare - 3
            selectedDay.add( Calendar.DAY_OF_MONTH, -3 );
        }
        else if( firstWeekDay == Calendar.FRIDAY ){ // compare - 4
            selectedDay.add( Calendar.DAY_OF_MONTH, -4 );
        }
        else if( firstWeekDay == Calendar.SATURDAY ){ // compare - 5
            selectedDay.add( Calendar.DAY_OF_MONTH, -5 );
        }
        else if( firstWeekDay == Calendar.SUNDAY ) { // compare - 6
            selectedDay.add(Calendar.DAY_OF_MONTH, -6);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_view, container, false);

        // initialise variables
        int startTime;
        int length;
        TextView eventText;
        RelativeLayout layout = (RelativeLayout) view.findViewById( R.id.dailyRelativeLayout );
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        weekDays = new Button[]{(Button) view.findViewById(R.id.mondayButton), (Button) view.findViewById(R.id.tuesdayButton),
                (Button) view.findViewById(R.id.wednesdayButton), (Button) view.findViewById(R.id.thursdayButton),
                (Button) view.findViewById(R.id.fridayButton), (Button) view.findViewById(R.id.saturdayButton),
                (Button) view.findViewById(R.id.sundayButton)};

        setButtonTexts();

        RelativeLayout[] layouts = { (RelativeLayout) view.findViewById( R.id.mondayRelativeLayout ), (RelativeLayout) view.findViewById( R.id.tuesdayRelativeLayout ),
                (RelativeLayout) view.findViewById( R.id.wednesdayRelativeLayout ),(RelativeLayout) view.findViewById( R.id.thursdayRelativeLayout ),
                (RelativeLayout) view.findViewById( R.id.fridayRelativeLayout ), (RelativeLayout) view.findViewById( R.id.saturdayRelativeLayout ),
                (RelativeLayout) view.findViewById( R.id.sundayRelativeLayout )};

        // Draw events in week
        for( int i = 0; i < 7; i++ ){
            currentDaysEvents = eventsInWeek[i];
            currentDaysEvents = databaseManager.getAttendedEventsAtDay( selectedDay.get( Calendar.DAY_OF_MONTH), selectedDay.get( Calendar.MONTH ) + 1, selectedDay.get( Calendar.YEAR ) );

            for( int k = 0; k < currentDaysEvents.size(); k++ ){
                startTime = currentDaysEvents.get(k).getStartHour() * 60 + currentDaysEvents.get(k).getStartMinute();
                length = ( currentDaysEvents.get(k).getEndHour() * 60 + currentDaysEvents.get(k).getEndMinute() ) - startTime;
                event = new TextView( context );
                length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, length, context.getResources().getDisplayMetrics());
                startTime = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, startTime, context.getResources().getDisplayMetrics());
                layouts[i].addView( event, new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT, length ));
                event.setY( startTime );
                //event.setGravity(  );
                event.setText( currentDaysEvents.get(k).getEventName() );

                event.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_PopupMenu_Small);

                if( currentDaysEvents.get(k).getEventType() == 0 ){
                    event.setBackgroundColor( Color.parseColor( "#03dac6" ) );
                    event.setTextColor( Color.parseColor( "#0069C0" ) );
                }
                else if( currentDaysEvents.get(k).getEventType() == 1 ){
                    event.setBackgroundColor( Color.GREEN );
                    event.setTextColor( Color.parseColor( "#00600f" ) );
                }
                else if( currentDaysEvents.get(k).getEventType() == 2 ){
                    event.setBackgroundColor( Color.RED );
                    event.setTextColor( Color.RED );
                }
                event.getBackground().setAlpha( 40 );

                //event.setText( currentDaysEvents.get(k).getEventName() );

                //event.setTextAppearance(R.style.TextAppearance_AppCompat_Widget_PopupMenu_Small);


            }

            selectedDay.add( Calendar.DAY_OF_MONTH, 1 );

        }



        return view;
    }

    /**
     * Method for setting the texts of days of week
     */
    private void setButtonTexts(){

        for( int i = 0; i < 7; i++ ){
            weekDays[i].setText( "" + selectedDay.get( Calendar.DAY_OF_MONTH ) );
            selectedDay.add( Calendar.DAY_OF_MONTH, 1 );
        }

        selectedDay.add( Calendar.DAY_OF_MONTH, -7 );

    }
}
