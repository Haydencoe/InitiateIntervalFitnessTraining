package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements TimersFragment.OnFragmentInteractionListener, RunningFragment.OnFragmentInteractionListener, JournalFragment.OnFragmentInteractionListener  {

    private SQLiteDatabaseHandler db;

    private TimersAdapter mAdapter;

    public RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;

    private TextView mTextMessage;

    public  int p;

    public  CustomItemClickListener mListener;

    public SQLiteDatabase database;


    private Timers timers;


    public int prepare;
    public int workout;
    public int rest;
    public int cycles;
    public int sets;
    public int setRest;
    public int coolDown;

    @Override
    public void onFragmentInteraction(Uri uri){

    }


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

                case R.id.navigation_running:

                    FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                    selectedFragment = RunningFragment.newInstance("A","B");
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

        // Load Libraries for SQLCipher.
        SQLiteDatabase.loadLibs(this);


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, TimersFragment.newInstance("A","B"));
        transaction.commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        // Find the toolbar view inside the activity layout
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);


        // Remove default title text
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // Get access to the custom title view
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Initiate Interval");





        //InitializeSQLCipher();
    }


    /*
    private void InitializeSQLCipher() {

        File databaseFile = getDatabasePath("TimersDB");
        databaseFile.mkdirs();
        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "secret", null);

    }*/




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





    // Data returned when the done button is pressed in the new timer activity.
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == 1) {
           if (resultCode == RESULT_OK) {

               //Toast toast =  Toast.makeText(this, "Code 1", Toast.LENGTH_SHORT); toast.show();

               String workoutTitleValue = data.getStringExtra("workoutTitleValue");
               String prepareValue = data.getStringExtra("prepareValue");
               String workoutTimeValue = data.getStringExtra("workoutTimeValue");
               String restValue = data.getStringExtra("restValue");
               String cyclesValue = data.getStringExtra("cyclesValue");
               String setsValue = data.getStringExtra("setsValue");
               String setRestValue = data.getStringExtra("setRestValue");
               String coolDownVaue = data.getStringExtra("coolDownValue");


               try {
                   prepare = Integer.parseInt(prepareValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   workout = Integer.parseInt(workoutTimeValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   rest = Integer.parseInt(restValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   cycles = Integer.parseInt(cyclesValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   sets = Integer.parseInt(setsValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   setRest = Integer.parseInt(setRestValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   coolDown = Integer.parseInt(coolDownVaue);
               } catch (NumberFormatException nfe) {

               }

               int total = prepare + ((((workout+rest)*cycles)+setRest)*sets) + coolDown;

               int minutes = (total % 3600) / 60;
               int seconds = total % 60;

              String totalTimeValue = String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);


               double minDec = minutes + (seconds / 60.00);


               double calories = minDec*15.00;


               int roundedCalories = (int) Math.rint(calories);

                String caloriesBurntValue = String.valueOf(roundedCalories);


               // Add new timer to the list of timers
               // timersList.add(new Timers(workoutTitleValue , prepareValue, workoutTimeValue, "20", "3"));


               // create our sqlite helper class
               db = new SQLiteDatabaseHandler(this);
               // create new timer
               Timers timer1 = new Timers(workoutTitleValue, prepareValue, workoutTimeValue, restValue, cyclesValue, 0, setsValue, setRestValue, coolDownVaue, totalTimeValue, caloriesBurntValue );

               // add them
               db.addTimer(timer1);


               List<Timers> timersList = db.allTimers();

               // Initialize the Timers adapter.
               mAdapter = new TimersAdapter(this, R.layout.timer_row, timersList, mListener);


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


       if (requestCode == 2) {
           if (resultCode == RESULT_OK) {

               //Toast toast =  Toast.makeText(this, "Code 2", Toast.LENGTH_SHORT); toast.show();

               String workoutTitleValue = data.getStringExtra("workoutTitleValue");
               String prepareValue = data.getStringExtra("prepareValue");
               String workoutTimeValue = data.getStringExtra("workoutTimeValue");
               String restValue = data.getStringExtra("restValue");
               String cyclesValue = data.getStringExtra("cyclesValue");
               String setsValue = data.getStringExtra("setsValue");
               String setRestValue = data.getStringExtra("setRestValue");
               String coolDownVaue = data.getStringExtra("coolDownValue");


               try {
                   prepare = Integer.parseInt(prepareValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   workout = Integer.parseInt(workoutTimeValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   rest = Integer.parseInt(restValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   cycles = Integer.parseInt(cyclesValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   sets = Integer.parseInt(setsValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   setRest = Integer.parseInt(setRestValue);
               } catch (NumberFormatException nfe) {

               }

               try {
                   coolDown = Integer.parseInt(coolDownVaue);
               } catch (NumberFormatException nfe) {

               }

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
               // timersList.add(new Timers(workoutTitleValue , prepareValue, workoutTimeValue, "20", "3"));

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


       if (requestCode == 3) {
           if (resultCode == RESULT_OK) {



               int id = data.getIntExtra("sendId", 0);
               Toast toast =  Toast.makeText(this, "Code 3" +id, Toast.LENGTH_SHORT); toast.show();

               Intent intent = new Intent(this, finishedActivity.class);
               intent.putExtra("sendId", id);
               startActivityForResult(intent, 4);



           }// End of result ok.

       }// End of if 3


   }// end of activity result method.

}// End of class.







/*

 //timersList.add(new Timers(R.drawable.ic_add_black_24dp, "After Earth" , "2013"));

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;


        // Define a layout for RecyclerView
        mLayoutManager = new GridLayoutManager(mContext,3);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Initialize a new instance of RecyclerView Adapter instance
        tAdapter = new TimersAdapter(mContext,timersList);

        // Set the adapter for RecyclerView
        mRecyclerView.setAdapter(tAdapter);


        //tAdapter = new TimersAdapter(this,timersList);
        //listView.setAdapter(tAdapter);

*/
//  mLayoutManager = new GridLayoutManager(mContext,3);
// mRecyclerView.setLayoutManager(mLayoutManager);

/*
        // **** Layout for the timer's list *************************************************************

        //ArrayList<Timers> timersList = new ArrayList<>();

        timersList.add(new Timers("Push ups" ,"10", "20", "10", "8"));
        timersList.add(new Timers("Sit ups" , "12", "30", "15", "5"));
        timersList.add(new Timers("Burpees" , "15", "45", "20", "3"));


        //mRecyclerView = (RecyclerView) findViewById(R.id.listings_view);

        // Initialize the Timers adapter.
        mAdapter = new TimersAdapter(this, R.layout.timer_row, timersList);

        // Initialize ItemAnimator, LayoutManager and ItemDecorators


        // Setup our RecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.listings_view);



        // For performance, tell OS RecyclerView won't change size
        mRecyclerView.setHasFixedSize(true);

        // Set the LayoutManager

        // Set the ItemDecorators

        // Attach the adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);


        // Initialize ItemAnimator, LayoutManager and ItemDecorators

        // Sets the view to have two columns in a grid fashion.
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);

        int verticalSpacing = 2;
        VerticalSpaceItemDecorator itemDecorator =
                new VerticalSpaceItemDecorator(verticalSpacing);
        ShadowVerticalSpaceItemDecorator shadowItemDecorator =
                new ShadowVerticalSpaceItemDecorator(this, R.drawable.drop_shadow);


        // Set the LayoutManager
        mRecyclerView.setLayoutManager(layoutManager);

        // Set the ItemDecorators
        mRecyclerView.addItemDecoration(shadowItemDecorator);
        mRecyclerView.addItemDecoration(itemDecorator);

*/


/*
    public void showMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                switch (item.getItemId()) {
                    case R.id.editItem:
                        // do your code
                        return true;
                    case R.id.deleteItem:
                        // do your code
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.show();

    }*/