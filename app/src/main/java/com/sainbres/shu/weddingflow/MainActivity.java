package com.sainbres.shu.weddingflow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sainbres.shu.weddingflow.Fragments.BudgetFragment;
import com.sainbres.shu.weddingflow.Fragments.GuestListFragment;
import com.sainbres.shu.weddingflow.Fragments.HomeFragment;
import com.sainbres.shu.weddingflow.Fragments.OrganizeFragment;
import com.sainbres.shu.weddingflow.Fragments.ShareFragment;
import com.sainbres.shu.weddingflow.Models.WeddingEvent;
import com.sainbres.shu.weddingflow.Models.WeddingEvent_Table;

import java.io.File;
import java.net.URI;

public class MainActivity extends AppCompatActivity  {

    private SharedPreferences SharedPrefs;
    private SharedPreferences.Editor Editor;

    private String homeImagePath;
    private Uri homeImageUri = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Editor = SharedPrefs.edit();

        int userId = SharedPrefs.getInt(getString(R.string.SP_UserId), -1);
        if (userId == -1){
            // default -> do nothing
        } else {
            WeddingEvent event = SQLite.select().from(WeddingEvent.class).where(WeddingEvent_Table.UserId.eq(userId)).querySingle();
            if (event == null){
                Intent intent = new Intent(getApplicationContext(), SetupWeddingEventActivity.class);
                startActivity(intent);
                finish();
            } else {
                Editor.putInt(getString(R.string.SP_EventId), event.getEventId());
                Editor.commit();
                if(event.getImage() != null){
                    homeImageUri = Uri.fromFile(new File(event.getImage(), "fileName.jpg"));
                    homeImagePath = event.getImage();
                }

            }
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Toolbar toolbar = findViewById(R.id.main_Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorWhite));

        Bundle extras = getIntent().getExtras();

        if(extras != null){ //Used to set correct fragment on MainActivity open
            switch (extras.getString("fragment", "home")){
                case "home":{
                    //Fragment homeFragment = new HomeFragment();
                    //homeFragment.setArguments(homeBundle());
                    navigation.setSelectedItemId(R.id.action_home);
                    //loadFragment(homeFragment);
                }
                case "budget":{
                    //Fragment budgetFragment = new BudgetFragment();
                    navigation.setSelectedItemId(R.id.action_budget);
                    //loadFragment(budgetFragment);
                }
            }
        }
        else { //Default to load HomeFragment
            Fragment defaultFrag = new HomeFragment();
            defaultFrag.setArguments(homeBundle());
            loadFragment(defaultFrag);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.edit_wedding:
                intent = new Intent(this, EditWeddingEventActivity.class);
                startActivity(intent);
                return true;
            case R.id.help:
                intent = new Intent(this, HelpActivity.class);

                startActivity(intent);
                return true;
            case R.id.logout:
                Editor.putBoolean(getString(R.string.SP_StayLoggedIn), false);
                Editor.commit();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.action_home:
                    fragment = new HomeFragment();
                    fragment.setArguments(homeBundle());
                    getSupportActionBar().setTitle("Home");
                    loadFragment(fragment);
                    return true;

                case R.id.action_organize:
                    fragment = new OrganizeFragment();
                    getSupportActionBar().setTitle("Organize");
                    loadFragment(fragment);
                    return true;

                case R.id.action_budget:
                    fragment = new BudgetFragment();
                    getSupportActionBar().setTitle("Budget");
                    loadFragment(fragment);
                    return true;

                case R.id.action_guest_list:
                    fragment = new GuestListFragment();
                    getSupportActionBar().setTitle("Guests");
                    loadFragment(fragment);
                    return true;
                case R.id.action_share:
                    fragment = new ShareFragment();
                    getSupportActionBar().setTitle("Share");
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private Bundle homeBundle() {
        Bundle homeBundle = new Bundle();
        if (homeImageUri != null){
            homeBundle.putString("uri", homeImageUri.toString());
            homeBundle.putString("imagePath", homeImagePath);
        }

        return homeBundle;
    }
}


   