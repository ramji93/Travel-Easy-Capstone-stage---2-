package com.example.user.traveleasy;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SearchActivity extends AppCompatActivity {

    private String[] mNav_array;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment mSuppfragment = null;
    private android.app.Fragment mfragment = null;
    private int mposition = 0;

    private boolean settingfragtrans = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         mNav_array = getResources().getStringArray(R.array.nav_items);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, mNav_array));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

//        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,toolbar, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

//                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mDrawerList.bringToFront();
                mDrawerLayout.requestLayout();

//                getSupportActionBar().setTitle("MENU");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        Fragment fragment =  new SearchFragment();

        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container,fragment)
                .commit();


        setTitle(mNav_array[0]);

        mSuppfragment = fragment;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
//
//        mDrawerList.setItemChecked(mposition, true);

        if(position == 1) {


            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();


            if (settingfragtrans)

            {
                getFragmentManager().beginTransaction().remove(mfragment).commit();

                mfragment = null;
                settingfragtrans = false;

        }

            mSuppfragment = new Saved_searches_Fragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,mSuppfragment)
                    .commit();
            mposition = 1;


        }

        else if(position == 2)

        {

            if(settingfragtrans == false)
            {

                android.support.v4.app.FragmentManager supportfragmanager = getSupportFragmentManager();

                supportfragmanager.beginTransaction().remove(mSuppfragment).commit();

                mSuppfragment = null;

            }

            FragmentManager fragmentManager = getFragmentManager();

            mfragment = new SettingsFragment();

            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, mfragment )
                    .commit();

            settingfragtrans = true;

            mposition = 2;
        }

        else
        {

            if(settingfragtrans)

            {

                getFragmentManager().beginTransaction().remove(mfragment).commit();

                 mfragment = null;
                settingfragtrans = false;

            }

            mSuppfragment = new SearchFragment();

            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container,mSuppfragment)
                    .commit();

            mposition = 0;
        }

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(mNav_array[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }



}
