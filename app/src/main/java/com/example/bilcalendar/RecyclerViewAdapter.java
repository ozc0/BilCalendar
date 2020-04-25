package com.example.bilcalendar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseManager;
import library.ClubEvent;
import library.Day;
import library.Event;
import library.SocialEvent;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>{

    Context mContext;
    ArrayList<Event> mData;
    String startHour;
    String startMinute;
    String endHour;
    String endMinute;
    //Dialog myPopup;

    public RecyclerViewAdapter(Context mContext, ArrayList<Event> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.row,parent,false);
        final MyViewHolder vHolder = new MyViewHolder(v);

        final Dialog myPopup;
        myPopup = new Dialog(mContext);
        myPopup.setContentView(R.layout.add_event_pop_up);
        final DatabaseManager databaseManager = DatabaseManager.getInstance();

        vHolder.item_row.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {


                TextView eventName = myPopup.findViewById(R.id.event_name_text_for_pop_up);
                eventName.setText(mData.get(vHolder.getAdapterPosition()).getEventName());
                TextView eventQuota = myPopup.findViewById(R.id.event_quota_for_pop_up);

                if(mData.get(vHolder.getAdapterPosition()).getEventType() == 1){
                    int quota = ((SocialEvent) mData.get(vHolder.getAdapterPosition())).getQuota();
                    eventQuota.setText("Quota "+ quota );
                    eventQuota.setTextColor(Color.parseColor("#3995ff"));
                    TextView location = myPopup.findViewById(R.id.location_for_pop_up);
                    location.setText(((SocialEvent) mData.get(vHolder.getAdapterPosition())).getLocation());

                    //TextView organizer = myPopup.findViewById(R.id.organizer_for_pop_up);
                    //organizer.setText(((SocialEvent) mData.get(vHolder.getAdapterPosition())).getHostId());
                }
                else if(mData.get(vHolder.getAdapterPosition()).getEventType() == 2){
                    int quota = ((ClubEvent) mData.get(vHolder.getAdapterPosition())).getQuota();
                    eventQuota.setText("Quota "+ quota );
                    eventQuota.setTextColor(Color.parseColor("#3995ff"));
                    TextView location = myPopup.findViewById(R.id.location_for_pop_up);
                    location.setText(((ClubEvent) mData.get(vHolder.getAdapterPosition())).getLocation());
                }
                else
                    eventQuota.setText("Quota -" );
                TextView eventStart = myPopup.findViewById(R.id.start_time_for_pop_up);
                eventStart.setText("Start : "+startHour+":"+startMinute);
                TextView eventFinish = myPopup.findViewById(R.id.finish_time_for_pop_up);
                eventFinish.setText("Finish : "+endHour+":"+endMinute);

                TextView eventDate = myPopup.findViewById(R.id.date_text_for_pop_up);
                eventDate.setText(mData.get(vHolder.getAdapterPosition()).getEventDay().getDayNo()+"/"
                                          +mData.get(vHolder.getAdapterPosition()).getEventDay().getMonthNo()+"/"+mData.get(vHolder.getAdapterPosition()).getEventDay().getYearNo());

                TextView eventType = myPopup.findViewById(R.id.event_type_text_for_pop_up);

                TextView eventNotes = myPopup.findViewById(R.id.note_text_for_pop_up);
                eventNotes.setText("Notes :"+mData.get(vHolder.getAdapterPosition()).getNotes());





                TextView notes = myPopup.findViewById(R.id.note_text_for_pop_up);
                notes.setText(mData.get(vHolder.getAdapterPosition()).getNotes());
                final Button addButton = myPopup.findViewById(R.id.add_event_button_for_pop_up);

                if(databaseManager.isAttendedToEvent(databaseManager.getCurUser().getId(),mData.get(vHolder.getAdapterPosition()).getEventId())){

                    addButton.setText("Remove from schedule");
                }
                else{
                    addButton.setText("Add to schedule");
                }

                if(mData.get(vHolder.getAdapterPosition()).getEventType() == 0){
                    addButton.setEnabled(false);
                }

                addButton.setText("Add/Remove Button");
                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DatabaseManager databaseManager = DatabaseManager.getInstance();
                        if(databaseManager.isAttendedToEvent(databaseManager.getCurUser().getId(),mData.get(vHolder.getAdapterPosition()).getEventId())){
                            if(databaseManager.getCurUser().getId() == mData.get(vHolder.getAdapterPosition()).getHostId()){
                                Toast.makeText(mContext,"You are the host of this event, you can not leave it",Toast.LENGTH_LONG).show();
                            }
                            else {
                                databaseManager.removeParticipantFromEvent(mData.get(vHolder.getAdapterPosition()).getEventId(), databaseManager.getCurUser().getId());
                                Toast.makeText(mContext,"You've removed from this event",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if( mData.get(vHolder.getAdapterPosition()).getEventType() == 1 && ((SocialEvent) mData.get(vHolder.getAdapterPosition())).getQuota() < databaseManager.noOfParticipantsOfEvent(mData.get(vHolder.getAdapterPosition()).getEventId()) ) {
                            Toast.makeText(mContext,"No quota left",Toast.LENGTH_SHORT).show();
                        }
                        else if( mData.get(vHolder.getAdapterPosition()).getEventType() == 2 && ((ClubEvent) mData.get(vHolder.getAdapterPosition())).getQuota() < databaseManager.noOfParticipantsOfEvent(mData.get(vHolder.getAdapterPosition()).getEventId()) ) {
                            Toast.makeText(mContext,"No quota left",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            databaseManager.addParticipantToEvent(mData.get(vHolder.getAdapterPosition()).getEventId(), databaseManager.getCurUser().getId());
                            Toast.makeText(mContext,"You added this event to your schedule",Toast.LENGTH_SHORT).show();
                        }


                        mContext.startActivity(new Intent(mContext.getApplicationContext(),MainMenu.class));
                        notifyDataSetChanged();

                    }
                });


                if(mData.get(vHolder.getAdapterPosition()).getEventType() == 0){
                    eventType.setText("Personal Event");
                    eventType.setTextColor(Color.parseColor("#3995ff"));
                }

                else if(mData.get(vHolder.getAdapterPosition()).getEventType() == 1) {
                    eventType.setText("Social Event");
                    eventType.setTextColor((Color.parseColor("#82cc53")));

                }
                else if (mData.get(vHolder.getAdapterPosition()).getEventType() == 2){
                    eventType.setText("Club Event");
                    eventType.setTextColor(Color.parseColor("#f40000"));
                }

                Toast.makeText(mContext,"Test Click "+String.valueOf(vHolder.getAdapterPosition()),Toast.LENGTH_SHORT).show();
                startFeedBackAnimation(vHolder.item_row);
                myPopup.show();
            }
        });
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.tv_for_event.setText(mData.get(position).getEventName());
        if(mData.get(position).getStartHour() < 10)
            startHour ="0"+mData.get(position).getStartHour();
        else
            startHour = ""+mData.get(position).getStartHour();
        if(mData.get(position).getStartMinute() < 10)
            startMinute ="0"+mData.get(position).getStartMinute();
        else
            startMinute = ""+mData.get(position).getStartMinute();
        if(mData.get(position).getEndHour() < 10)
            endHour ="0"+mData.get(position).getEndHour();
        else
            endHour = ""+mData.get(position).getEndHour();
        if(mData.get(position).getEndMinute() < 10)
            endMinute ="0"+mData.get(position).getEndMinute();
        else
            endMinute = ""+mData.get(position).getEndMinute();

            holder.tv_for_start.setText(startHour+":"+startMinute);

        holder.tv_for_finish.setText(endHour+":"+endMinute);
        if ((mData.get(position).getEventType()) == 0 )
            holder.img_for_color.setImageResource(R.drawable.blue);
        else if (mData.get(position).getEventType() == 1)
            holder.img_for_color.setImageResource(R.drawable.green);
        else if (mData.get(position).getEventType() == 2)
            holder.img_for_color.setImageResource(R.drawable.red);
        else
            holder.img_for_color.setImageResource(R.drawable.black);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout item_row;
        private TextView tv_for_event;
        private TextView tv_for_start;
        private TextView tv_for_finish;
        private ImageView img_for_color;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            item_row = (LinearLayout)itemView.findViewById(R.id.row_linear_layout);
            tv_for_event = (TextView) itemView.findViewById(R.id.text_for_event);
            tv_for_start = (TextView) itemView.findViewById(R.id.text_for_time_start);
            tv_for_finish = (TextView) itemView.findViewById(R.id.text_for_time_finish);
            img_for_color = (ImageView) itemView.findViewById(R.id.image_of_color);


        }
    }

    private void startFeedBackAnimation( LinearLayout object){
        ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#DFF6F9")), new ColorDrawable(Color.parseColor("#ffffff"))};
        TransitionDrawable trans = new TransitionDrawable(color);
        object.setBackground(trans);
        trans.startTransition(300); // duration 3 seconds
    }

}
