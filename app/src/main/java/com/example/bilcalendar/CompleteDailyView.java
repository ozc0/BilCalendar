package com.example.bilcalendar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

/**
 * Complete part of the daily fragment
 *
 * @author team of Ministler
 * @date 26.12.2019
 *
 */
public class CompleteDailyView extends Fragment {

    public SectionsPageAdapter dailyStatePagerAdapter;
    public SectionsPageAdapter adapter;
    public ViewPager viewPager;
    private Context context;
    TabLayout tabLayout;
    Calendar firstDay;
    int currentItem;

    /**
     * Constructor of complete daily view which takes chosen day as a parameter
     * @param date chosen day
     */
    public CompleteDailyView( Calendar date ){
        firstDay = ( Calendar ) date.clone();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.daily_view, container, false);

        // initialise UI parts
        viewPager = (ViewPager) view.findViewById( R.id.daily_event_container );
        tabLayout = (TabLayout) view.findViewById( R.id.weekdays_tablayout);

        dailyStatePagerAdapter = new SectionsPageAdapter( getFragmentManager() );

        //found the week containing the chosen day
        containWeek();

        //set up slider UI parts
        setupViewPager();
        setupTabLayout();

        // set the current day chosen
        viewPager.setCurrentItem( currentItem );

        return view;
    }

    /**
     * method for setting the tab layout
     */
    private void setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager);

        for( int i = 0; i < 7; i++ ){
            tabLayout.getTabAt(i).setText( String.valueOf( firstDay.get( Calendar.DAY_OF_MONTH ) ) );
            firstDay.add( Calendar.DAY_OF_WEEK, 1 );
        }

        firstDay.add( Calendar.DAY_OF_WEEK, -7);

    }

    /**
     *  method for setting the view pager
     */
    private void setupViewPager( ){

        adapter = new SectionsPageAdapter( getFragmentManager() );
        for( int i = 0; i < 7; i++ ){
            adapter.addFragment( new DailyView( firstDay ), "" + firstDay.get( Calendar.DAY_OF_MONTH ) );
            firstDay.add( Calendar.DAY_OF_MONTH, 1 );
        }

        firstDay.add( Calendar.DAY_OF_MONTH, -7 );

        viewPager.setAdapter( adapter );
    }

    /**
     * method for finding first day of week and which day will be selected when ui is completely set
     */
    private void containWeek(){
        if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.MONDAY ){//compare date is first date
            currentItem = 0;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.TUESDAY ){ // compare - 1
            firstDay.add( Calendar.DAY_OF_MONTH, -1 );
            currentItem = 1;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.WEDNESDAY ){ // compare - 2
            firstDay.add( Calendar.DAY_OF_MONTH, -2 );
            currentItem = 2;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.THURSDAY ){ // compare - 3
            firstDay.add( Calendar.DAY_OF_MONTH, -3 );
            currentItem = 3;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY ){ // compare - 4
            firstDay.add( Calendar.DAY_OF_MONTH, -4 );
            currentItem = 4;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY ){ // compare - 5
            firstDay.add( Calendar.DAY_OF_MONTH, -5 );
            currentItem = 5;
        }
        else if( firstDay.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY ){ // compare - 6
            firstDay.add( Calendar.DAY_OF_MONTH, -6 );
            currentItem = 6;
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

}
