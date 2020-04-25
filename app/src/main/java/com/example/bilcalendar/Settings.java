package com.example.bilcalendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * A class used to display settings page.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class Settings extends AppCompatActivity {

    private LinearLayout layoutForNotification;
    private LinearLayout layoutForRingtone;
    private LinearLayout layoutForVibrate;
    private LinearLayout layoutForGlobalEvents;
    private LinearLayout layoutForDarkMode;

    private Context thisInstance = this;

    private Switch switchForNotifications;
    private Switch switchForVibrate;
    private Switch switchForGlobalEvents;
    private Switch switchForDarkMode;

    private Spinner spinnerForRingtone;

    private Map<String, String> ringtonesOnThePhone;
    private Ringtone defaultRingtone;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        toolbar = findViewById(R.id.toolbar_for_settings);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ringtonesOnThePhone = getNotifications();

        List ringtoneNames = new ArrayList(ringtonesOnThePhone.keySet());

        ArrayList<String> ringtoneNamess = new ArrayList<>(ringtoneNames);

        SharedPreferences sharedPref = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        String defaultRingtoneName = sharedPref.getString("defaultRingtone", ringtoneNamess.get(0));
        defaultRingtone = RingtoneManager.getRingtone(thisInstance, Uri.parse(Uri.decode(ringtonesOnThePhone.get(defaultRingtoneName))));

        createObjects();

        setUpSpinner();

        setLayouts();

        setChecks();
    }

    /**
     * Creates objects displayed in the settings page.
     */
    private void createObjects() {
        layoutForNotification = findViewById(R.id.layout_for_notifications);
        layoutForRingtone = findViewById(R.id.layout_for_ringtone);
        layoutForVibrate = findViewById(R.id.layout_for_vibrate);
        layoutForGlobalEvents = findViewById(R.id.layout_for_global_events);
        layoutForDarkMode = findViewById(R.id.layout_for_dark_mode);

        switchForNotifications = findViewById(R.id.switch_button_for_notifications);
        switchForVibrate = findViewById(R.id.switch_button_for_vibrate);
        switchForGlobalEvents = findViewById(R.id.switch_button_for_global_events);
        switchForDarkMode = findViewById(R.id.switch_button_for_dark_mode);

        spinnerForRingtone = findViewById(R.id.spinner_for_choose_notifications);
    }

    /**
     * Sets layouts of the fields used in the settings page.
     */
    private void setLayouts() {

        layoutForNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCloseNotifications();
                adjustSituationOfSettings();
            }
        });

        layoutForRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRingtone();
                adjustSituationOfSettings();
            }
        });

        layoutForVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello");
                openCloseVibrate();
                adjustSituationOfSettings();
            }
        });

        layoutForGlobalEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCloseGlobalEvents();
                adjustSituationOfSettings();
            }
        });

        layoutForDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCloseDarkMode();
                adjustSituationOfSettings();
            }
        });

    }

    /**
     * Turns notifications on or off based on user input.
     */
    private void openCloseNotifications() {
        if (switchForNotifications.isChecked()) {
            switchForNotifications.setChecked(false);
        } else {
            switchForNotifications.setChecked(true);
        }
    }

    /**
     * Turns vibration on or off based on user input.
     */
    private void openCloseVibrate() {
        if (switchForVibrate.isChecked()) {
            switchForVibrate.setChecked(false);
        } else {
            switchForVibrate.setChecked(true);
        }
    }

    /**
     * Turns global events on or off based on user input.
     */
    private void openCloseGlobalEvents() {
        if (switchForGlobalEvents.isChecked()) {
            switchForGlobalEvents.setChecked(false);
        } else {
            switchForGlobalEvents.setChecked(true);
        }
    }

    /**
     * Turns dark mode on or off based on user input.
     */
    private void openCloseDarkMode() {
        if (switchForDarkMode.isChecked()) {
            switchForDarkMode.setChecked(false);
        } else {
            switchForDarkMode.setChecked(true);
        }
    }

    /**
     * Changes ring tone based on user input.
     */
    private void chooseRingtone() {

        spinnerForRingtone.performClick();

    }

    /**
     * Sets up the spinners.
     */
    private void setUpSpinner() {

        spinnerForRingtone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // On selecting a spinner item
                String item = parent.getItemAtPosition(position).toString();

                SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("defaultRingtone", item );
                editor.commit();

                defaultRingtone = RingtoneManager.getRingtone( thisInstance, Uri.parse(Uri.decode( ringtonesOnThePhone.get( item ) )) );

                // Showing selected spinner item
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        List ringtoneNames = new ArrayList(ringtonesOnThePhone.keySet());

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ringtoneNames);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForRingtone.setAdapter(dataAdapter);

        spinnerForRingtone.setSelection( ringtoneNames.indexOf( defaultRingtone.getTitle(this) ) );

    }

    /**
     * Returns the notifications of the user.
     * @return notifications
     */
    public Map<String, String> getNotifications() {
        RingtoneManager manager = new RingtoneManager(this);
        manager.setType(RingtoneManager.TYPE_RINGTONE);
        Cursor cursor = manager.getCursor();

        Map<String, String> list = new HashMap<>();
        while (cursor.moveToNext()) {
            String notificationTitle = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            String notificationUri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(RingtoneManager.ID_COLUMN_INDEX);

            list.put(notificationTitle, notificationUri);
        }

        return list;
    }



    /**
     * Adjusts the situations of settings.
     */
    private void adjustSituationOfSettings() {

        SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isVibrateOpen", switchForVibrate.isChecked() );
        editor.putBoolean("isNotificationsOpen", switchForNotifications.isChecked());
        editor.putBoolean("isDarkModeOpen", switchForDarkMode.isChecked());
        editor.putBoolean("isShowGlobalEventsOpen", switchForGlobalEvents.isChecked());
        editor.commit();

        System.out.println("vibrate");
    }

    /**
     * Sets the default values of the checkboxes.
     */
    private void setChecks() {

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);

        switchForNotifications.setChecked( sharedPreferences.getBoolean( "isNotificationsOpen", false ));
        switchForVibrate.setChecked( sharedPreferences.getBoolean( "isVibrateOpen", false ));
        switchForDarkMode.setChecked( sharedPreferences.getBoolean( "isDarkModeOpen", false ));
        switchForGlobalEvents.setChecked( sharedPreferences.getBoolean( "isShowGlobalEventsOpen", false ));

    }

}