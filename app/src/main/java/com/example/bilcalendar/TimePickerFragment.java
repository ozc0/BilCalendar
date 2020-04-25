package com.example.bilcalendar;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
/**
 * A class used to choose a time, hour and minute, for an event.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class TimePickerFragment extends DialogFragment {
    private TimePickerDialog.OnTimeSetListener timeSetListener; // listener object to get calling fragment listener
    TimePickerDialog myTimePicker;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        timeSetListener = (TimePickerDialog.OnTimeSetListener) getTargetFragment();
        myTimePicker = new TimePickerDialog(getActivity(), R.style.my_dialog_theme, timeSetListener, hour, minute, true);

        return myTimePicker;
    }
}