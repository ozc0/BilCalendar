package com.example.bilcalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
import library.Event;
import library.MainActivity;
/**
 * Fragment class of the flow.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class RecyclerFlowFragment extends Fragment {

    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<Event> list, listt;
    private ArrayList<Event> myList;
    private RecyclerFlowFragment thisIns = this;
    int day, month, year;

    /**
     * Constructor -
     */
    public RecyclerFlowFragment(int day, int month, int year) {
        this.day = day;
        this.year = year;
        this.month = month;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.recycler_flow_view, container, false);
        myRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_flow_view);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(getContext(), listt);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerViewAdapter);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        Boolean isShowClubEventsChecked = sharedPref.getBoolean("isShowClubEventsChecked", false);
        Boolean isShowSocialEventsChecked = sharedPref.getBoolean("isShowSocialEventsChecked", false);
        Boolean isShowIndividualEventsChecked = sharedPref.getBoolean("isShowIndividualEventsChecked", false);
        Boolean isShowGlobalEventChecked = sharedPref.getBoolean("isShowGlobalEventsOpen", false);
        Boolean isShowAvaiableEventChecked = sharedPref.getBoolean("isShowAvailableEvents", false);
        Boolean isPlannedEventsChecked = sharedPref.getBoolean("isShowPlannedEventChecked", false);

        list = new ArrayList<>();

        myList = new ArrayList<>();

        listt = new ArrayList<>();


        DatabaseManager databaseManager = DatabaseManager.getInstance();
        ArrayList<Event> events1 = databaseManager.getGlobalEventsAtDay(day, month, year);
        ArrayList<Event>  events2 = databaseManager.getAttendedEventsAtDay(day, month, year);
        for (Event event : events2) {
            Boolean b = true;
            for(Event eventt : events1) {
                if(eventt.getEventId() == event.getEventId())
                    b = false;
            }
            if(b){
                myList.add(event);
            }
        }
        for(Event event : events1){
            myList.add(event);
        }


        for (Event event : myList) {
            if (isShowClubEventsChecked && event.getEventType() == 2) {
                list.add(event);
            } else if (isShowSocialEventsChecked && event.getEventType() == 1) {
                list.add(event);
            } else if (isShowIndividualEventsChecked && event.getEventType() == 0) {
                list.add(event);
            } else if (isShowGlobalEventChecked && event.getEventType() > 0) {
                list.add(event);
            }
        }

        for(Event event: list){

            if (isPlannedEventsChecked && databaseManager.isAttendedToEvent(databaseManager.getCurUser().getId(),event.getEventId())) {
                listt.add(event);
            }
            if(isShowAvaiableEventChecked && !databaseManager.isAttendedToEvent(databaseManager.getCurUser().getId(),event.getEventId()))
                listt.add(event);

        }

    }
}