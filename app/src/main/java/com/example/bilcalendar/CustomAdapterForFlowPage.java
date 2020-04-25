package com.example.bilcalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import database.DatabaseManager;
import library.ClubEvent;
import library.Event;
import library.SocialEvent;

public class CustomAdapterForFlowPage extends BaseExpandableListAdapter {

    private Context c;
    private ArrayList<DayCagri> dayCagris;
    private LayoutInflater inflater;
    String endHour,endMinute,startHour,startMinute;

    public CustomAdapterForFlowPage(Context c, ArrayList<DayCagri> dayCagris){
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
        TextView tv_for_start;
        TextView tv_for_finish;
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
                if(event.getEventType() == 1)
                    eventQuota.setText("Quota : "+ ((SocialEvent) event).getQuota());
                else if(event.getEventType() == 2)
                    eventQuota.setText("Quota : "+ ((ClubEvent) event).getQuota());
                else
                    eventQuota.setText("Quota -");

                TextView eventStart = myPopup.findViewById(R.id.start_time_for_pop_up);
                eventStart.setText("Start : "+startHour+":"+startMinute);
                TextView eventFinish = myPopup.findViewById(R.id.finish_time_for_pop_up);
                eventFinish.setText("Finish : "+endHour+":"+endMinute);
                TextView eventType = myPopup.findViewById(R.id.event_type_text_for_pop_up);

                TextView eventNotes = myPopup.findViewById(R.id.note_text_for_pop_up);
                eventNotes.setText(event.getNotes());

                TextView eventDate = myPopup.findViewById(R.id.date_text_for_pop_up);
                eventDate.setText(event.getEventDay().getDayNo()+"/"+event.getEventDay().getMonthNo()+"/"+event.getEventDay().getYearNo());

                Button addButton = myPopup.findViewById(R.id.add_event_button_for_pop_up);
                addButton.setText("Add/Remove Button");

                if(event.getEventType() == 0){
                    addButton.setEnabled(false);
                }
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseManager databaseManager = DatabaseManager.getInstance();
                        if(databaseManager.isAttendedToEvent(databaseManager.getCurUser().getId(),event.getEventId())){
                            if(databaseManager.getCurUser().getId() == event.getHostId()){
                                Toast.makeText(c,"You are the hos of this event, you can not leave it",Toast.LENGTH_LONG).show();
                            }
                            else {
                                databaseManager.removeParticipantFromEvent(event.getEventId(), databaseManager.getCurUser().getId());
                                Toast.makeText(c,"You've removed from this event",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if( event.getEventType() == 1 && ((SocialEvent) event).getQuota() < databaseManager.noOfParticipantsOfEvent(event.getEventId()))
                            Toast.makeText(c, "No quota left", Toast.LENGTH_SHORT).show();
                        else if( event.getEventType() == 2 && ((ClubEvent) event).getQuota() < databaseManager.noOfParticipantsOfEvent(event.getEventId()))
                                Toast.makeText(c, "No quota left", Toast.LENGTH_SHORT).show();

                        else {
                            databaseManager.addParticipantToEvent(event.getEventId(), databaseManager.getCurUser().getId());
                            Toast.makeText(c, "You added this event to your schedule", Toast.LENGTH_SHORT).show();
                        }


                        c.startActivity(new Intent(c.getApplicationContext(),ExpandableListForFlowPage.class));
                        notifyDataSetChanged();

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
