package com.example.bilcalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;


import com.example.bilcalendar.AccountInformationPageActivity;
import com.example.bilcalendar.ExpandableListForHostedEvents;
import com.example.bilcalendar.HostEvent;
import com.example.bilcalendar.MainMenu;
import com.example.bilcalendar.MainMenuFragment;
import com.example.bilcalendar.R;
import com.example.bilcalendar.Settings;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import database.DatabaseManager;
import library.MainActivity;

/**
 * Flow activity which contains the flow view fragment
 *
 * @author the team of Ministler
 */
public class FlowActivity extends AppCompatActivity{

    // Properties
    private FloatingActionButton hostEventButton;
    private Calendar currentDate;
    private Toolbar toolbar;
    private ImageButton changeViewButton;
    private LinearLayout changeViewNavigator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // initialise variables
        currentDate = MainMenu.currentDate;
        toolbar = findViewById(R.id.toolbar);

        // Set the toolbar of activity
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // set the date in toolbar
        setDate();
        toolbar.setTitleTextColor( Color.parseColor("#000000" ));
        setSupportActionBar(toolbar);


        //initialise change view navigator
        changeViewNavigator = (LinearLayout) findViewById(R.id.change_view_navigator);

        //initialise change view button
        changeViewButton = (ImageButton) findViewById( R.id.change_view_button );
        changeViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // On click change the visisbility of change view navigator
                if( changeViewNavigator.getVisibility() == View.GONE ){
                    changeViewNavigator.setVisibility( View.VISIBLE );
                }
                else{
                    changeViewNavigator.setVisibility( View.GONE );
                }
            }
        });

        setButtonListeners();

        setComponents();

        //set fragment of activity
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RecyclerFlowFragment(
                currentDate.get(Calendar.DAY_OF_MONTH),currentDate.get(Calendar.MONTH),currentDate.get(Calendar.YEAR)) ).commit();

        // initialise database manager
        DatabaseManager databaseManager = DatabaseManager.getInstance();

    }

    /**
     * Method for setting components
     */
    private void setComponents() {
        hostEventButton = findViewById(R.id.host_event_button);
        hostEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FlowActivity.this, HostEvent.class));
            }
        });

    }

    /**
     * Set the date of the toolbar
     */
    private void setDate() {

        final String[] monthName = {"January", "February",
                "March", "April", "May", "June", "July",
                "August", "September", "October", "November",
                "December"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        toolbar.setTitle( monthName[ currentDate.get( currentDate.MONTH) ] + " " + sdf.format( currentDate.getTime()));

    }

    /**
     * Make an animation for object
     * @param object
     */
    protected void startFeedBackAnimation(LinearLayout object){
        ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#DFF6F9")), new ColorDrawable(Color.parseColor("#ffffff"))};
        TransitionDrawable trans = new TransitionDrawable(color);
        object.setBackground(trans);
        trans.startTransition(300);// duration 3 seconds
    }

    /**
     * Set navigator button listeners
     */
    protected void setButtonListeners(){
        final LinearLayout layoutGoToMonth = findViewById(R.id.go_to_monthly_view);

        layoutGoToMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Go to month
                startFeedBackAnimation( layoutGoToMonth );
                startActivity(new Intent(FlowActivity.this, MainMenu.class));

            }
        });

        final LinearLayout layoutGoToWeek = findViewById(R.id.go_to_weekly_view);

        layoutGoToWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Go to week
                startFeedBackAnimation( layoutGoToWeek );
                startActivity(new Intent(FlowActivity.this, WeeklyActivity.class));

            }
        });

        final LinearLayout layoutGoToDay = findViewById(R.id.go_to_daily_view);

        layoutGoToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Go to Day
                startFeedBackAnimation( layoutGoToDay );
                startActivity(new Intent(FlowActivity.this, DailyActivity.class));
            }
        });

        final LinearLayout layoutGoToFlow = findViewById(R.id.go_to_flow_view);

        layoutGoToFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// Go to flow
                startFeedBackAnimation( layoutGoToFlow );
            }
        });
    }

}