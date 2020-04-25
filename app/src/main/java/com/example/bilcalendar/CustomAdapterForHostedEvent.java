package com.example.bilcalendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import database.DatabaseManager;
import library.ClubEvent;
import library.Event;
import library.SocialEvent;
import library.User;

public class CustomAdapterForHostedEvent extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<DayCagri> dayCagris;
    private LayoutInflater inflater;
    String endHour,endMinute,startHour,startMinute;

    public CustomAdapterForHostedEvent(Context c, ArrayList<DayCagri> dayCagris){
        this.c = c;
        this.dayCagris = dayCagris;
        inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getGroupCount() {
        return dayCagris.size();
    }

    @Override
    public int getChildrenCount(int groupPos) {
        return dayCagris.get(groupPos).eventCagrises.size();
    }

    @Override
    public Object getGroup(int groupPos) {
        return dayCagris.get(groupPos);
    }

    //GET A SINGLE EVENT
    @Override
    public Object getChild(int groupPos, int childPos) {
        return dayCagris.get(groupPos).eventCagrises.get(childPos);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //GET DAY VIEW
    @Override
    public View getGroupView(int groupPos, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.day_row,null);
        }

        DayCagri dayCagri = (DayCagri) getGroup(groupPos);
        TextView dayText = convertView.findViewById(R.id.day_text_for_flow_page);
        dayText.setText(dayCagri.toString());

        return convertView;
    }

    //GET CHILD ROW
    @Override
    public View getChildView(final int groupPos, final int childPos, boolean isLastChild, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = inflater.inflate(R.layout.row,null);
        }

        final Event event = (Event) getChild(groupPos, childPos);

        LinearLayout item_row;
        TextView tv_for_event;
        final TextView tv_for_start;
        final TextView tv_for_finish;
        ImageView img_for_color;

        item_row = (LinearLayout)convertView.findViewById(R.id.row_linear_layout);
        tv_for_event = (TextView) convertView.findViewById(R.id.text_for_event);
        tv_for_start = (TextView) convertView.findViewById(R.id.text_for_time_start);
        tv_for_finish = (TextView) convertView.findViewById(R.id.text_for_time_finish);
        img_for_color = (ImageView) convertView.findViewById(R.id.image_of_color);

        tv_for_event.setText(event.getEventName());


        if(event.getStartHour() < 10)
            startHour ="0"+event.getStartHour();
        else
            startHour = ""+event.getStartHour();
        if(event.getStartMinute() < 10)
            startMinute ="0"+event.getStartMinute();
        else
            startMinute = ""+event.getStartMinute();
        if(event.getEndHour() < 10)
            endHour ="0"+event.getEndHour();
        else
            endHour = ""+event.getEndHour();
        if(event.getEndMinute() < 10)
            endMinute ="0"+event.getEndMinute();
        else
            endMinute = ""+event.getEndMinute();
        tv_for_start.setText("Start : "+startHour+":"+startMinute);
        tv_for_finish.setText("Finish : "+endHour+":"+endMinute);

        if (event.getEventType() == 0)
            img_for_color.setImageResource(R.drawable.blue);
        else if (event.getEventType() == 1)
            img_for_color.setImageResource(R.drawable.green);
        else if (event.getEventType() == 2)
            img_for_color.setImageResource(R.drawable.red);
        else
            img_for_color.setImageResource(R.drawable.black);
        final Dialog myPopup;

        myPopup = new Dialog(c);
        myPopup.setContentView(R.layout.add_event_pop_up);

        item_row.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                TextView eventName = myPopup.findViewById(R.id.event_name_text_for_pop_up);
                eventName.setText(event.getEventName());
                TextView eventQuota = myPopup.findViewById(R.id.event_quota_for_pop_up);
                TextView eventStart = myPopup.findViewById(R.id.start_time_for_pop_up);
                TextView eventEnd = myPopup.findViewById(R.id.finish_time_for_pop_up);

                if(event.getEventType() == 1)
                    eventQuota.setText("Quota : "+ ((SocialEvent) event).getQuota());
                else if(event.getEventType() == 2)
                    eventQuota.setText("Quota : "+ ((ClubEvent) event).getQuota());
                else
                    eventQuota.setText("Quota -");

                eventStart.setText("Start :"+startHour+":"+startMinute);
                eventEnd.setText("Finish :"+endHour+":"+endMinute);


                TextView eventType = myPopup.findViewById(R.id.event_type_text_for_pop_up);

                TextView eventDate = myPopup.findViewById(R.id.date_text_for_pop_up);
                eventDate.setText(event.getEventDay().getDayNo()+"/"+event.getEventDay().getMonthNo()+"/"+event.getEventDay().getYearNo());

                Button seeButton = myPopup.findViewById(R.id.add_event_button_for_pop_up);
                seeButton.setText("See participants");

                final Dialog mySecondPopup = new Dialog(c);
                mySecondPopup.setContentView(R.layout.popup_for_participants);

                final ListView listViewOfParticipants = mySecondPopup.findViewById(R.id.list_view_for_participants);

                seeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        DatabaseManager databaseManager = DatabaseManager.getInstance();

                        String id = event.getEventId();

                        ArrayList<User> participants = databaseManager.getParicipants(id);

                        ArrayList<String> participantsInfo = new ArrayList<>();

                        for (int i = 0; i < participants.size(); i++) {
                            participantsInfo.add( participants.get(i).getName() + ": " + participants.get(i).getEmail() );
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1, participantsInfo);

                        listViewOfParticipants.setAdapter( arrayAdapter );

                        mySecondPopup.show();

                    }

                });


                if(event.getEventType() == 0){
                    eventType.setText("Personal Event");
                    eventType.setTextColor(Color.parseColor("#3995ff"));
                }

                else if(event.getEventType() == 1) {
                    eventType.setText("Social Event");
                    eventType.setTextColor((Color.parseColor("#82cc53")));

                }
                else if (event.getEventType() == 2){
                    eventType.setText("Club Event");
                    eventType.setTextColor(Color.parseColor("#f40000"));
                }

                Toast.makeText(c, "Test Click "+String.valueOf(groupPos)+" "+String.valueOf(childPos), Toast.LENGTH_SHORT).show();
                myPopup.show();
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
