package com.example.bilcalendar;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
/**
 * Class representing a host event page.
 *
 * @author Team of Ministler
 * @date 12/25/2019
 */
public class HostEvent extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_event);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(viewPager);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        setupTabLayout();

    }

    /**
     * Sets the tab layout up.
     */
    private void setupTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon( R.drawable.ic_bookmark);
        tabLayout.getTabAt(1).setIcon( R.drawable.ic_bookmark);
        tabLayout.getTabAt(2).setIcon( R.drawable.ic_bookmark);

        tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#3995ff"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
        tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Object source = tab;
                if( source == tabLayout.getTabAt(0) ) {
                    tabLayout.getTabAt(0).getIcon().setColorFilter(Color.parseColor("#3995ff"), PorterDuff.Mode.SRC_IN);
                    tabLayout.setTabTextColors(Color.parseColor("#a8a8a8"), Color.parseColor("#3995ff"));
                }
                else if( source == tabLayout.getTabAt(1) ) {
                    tabLayout.getTabAt(1).getIcon().setColorFilter(Color.parseColor("#82cc53"), PorterDuff.Mode.SRC_IN);
                    tabLayout.setTabTextColors(Color.parseColor("#a8a8a8"), Color.parseColor("#82cc53"));
                }
                else if( source == tabLayout.getTabAt(2) ) {
                    tabLayout.getTabAt(2).getIcon().setColorFilter(Color.parseColor("#f40000"), PorterDuff.Mode.SRC_IN);
                    tabLayout.setTabTextColors(Color.parseColor("#a8a8a8"), Color.parseColor("#f40000"));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(Color.parseColor("#a8a8a8"), PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * Sets up the view pager.
     * @param viewPager, view pager that will be set up.
     */
    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new HostPersonalEventFragment(), "Personal Event");
        adapter.addFragment(new HostSocialEventFragment(), "Social Events");
        adapter.addFragment(new HostClubEventFragment(), "Club Event");
        viewPager.setAdapter(adapter);
    }

}