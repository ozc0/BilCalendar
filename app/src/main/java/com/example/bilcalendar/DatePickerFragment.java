package com.example.bilcalendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

/*
    A fragment controller class to enable the user to choose year, month, day, hour and minutes.

    @author Team of Ministler
    @date 12/25/2019
 */
public class DatePickerFragment extends DialogFragment {

    // Properties
    private DatePickerDialog.OnDateSetListener dateSetListener; // listener object to get calling fragment listener
    DatePickerDialog myDatePicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final android.icu.util.Calendar c = android.icu.util.Calendar.getInstance();
        int year = c.get(android.icu.util.Calendar.YEAR);
        int month = c.get(android.icu.util.Calendar.MONTH);
        int day = c.get(android.icu.util.Calendar.DAY_OF_MONTH);
        dateSetListener = (DatePickerDialog.OnDateSetListener) getTargetFragment(); // getting passed fragment
        myDatePicker = new DatePickerDialog(getActivity(), R.style.my_dialog_theme, dateSetListener, year, month, day); // DatePickerDialog gets callBack listener as 2nd parameter
        // Create a new instance of DatePickerDialog and return it
        return myDatePicker;
    }
}