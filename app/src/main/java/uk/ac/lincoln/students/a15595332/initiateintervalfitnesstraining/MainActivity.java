package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



import es.dmoral.toasty.Toasty;


public class MainActivity extends AppCompatActivity implements TimersFragment.OnFragmentInteractionListener, UserFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener  {

    private SQLiteDatabaseHandler db;
    private TimersAdapter mAdapter;
    public  CustomItemClickListener mListener;
    public SQLiteDatabase database;
    private FirebaseAuth mAuth;
    public TextView mTitle;

    // Ints
    public int prepare;
    public int workout;
    public int rest;
    public int cycles;
    public int sets;
    public int setRest;
    public int coolDown;
    public int p;  // Postion in cardview.


    public RecyclerView mRecyclerView;
    private Timers timers;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    private TextView mTextMessage;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {

            startActivity(new Intent(MainActivity.this, loginUserActivity.class));
        }

    }


    @Override
    public void onFragmentInteraction(Uri uri){

    }

    /**************************************************************************************
     * Title: Bottom Navigation Bar source code
     * Author: Ravi Tamada
     * Date: DECEMBER 29, 2017
     * Availability: https://www.androidhive.info/2017/12/android-working-with-bottom-navigation/
     *
    ***************************************************************************************/

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;



            switch (item.getItemId()) {
                case R.id.navigation_timers:


                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    selectedFragment = TimersFragment.newInstance("A","B");
                    transaction.replace(R.id.frame_layout, selectedFragment);

                    transaction.commit();
                    return true;

                case R.id.navigation_user:

                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = UserFragment.newInstance("A","B");
                    transaction2.replace(R.id.frame_layout, selectedFragment);
                    transaction2.commit();
                    return true;

                case R.id.navigation_journal:


                    FragmentTransaction transaction3 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = JournalFragment.newInstance("A","B");
                    transaction3.replace(R.id.frame_layout, selectedFragment);
                    transaction3.commit();
                    return true;


            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Prevent orientation layout change.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Grab current user.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if (currentUser != null) {
            // Loading profile image

            ImageView profileImage = findViewById(R.id.profile_image);
            Uri profilePicUrl = currentUser.getPhotoUrl();
            if (profilePicUrl != null) {
                Glide.with(this).load(profilePicUrl)
                        .into(profileImage);
            }

        }
        // Load Libraries for SQLCipher.
        SQLiteDatabase.loadLibs(this);

        // Set the main fragment to load on start
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TimersFragment.newInstance("A","B"));
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /**************************************************************************************
         * Title: Tool Bar source code
         * Author: Unknown
         * Date: Unknown
         * Availability: https://guides.codepath.com/android/Using-the-App-Toolbar
         *
         ***************************************************************************************/

        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view

        // Setting custom tool bar title.
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Initiate Interval");


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);

        return true;
    }



    // When the plus button has been pressed.
    public void plusTimer(MenuItem item){

        Intent i = new Intent(this, NewTimer.class);
        startActivityForResult(i, 1);

    }


    /**************************************************************************************
     * Title: Activity Result source code
     * Author: Bill Phillips, Chris Stewart, Brian Hardy and Kristin Marsicano
     * Date: October 2016
     * Availability: Android Programming: The Big Nerd Ranch Guide (2nd Edition)
     *
     ***************************************************************************************/

    // Data returned when the done button is pressed in the new timer activity.
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // When a new timer has been created.
        if (requestCode == 1) {
           if (resultCode == RESULT_OK) {

               String workoutTitleValue = data.getStringExtra("workoutTitleValue");
               String prepareValue = data.getStringExtra("prepareValue");
               String workoutTimeValue = data.getStringExtra("workoutTimeValue");
               String restValue = data.getStringExtra("restValue");
               String cyclesValue = data.getStringExtra("cyclesValue");
               String setsValue = data.getStringExtra("setsValue");
               String setRestValue = data.getStringExtra("setRestValue");
               String coolDownVaue = data.getStringExtra("coolDownValue");

               // Exception handling for the conversion of Strings to ints.
               try {
                   prepare = Integer.parseInt(prepareValue);
                   workout = Integer.parseInt(workoutTimeValue);
                   rest = Integer.parseInt(restValue);
                   cycles = Integer.parseInt(cyclesValue);
                   sets = Integer.parseInt(setsValue);
                   setRest = Integer.parseInt(setRestValue);
                   coolDown = Integer.parseInt(coolDownVaue);

               }

               catch (Exception e) {
                   StyleableToast.makeText(MainActivity.this, "Error, please try again.", Toast.LENGTH_LONG, R.style.warningtoast).show();
                   Log.e("APP", "exception", e);
               }

               // Total workout time calculations.
               int total = prepare + ((((workout+rest)*cycles)+setRest)*sets) + coolDown;

               int minutes = (total % 3600) / 60;
               int seconds = total % 60;

               String totalTimeValue = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);

               double minDec = minutes + (seconds / 60.00);
               double calories = minDec*15.00;

               int roundedCalories = (int) Math.rint(calories);

               String caloriesBurntValue = String.valueOf(roundedCalories);

               // create our sqlite helper class
               db = new SQLiteDatabaseHandler(this);

               // create new timer from returned data.
               Timers timer1 = new Timers(workoutTitleValue, prepareValue, workoutTimeValue, restValue, cyclesValue, 0, setsValue, setRestValue, coolDownVaue, totalTimeValue, caloriesBurntValue );

               // add them
               db.addTimer(timer1);


               List<Timers> timersList = db.allTimers();

               // Initialize the Timers adapter.
               mAdapter = new TimersAdapter(this, R.layout.timers_row, timersList, mListener);


               // Insert the new timer at the end of the list into the recycler view.
               int position = timersList.size();
               mAdapter.notifyItemInserted(position);


               // Refresh fragment.
               TimersFragment fragment = new TimersFragment();
               android.support.v4.app.FragmentTransaction fragmentTransaction =
                       getSupportFragmentManager().beginTransaction();
               fragmentTransaction.replace(R.id.frame_layout, fragment);
               fragmentTransaction.commitAllowingStateLoss();


           }// End of if resultCode.
       }// End of if requestCode.

       // When a timer has been updated.
       if (requestCode == 2) {
           if (resultCode == RESULT_OK) {

               String workoutTitleValue = data.getStringExtra("workoutTitleValue");
               String prepareValue = data.getStringExtra("prepareValue");
               String workoutTimeValue = data.getStringExtra("workoutTimeValue");
               String restValue = data.getStringExtra("restValue");
               String cyclesValue = data.getStringExtra("cyclesValue");
               String setsValue = data.getStringExtra("setsValue");
               String setRestValue = data.getStringExtra("setRestValue");
               String coolDownVaue = data.getStringExtra("coolDownValue");

               // Exception handling for the conversion of Strings to ints.
               try {
                   prepare = Integer.parseInt(prepareValue);
                   workout = Integer.parseInt(workoutTimeValue);
                   rest = Integer.parseInt(restValue);
                   cycles = Integer.parseInt(cyclesValue);
                   sets = Integer.parseInt(setsValue);
                   setRest = Integer.parseInt(setRestValue);
                   coolDown = Integer.parseInt(coolDownVaue);

               }

               catch (Exception e) {
                   StyleableToast.makeText(MainActivity.this, "Error, please try again.", Toast.LENGTH_LONG, R.style.warningtoast).show();
                   Log.e("APP", "exception", e);
               }

               // Recalculate the workout total time.
               int total = prepare + ((((workout+rest)*cycles)+setRest)*sets) + coolDown;
               int minutes = (total % 3600) / 60;
               int seconds = total % 60;

               String totalTimeValue = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);

               // Minutes in decimal format.
               double minDec = 0;

               if (minutes > 0) {
                   minDec = minutes + (seconds / 60);
               }

               else
               {
                   minDec = total/60;
               }

               double calories = minDec*15;

               String caloriesBurntValue = String.valueOf(calories);

               int id = data.getIntExtra("sendId", 0);
               int pos = data.getIntExtra("sendPos", 0);

               // Add new timer to the list of timers

               if (id != 0) {

                   // create our sqlite helper class
                   db = new SQLiteDatabaseHandler(this);

                   // set timer from new values
                   Timers timer1 = new Timers(workoutTitleValue, prepareValue, workoutTimeValue, restValue, cyclesValue, id, setsValue, setRestValue, coolDownVaue, totalTimeValue, caloriesBurntValue);

                   // update the timer.
                   db.updateTimer(timer1);


                   // Refresh fragment.
                   TimersFragment fragment = new TimersFragment();
                   android.support.v4.app.FragmentTransaction fragmentTransaction =
                           getSupportFragmentManager().beginTransaction();
                   fragmentTransaction.replace(R.id.frame_layout, fragment);
                   fragmentTransaction.commitAllowingStateLoss();


               }

           }


       }

        // Start time of workout.
       if (requestCode == 3) {
           if (resultCode == RESULT_OK) {

               int id = data.getIntExtra("sendId", 0);
               int calTimeStart = data.getIntExtra("sendCalTime",0);
               int calTimeMinStart = data.getIntExtra("sendCalTimeMin",0);

               Intent intent = new Intent(this, finishedActivity.class);
               intent.putExtra("sendId", id);
               intent.putExtra("sendCalTime", calTimeStart);
               intent.putExtra("sendCalTimeMin", calTimeMinStart);

               startActivityForResult(intent, 4);


           }// End of result ok.

       }// End of if 3

       // When permission for Google fit has been given.
       if (requestCode == 1234) {

           if (resultCode == RESULT_OK) {
               Log.d("Main Request Code", "1234");

               // Calls method in User fragment.
               UserFragment fragment = (UserFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout);
               fragment.readHistoryData();

           }
       }

   }// end of activity result method.


}// End of class.

