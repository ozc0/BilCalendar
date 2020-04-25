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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;

import java.util.Calendar;

import database.DatabaseManager;
import library.MainActivity;
/**
 * Main class of BilCalendar.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class MainMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    // Properties
    private FloatingActionButton hostEventButton;
    public static Calendar currentDate = Calendar.getInstance();
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private ImageButton changeViewButton;
    private LinearLayout changeViewNavigator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        toolbar = findViewById(R.id.toolbar);

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

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener( this );

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainMenuFragment()).commit();

        adjustSituationOfCheckBoxes();

        navigationView.setItemIconTintList(null);
        navigationView.setItemIconSize( 25 );

        View headerView = navigationView.getHeaderView(0);

        DatabaseManager databaseManager = DatabaseManager.getInstance();

        TextView nameNavHeader = headerView.findViewById(R.id.name_nav_header);
        nameNavHeader.setText( databaseManager.getCurUser().getName() );

        TextView mailNavHeader = headerView.findViewById(R.id.mail_nav_header);
        mailNavHeader.setText(databaseManager.getCurUser().getEmail());
    }

    /**
     * Sets up the components of the menu.
     */
    private void setComponents() {
        hostEventButton = findViewById(R.id.host_event_button);
        hostEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenu.this, HostEvent.class));
            }
        });

    }

    /**
     * Sets up the dates in terms of years and months.
     */
    private void setDate() {

            final String[] monthName = {"January", "February",
                    "March", "April", "May", "June", "July",
                    "August", "September", "October", "November",
                    "December"};

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");

        toolbar.setTitle( monthName[ currentDate.get( currentDate.MONTH) ] + " " + sdf.format( currentDate.getTime()));

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        SharedPreferences sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        int itemId = item.getItemId();

           if ( itemId == R.id.sidebar_account ) {
               startActivity(new Intent(MainMenu.this, AccountInformationPageActivity.class));
           }
           else if ( itemId == R.id.sidebar_hosted_events ) {
               startActivity(new Intent( MainMenu.this, ExpandableListForHostedEvents.class ));
           }
           else if ( itemId == R.id.sidebar_settings ) {
               startActivity(new Intent(MainMenu.this, Settings.class));
           }
           else if ( itemId == R.id.sidebar_individual_events ) {
               MaterialCheckBox checkBoxIndividualEvent = findViewById(R.id.sidebar_individual_events);

               if (checkBoxIndividualEvent.isChecked()) {
                   checkBoxIndividualEvent.setChecked(false);
               } else {
                   checkBoxIndividualEvent.setChecked(true);
               }

               editor.putBoolean("isShowIndividualEventsChecked", checkBoxIndividualEvent.isChecked());
               editor.commit();
           }
           else if ( itemId == R.id.sidebar_social_events ) {
               MaterialCheckBox checkBoxSocialEvent = findViewById(R.id.sidebar_social_events);

               if (checkBoxSocialEvent.isChecked()) {
                   checkBoxSocialEvent.setChecked(false);
               } else {
                   checkBoxSocialEvent.setChecked(true);
               }

               editor.putBoolean("isShowSocialEventsChecked", checkBoxSocialEvent.isChecked());
               editor.commit();
           }
           else if ( itemId ==  R.id.sidebar_club_events) {
               MaterialCheckBox checkBoxClubEvents = findViewById(R.id.sidebar_club_events);

               if (checkBoxClubEvents.isChecked()) {
                   checkBoxClubEvents.setChecked(false);
               } else {
                   checkBoxClubEvents.setChecked(true);
               }

               editor.putBoolean("isShowClubEventsChecked", checkBoxClubEvents.isChecked());
               editor.commit();

           }
           else if ( itemId == R.id.sidebar_planned_events ) {

               MaterialCheckBox checkBoxPlannedEvents = findViewById(R.id.sidebar_planned_events);

               if (checkBoxPlannedEvents.isChecked()) {
                   checkBoxPlannedEvents.setChecked(false);
               } else {
                   checkBoxPlannedEvents.setChecked(true);
               }

               editor.putBoolean("isShowPlannedEventChecked", checkBoxPlannedEvents.isChecked());
               editor.commit();

           }
           else if ( itemId == R.id.sidebar_available_events ) {

               MaterialCheckBox checkBoxAvailableEvents = findViewById(R.id.sidebar_available_events);

               if (checkBoxAvailableEvents.isChecked()) {
                   checkBoxAvailableEvents.setChecked(false);
               } else {
                   checkBoxAvailableEvents.setChecked(true);
               }

               editor.putBoolean("isShowAvailableEvents", checkBoxAvailableEvents.isChecked());
               editor.commit();

           }
           else {
               drawer.closeDrawer(GravityCompat.START);
           }

                return false;
      }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Adjusts the checkboxes.
     */
    public void adjustSituationOfCheckBoxes() {

        SharedPreferences sharedPref = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);

        NavigationView view = findViewById(R.id.nav_view);

        Menu menuNav = view.getMenu();

        MenuItem checkBoxIndiviualEvent = menuNav.findItem(R.id.sidebar_individual_events);
        MenuItem checkBoxSocialEvent = menuNav.findItem(R.id.sidebar_social_events);
        MenuItem checkBoxClubEvents = menuNav.findItem(R.id.sidebar_club_events);
        MenuItem checkBoxPlannedEvents = menuNav.findItem(R.id.sidebar_planned_events);
        MenuItem checkBoxAvailableEvents = menuNav.findItem(R.id.sidebar_available_events);

        CheckBox checkBox = (CheckBox) checkBoxIndiviualEvent.getActionView();
        Boolean isChecked = sharedPref.getBoolean("isShowIndividualEventsChecked", false );
        checkBox.setChecked( isChecked );

        checkBox = (CheckBox) checkBoxSocialEvent.getActionView();
        isChecked = sharedPref.getBoolean("isShowSocialEventsChecked", false );
        checkBox.setChecked(isChecked);

        checkBox = (CheckBox) checkBoxClubEvents.getActionView();
        isChecked = sharedPref.getBoolean("isShowClubEventsChecked", false );
        checkBox.setChecked(isChecked);

        checkBox = (CheckBox) checkBoxPlannedEvents.getActionView();
        isChecked = sharedPref.getBoolean("isShowPlannedEventChecked", false );
        checkBox.setChecked(isChecked);

        checkBox = (CheckBox) checkBoxAvailableEvents.getActionView();
        isChecked = sharedPref.getBoolean("isShowAvailableEvents", false );
        checkBox.setChecked(isChecked);


    }

    /**
     * Starts feedback animation
     * @param object, an object of linear layout
     */
    protected void startFeedBackAnimation(LinearLayout object){
        ColorDrawable[] color = {new ColorDrawable(Color.parseColor("#DFF6F9")), new ColorDrawable(Color.parseColor("#ffffff"))};
        TransitionDrawable trans = new TransitionDrawable(color);
        object.setBackground(trans);
        trans.startTransition(300);// duration 3 seconds
    }

    /**
     * Sets up the button listeners.
     */
    protected void setButtonListeners(){
        final LinearLayout layoutGoToMonth = findViewById(R.id.go_to_monthly_view);

        layoutGoToMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeedBackAnimation( layoutGoToMonth );
            }
        });

        final LinearLayout layoutGoToWeek = findViewById(R.id.go_to_weekly_view);

        layoutGoToWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeedBackAnimation( layoutGoToWeek );
                startActivity(new Intent(MainMenu.this, WeeklyActivity.class ));

            }
        });

        final LinearLayout layoutGoToDay = findViewById(R.id.go_to_daily_view);

        layoutGoToDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeedBackAnimation( layoutGoToDay );
                startActivity(new Intent(MainMenu.this, DailyActivity.class ) );

            }
        });

        final LinearLayout layoutGoToFlow = findViewById(R.id.go_to_flow_view);

        layoutGoToFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFeedBackAnimation( layoutGoToFlow );
                startActivity(new Intent(MainMenu.this, ExpandableListForFlowPage.class));

            }
        });
    }



}