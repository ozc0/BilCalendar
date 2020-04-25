package com.example.bilcalendar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ArrayList;

import database.DatabaseManager;
import library.Day;
import library.Event;
import library.User;
/**
 * Class representing a club event fragment.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class HostClubEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private static final String TAG = "HostClubEventFragment";

    private TextInputLayout eventName, notes, quota, location;
    private TextView organizerNameText, dateText, endTimeText, startTimeText;
    private LinearLayout organizerLayout, dateLayout, startLayout, endLayout;

    private FloatingActionButton hostEventButton;

    private int layoutSelection;
    private int organizerNameSelection;

    private View viewForSnackBar;

    private android.icu.util.Calendar calendarStartDate, calendarEndDate;

    private String[] items;

    private int quotaInt = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.host_club_event,container,false);

        viewForSnackBar = view;

        eventName = view.findViewById(R.id.club_event_name_textInput);
        notes = view.findViewById(R.id.club_event_notes_textInput);
        quota = view.findViewById(R.id.club_event_quota_textInput);
        location = view.findViewById(R.id.club_event_location_name_textInput);

        dateText =  view.findViewById(R.id.club_event_date_textView);
        startTimeText = view.findViewById(R.id.club_event_start_time_textView);
        endTimeText = view.findViewById(R.id.club_event_end_time_textView);

        organizerNameText =  view.findViewById(R.id.club_event_organizer_name_textView);

        dateLayout = view.findViewById(R.id.club_event_date_layout);
        startLayout = view.findViewById(R.id.club_event_start_time_layout);
        endLayout = view.findViewById(R.id.club_event_end_time_layout);

        organizerLayout = view.findViewById(R.id.club_event_organizer_club_layout);

        calendarStartDate = android.icu.util.Calendar.getInstance();
        calendarEndDate = android.icu.util.Calendar.getInstance();


        organizerNameSelection = -1;

        setDateText();
        setLayoutListeners();

        hostEventButton = view.findViewById(R.id.host_club_event_button);
        hostEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( isAppropriate() ) {
                    DatabaseManager databaseManager = DatabaseManager.getInstance();
                    User user = databaseManager.getCurUser();
                    Day eventDay = new Day( calendarEndDate.get(Calendar.DAY_OF_MONTH), calendarEndDate.get(Calendar.MONTH)+1, calendarEndDate.get(Calendar.YEAR) );
                    int startHour = calendarStartDate.get(Calendar.HOUR_OF_DAY);
                    int startMinute = calendarStartDate.get(Calendar.MINUTE);
                    int endHour = calendarEndDate.get(Calendar.HOUR_OF_DAY);
                    int endMinute = calendarEndDate.get(Calendar.MINUTE);
                    databaseManager.addClubEvent( eventName.getEditText().getText().toString(), notes.getEditText().getText().toString(), user.getId(), eventDay, startHour, startMinute, endHour, endMinute, location.getEditText().getText().toString(), quotaInt , databaseManager.getClubAdministrations().get( organizerNameSelection ) );
                    startActivity(new Intent(getActivity(), MainMenu.class));
                }
            }
        });

        return view;
    }

    /**
     * Sets the text representing the day.
     */
    private void setDateText() {
        SimpleDateFormat dmy = new SimpleDateFormat("dd MMM yyyy EE");
        SimpleDateFormat hm = new SimpleDateFormat( "HH:mm");

        dateText.setText( dmy.format(calendarStartDate.getTime()) );
        startTimeText.setText(hm.format(calendarStartDate.getTime()));
        endTimeText.setText(hm.format(calendarEndDate.getTime()));

        /*startText.setText(sdf.format(calendarStartDate.getTime()));
        endText.setText(sdf.format(calendarEndDate.getTime()));*/
    }

    /**
     * Sets layout listeners.
     */
    private void setLayoutListeners() {
        layoutSelection = 0;
        dateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelection = 0;
                startFeedBackAnimation( dateLayout );

                DatePickerFragment dialog = new DatePickerFragment();
                dialog.setTargetFragment(HostClubEventFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        startLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelection = 1;
                startFeedBackAnimation( startLayout );

                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setTargetFragment(HostClubEventFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        endLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelection = 2;
                startFeedBackAnimation( endLayout );

                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setTargetFragment(HostClubEventFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        organizerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutSelection = 3;
                startFeedBackAnimation( organizerLayout );

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogStyle);

                builder.setTitle("Select Club");

                final DatabaseManager databaseManager = DatabaseManager.getInstance();

                items = new String[ databaseManager.getClubAdministrations().size() ];

                for( int i = 0; i < databaseManager.getClubAdministrations().size(); i++ ) {
                    items[ i ] = databaseManager.getClubAdministrations().get( i ).getClubName();
                }

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        organizerNameText.setText(items[ which ]);
                        organizerNameSelection = which;
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        if( ( layoutSelection == 0 && !validateDate( year, month, dayOfMonth) ) )
            return;

        if (layoutSelection == 0) {
            calendarStartDate.set(Calendar.YEAR, year);
            calendarStartDate.set(Calendar.MONTH, month);
            calendarStartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if( !isOnSameDay( calendarStartDate, today) ) {
                calendarStartDate.set(Calendar.HOUR_OF_DAY, 0);
                calendarStartDate.set(Calendar.MINUTE, 0);
                calendarEndDate.set(Calendar.HOUR_OF_DAY, 0);
                calendarEndDate.set(Calendar.MINUTE, 0);
            }
            else{
                calendarStartDate.set(Calendar.HOUR_OF_DAY, today.get( Calendar.HOUR_OF_DAY ) );
                calendarStartDate.set(Calendar.MINUTE, today.get( Calendar.MINUTE ) );
                calendarEndDate.set(Calendar.HOUR_OF_DAY, today.get( Calendar.HOUR_OF_DAY ) );
                calendarEndDate.set(Calendar.MINUTE, today.get( Calendar.MINUTE ));
            }
            //if( calendarEndDate.compareTo( calendarStartDate ) < 0 || !isOnSameDay( calendarEndDate, calendarStartDate ) ) {
            calendarEndDate.set(Calendar.YEAR, year);
            calendarEndDate.set(Calendar.MONTH, month);
            calendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            //}
        }
        /*if (layoutSelection == 1) {
            calendarStartDate.set(Calendar.YEAR, year);
            calendarEndDate.set(Calendar.MONTH, month);
            calendarEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }*/

        /*TimePickerFragment dialog = new TimePickerFragment();
        dialog.setTargetFragment(HostPersonalEventFragment.this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "timePicker");

*/
        setDateText();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if( ( layoutSelection == 1 && !validateStartTime( hourOfDay, minute) )
                || ( layoutSelection == 2 && !validateEndTime( hourOfDay, minute )) )
            return;

        if( layoutSelection == 1) {
            calendarStartDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarStartDate.set(Calendar.MINUTE, minute);

            if( calendarEndDate.compareTo( calendarStartDate ) < 0 ) {
                calendarEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendarEndDate.set(Calendar.MINUTE, minute);
            }
        }
        if( layoutSelection == 2) {
            calendarEndDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendarEndDate.set(Calendar.MINUTE, minute);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy EE HH:mm");
        setDateText();
    }

    /**
     * Validates the date that the events will be set.
     * @param year, year of the event
     * @param month, month of the event
     * @param dayOfMonth, day of the event
     * @return true, if the date of the event is valid, false, if it is not
     */
    private boolean validateDate(int year, int month, int dayOfMonth) {
        Context context = getContext();
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        android.icu.util.Calendar set = android.icu.util.Calendar.getInstance();

        set.set(Calendar.YEAR, year);
        set.set(Calendar.MONTH, month);
        set.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        if ( ( today.compareTo(set) > 0 ) && layoutSelection == 0 ) {
            Toast.makeText(context, "Event Date Cannot be Selected Before Today", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateEndDate(int year, int month, int dayOfMonth) {
        Context context = getContext();
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        android.icu.util.Calendar set = android.icu.util.Calendar.getInstance();

        if( ( year == calendarStartDate.get(Calendar.YEAR) ) && ( month == calendarStartDate.get(Calendar.MONTH) ) && ( dayOfMonth == calendarStartDate.get(Calendar.DAY_OF_MONTH) ) ) {
            return true;
        }
        Toast.makeText(context, "End Date Has to be on the Same Day as Start Date", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * Controls if two days are the same.
     * @return true if they are, false if not
     */
    private boolean isOnSameDay( final android.icu.util.Calendar calendar1, final android.icu.util.Calendar calendar2 ) {
        if( ( calendar1.get( Calendar.MONTH ) == calendar2.get( Calendar.MONTH ) ) && ( calendar1.get( Calendar.YEAR ) == calendar2.get( Calendar.YEAR ) ) && ( calendar1.get( Calendar.DAY_OF_MONTH ) == calendar2.get( Calendar.DAY_OF_MONTH ) ) ) {
            return true;
        }
        return false;
    }

    /**
     * Validates the starting time of the event.
     * @param hourOfDay, hour of the beginning of the event
     * @param minute, minute of the beginning of the event
     * @return true, if the beginning is valid, false, if no6
     */
    private boolean validateStartTime(int hourOfDay, int minute) {
        Context context = getContext();
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        android.icu.util.Calendar set = android.icu.util.Calendar.getInstance();

        set.set(Calendar.HOUR_OF_DAY, hourOfDay);
        set.set(Calendar.MINUTE, minute);
        set.set(Calendar.YEAR, calendarStartDate.get(Calendar.YEAR));
        set.set(Calendar.MONTH, calendarStartDate.get(Calendar.MONTH));
        set.set(Calendar.DAY_OF_MONTH, calendarStartDate.get(Calendar.DAY_OF_MONTH));

        if ( today.compareTo(set) > 0  && layoutSelection == 1) {
            //calendarStartDate = android.icu.util.Calendar.getInstance();
            //calendarEndDate = android.icu.util.Calendar.getInstance();
            Toast.makeText(context, "Start Time Cannot be Selected Before Current Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validates the ending time of the event.
     * @param hourOfDay, hour of the end of the event
     * @param minute, minute of the end of the event
     * @return true, if the end is valid, false, if no6
     */
    private boolean validateEndTime(int hourOfDay, int minute) {
        Context context = getContext();
        final android.icu.util.Calendar today = android.icu.util.Calendar.getInstance();
        android.icu.util.Calendar set = android.icu.util.Calendar.getInstance();

        set.set(Calendar.HOUR_OF_DAY, hourOfDay);
        set.set(Calendar.MINUTE, minute);
        set.set(Calendar.YEAR, calendarEndDate.get(Calendar.YEAR));
        set.set(Calendar.MONTH, calendarEndDate.get(Calendar.MONTH));
        set.set(Calendar.DAY_OF_MONTH, calendarEndDate.get(Calendar.DAY_OF_MONTH));

        if ( set.compareTo( calendarStartDate ) < 0  && layoutSelection == 2) {
            //calendarEndDate = android.icu.util.Calendar.getInstance();
            Toast.makeText(context, "End Time Cannot be Selected Before Start Time", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Starts feedback animation.
     * @param object, a linear layout object
     */
    private void startFeedBackAnimation( LinearLayout object){
        ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#DFF6F9")), new ColorDrawable(Color.parseColor("#ffffff"))};
        TransitionDrawable trans = new TransitionDrawable(color);
        object.setBackground(trans);
        trans.startTransition(300); // duration 3 seconds
    }

    /**
     * Controls if the event is appropriate to be hosted.
     * @return true if it is appropriate, false if not
     */
    private boolean isAppropriate() {
        Context context = getContext();

        if( isThereOverlap( calendarStartDate.get(Calendar.DAY_OF_MONTH), calendarStartDate.get(Calendar.MONTH), calendarStartDate.get(Calendar.YEAR), calendarStartDate.get(Calendar.HOUR_OF_DAY), calendarStartDate.get(Calendar.MINUTE), calendarEndDate.get(Calendar.HOUR_OF_DAY), calendarEndDate.get(Calendar.MINUTE) ) ) {
            Toast.makeText(context, "The event shouldn't overlap with another attended event!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( DatabaseManager.getInstance().getClubAdministrations().size() == 0) {
            Toast.makeText(context, "You have to be a club admin to host a club event!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( eventName.getEditText().getText().length() < 4 ) {
            Toast.makeText(context, "Event name should have at least 4 character!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( organizerNameSelection == -1 ) {
            Toast.makeText(context, "You have to choose a club to host a club event!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if( location.getEditText().getText().length() == 0 ) {
            Toast.makeText(context, "Location description cannot be empty!", Toast.LENGTH_SHORT);
            return false;
        }

        try{
            quotaInt = Integer.parseInt(quota.getEditText().getText().toString());
        }catch(NumberFormatException e){
            Toast.makeText(context, "Quota should be an integer!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Controls if there is an overlap between the event to be hosted and the other events of the user.
     * @param eventDay, day of the event
     * @param eventMonth, month of the event
     * @param eventYear, year of the event
     * @param startHour, start hour of the event
     * @param startMinute, start minute of event
     * @param endHour, end hour of the event
     * @param endMinute, end minute of the event
     * @return true if there is an overlap, false if not
     */
    private boolean isThereOverlap( int eventDay, int eventMonth, int eventYear, int startHour, int startMinute, int endHour, int endMinute ) {
        Context context = getContext();
        DatabaseManager databaseManager = DatabaseManager.getInstance();

        for( int i = 0; i < databaseManager.getAttendedEventsAtDay( eventDay, eventMonth + 1, eventYear ).size(); i++ ) {
            if( doesOverlap( startHour, startMinute, endHour, endMinute, databaseManager.getAttendedEventsAtDay( eventDay, eventMonth + 1, eventYear ).get( i ) ) ) {
                return true;
            }
        }
        return false;
    }

    /**
     * Controls if there is an overlap between the event to be hosted and another event that is on the same day.
     * @param startHour, start hour of the event
     * @param startMinute, start minute of event
     * @param endHour, end hour of the event
     * @param endMinute, end minute of the event
     * @param event, event to be compared
     * @return true if there is an overlap, false if not
     */
    private boolean doesOverlap(  int startHour, int startMinute, int endHour, int endMinute, Event event ) {
        int start1 = startHour * 60 + startMinute;
        int start2 = event.getStartHour() * 60 + event.getStartMinute();
        int end1 = endHour * 60 + endMinute;
        int end2 = event.getEndHour() * 60 + event.getEndMinute();

        if( end1 < start2 || end2 < start1   ) {
            return false;
        }

        return true;
    }
}