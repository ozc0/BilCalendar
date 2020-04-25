package com.example.bilcalendar;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;

import database.DatabaseManager;
import library.Day;
import library.Event;

/**
 * Fragment of the main menu activity
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class MainMenuFragment extends Fragment {

    //Variables
    private ViewPager viewPager;
    private MaterialCalendarView calendar;
    SharedPreferences.OnSharedPreferenceChangeListener listener;
    DatabaseManager databaseManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);
        viewPager = view.findViewById(R.id.event_list_viewer);

        // initialise database manager
        databaseManager = DatabaseManager.getInstance();


        // find all the days with events
        final ArrayList<Day> days = findDaysWithEvents();

        //Setup view pager
        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter( getFragmentManager() );
        for( int i = 0; i < days.size(); i++ ) {
            sectionsPageAdapter.addFragment( new RecyclerFlowFragment(days.get(i).getDayNo(),days.get(i).getMonthNo(),days.get(i).getYearNo()), "events");
        }
        sectionsPageAdapter.addFragment( new RecyclerFlowFragment( 1, 1, 2023 ), "0" );

        viewPager.setAdapter( sectionsPageAdapter );
        viewPager.setCurrentItem( days.size() );


        // make a calendar view
        calendar = view.findViewById(R.id.calendarView);
        calendar.setTopbarVisible(false);

        calendar.setSelectedDate( CalendarDay.today() );

        calendar.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                final String[] monthName = {"January", "February",
                        "March", "April", "May", "June", "July",
                        "August", "September", "October", "November",
                        "December"};

                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( monthName[ date.getMonth()-1 ] + " " + date.getYear());
            }
        });
        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // update current date
                setCurrentDate( date );

                //find the events in selected day
                for( int i = 0; i <= days.size(); i++ ){
                    if( i == days.size() ){
                        viewPager.setCurrentItem(i);
                    }
                    else if( days.get(i).getDayNo() == MainMenu.currentDate.get( Calendar.DAY_OF_MONTH ) && days.get(i).getMonthNo() - 1 == MainMenu.currentDate.get( Calendar.MONTH ) && days.get(i).getYearNo() == MainMenu.currentDate.get( Calendar.YEAR ) ){
                        viewPager.setCurrentItem(i);
                        i = days.size() + 2;
                    }
                }
            }
        });

        return view;
    }

    /**
     * Updates current date in order to CalendarDay variable
     *
     * @param date selected date
     */
    private void setCurrentDate( CalendarDay date ){
        MainMenu.currentDate.set( date.getYear(), date.getMonth(), date.getDay() );
        MainMenu.currentDate.add( Calendar.MONTH, -1);
    }


    /**
     * Method for finding all days with events
     *
     * @return days with events
     */
    private ArrayList<Day> findDaysWithEvents(){
        ArrayList<Day> days = new ArrayList<Day>();

        ArrayList<Event> events = databaseManager.getAttendedEvents();
        for( int i = 1; i < events.size(); i++ ){
            if( events.get(i-1).compareTo( events.get(i) ) != 0 ){
                days.add( events.get(i-1).getEventDay() );
                if( i + 1 == events.size() ){
                    days.add( events.get(i).getEventDay() );
                }
            }
        }

        return days;
    }


}