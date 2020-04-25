package com.example.bilcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Calendar;

import database.DatabaseManager;
import library.Event;
/**
 * A class representing a list of hosted events.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class ExpandableListForHostedEvents extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<DayCagri> dayCagris = getData();

        CustomAdapterForHostedEvent adapter = new CustomAdapterForHostedEvent(this, dayCagris);

        setContentView(R.layout.activity_expandable_list);
        ExpandableListView exp = (ExpandableListView) findViewById(R.id.expandable_view);
        exp.setAdapter(adapter);
        int count = adapter.getGroupCount();
        for ( int i = 0; i < count; i++ )
            exp.expandGroup(i);

        Toolbar toolbar = findViewById(R.id.toolbar_for_hosted_event_page);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

    }

    /**
     * Gets them from database and returns hosted events.
     * @return hosted events
     */
    private ArrayList<DayCagri> getData(){

        DatabaseManager databaseManager = DatabaseManager.getInstance();
        Calendar calendar = Calendar.getInstance();

        ArrayList<Event> events = databaseManager.getGlobalEventsAtDay(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));

        DayCagri dayCagri1;
        ArrayList<DayCagri> dayCagris = new ArrayList<>();
        for(int i  = 0; i<30 ; i++){
            dayCagri1 = new DayCagri(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            events = databaseManager.getHostedEventsAtDay(calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR));
            for (Event event : events) {
                dayCagri1.eventCagrises.add(event);
            }
            calendar.add(Calendar.DAY_OF_MONTH,1);
            if(dayCagri1.eventCagrises.size() != 0)
                dayCagris.add(dayCagri1);
        }

        return dayCagris;

    }
}
