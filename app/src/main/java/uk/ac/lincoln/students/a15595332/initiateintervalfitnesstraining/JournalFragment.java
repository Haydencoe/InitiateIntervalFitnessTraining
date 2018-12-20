package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoast.StyleableToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private JournalsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public Menu menu;
    private SQLiteDatabaseHandler db;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    DatabaseReference refdb;
    public JCustomItemClickListener listener;

    public boolean refreshFlag;

    public List<Journal> journalList;

    private static final String DEBUG_TAG = "NetworkStatus";

    public int p; // position



    public JournalFragment() {
        // Required empty public constructor


        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JournalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JournalFragment newInstance(String param1, String param2) {
        JournalFragment fragment = new JournalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        // Load Libraries for SQLCipher.
        SQLiteDatabase.loadLibs(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            refreshFlag = false;

            journalList = new LinkedList<>();

            loadFromDevice();

        // Setup our RecyclerView

        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.journal_view);


        listener = new JCustomItemClickListener() {
            @Override
            public void onJMenu(View view, int position) {
                //Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                p = position;

                showJMenu(view);

            }

        };


        // Initialize the Journal adapter.
        mAdapter = new JournalsAdapter(getActivity(), R.layout.journal_row, journalList, listener);

        // For performance, tell OS RecyclerView won't change size
        mRecyclerView.setHasFixedSize(true);

        // Attach the adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Sets the view to have one column in a linear fashion.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Set the LayoutManager
        mRecyclerView.setLayoutManager(layoutManager);

        return rootView;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        inflater.inflate(R.menu.my_j_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.cloud:
                Log.i("MENU", "Cloud option selected");
                journalList.clear();
               // mAdapter.notifyDataSetChanged();
                loadFromCloud();
                break;
            case R.id.device:
                Log.i("MENU", "Device option selected");
                journalList.clear();
                //mAdapter.notifyDataSetChanged();
                refreshFlag = true;
                loadFromDevice();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void showJMenu(final View v) {

        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(getActivity());


        PopupMenu popup = new PopupMenu(getActivity(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_j_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                        case R.id.deleteItem:
                        // do the delete
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Confirm Delete")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete

                                            int id = journalList.get(p).getmJId();
                                            // delete one from the data base.
                                            db.deleteOneJournal(id);

                                            //firebase.child(id).removeValue();

                                            // delete from the displayed list.
                                            journalList.remove(p);

                                            //this line below gives you the animation and also updates the adapter.
                                            mAdapter.notifyItemRemoved(p);


                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // do nothing

                                        }

                                    })// End of set negative button

                                    .setIcon(android.R.drawable.ic_dialog_alert)

                                    .show();
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.show();
    }// End of showMenu


    /**************************************************************************************
     * Title: Google Firebase
     * Author: Filip Babić
     * Date: June 2018
     * Availability: https://www.raywenderlich.com/5114-firebase-tutorial-for-android-getting-started
     *
     ***************************************************************************************/

    public void loadFromCloud (){

        ////***********LOAD FROM FIREBASE DATABASE ********************************

        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.d(DEBUG_TAG, "Network Info: " + isConnected);

        if (isConnected) {


            MainActivity mActivity = (MainActivity) getActivity();
            mActivity.mTitle.setText("Journal - Cloud");

            database = FirebaseDatabase.getInstance();
            refdb = database.getReference();

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            String uid = currentUser.getUid();
            String uname = currentUser.getDisplayName();

            DatabaseReference database = FirebaseDatabase.getInstance().getReference();

            DatabaseReference userId = database.child(uid);

            DatabaseReference userName = userId.child(uname);

            DatabaseReference myRef = userName.child("Journal");

            Log.i("FIREBASE", "Before Firebase Call");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("FIREBASE", "Call Completed");
                    // do my bidding i.e find all the workouts on the database.

                    for (DataSnapshot jDataSnapshot : dataSnapshot.getChildren()) {

                        journalWorkout j = jDataSnapshot.getValue(journalWorkout.class);
                        Log.i("FIREBASE", "Call Value: " + j);

                        String title = j.getJournalTitle();
                        Log.i("FIREBASE", "Call Title: " + title);

                        String time = j.getJournalTime();
                        Log.i("FIREBASE", "Call Time: " + time);

                        String calories = j.getJournalCalories();
                        Log.i("FIREBASE", "Call Calories: " + calories);

                        String id = j.getJournalId();
                        Log.i("FIREBASE", "Call Id: " + id);


                        String pictureURL = j.getPictureURL();
                        Log.i("FIREBASE", "Call Picture URL: " + pictureURL);

                        Journal jadd = new Journal(title, time, calories, 0, pictureURL);
                        Log.i("FIREBASE", "Call Object: " + jadd);

                        journalList.add(jadd);
                        Log.i("FIREBASE", "Call List Size: " + journalList.size());

                    }

                    Log.i("FIREBASE", "WHEN DOES THIS HAPPEN? ");

                    // Puts the list in most recently added order.
                    Collections.reverse(journalList);

                    mAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Toast.makeText(getContext(), "Please Check your Internet connection", Toast.LENGTH_LONG).show();

                }
            });

        }// End of if isConnected.

        else {
            StyleableToast.makeText(getContext(), "No network available, please check your connection or load from device.", Toast.LENGTH_LONG, R.style.warningtoast).show();
        }

    }

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    public void loadFromDevice (){

        MainActivity mActivity = (MainActivity) getActivity();
        mActivity.mTitle.setText("Journal - Device");

        ////***********LOAD FROM SQL DATABASE ********************************
        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(getActivity());

        //journalList = new LinkedList<>();

        journalList = db.allJournal();

        if (journalList != null) {

            String[] itemsNames = new String[journalList.size()];

            for (int i = 0; i < journalList.size(); i++) {

                itemsNames[i] = journalList.get(i).toString();
            }
        }

        if (journalList.size() == 0) {
            StyleableToast.makeText(getContext(), "No journal entries, let's go make some!", Toast.LENGTH_SHORT, R.style.mytoast).show();
        }

        Log.i("REFRESH", "List: "+journalList.size());

        // Puts the list in most recently added order.
        Collections.reverse(journalList);

        if (refreshFlag) {

            Log.i("REFRESH", "True");

           mAdapter = new JournalsAdapter(getActivity(), R.layout.journal_row, journalList, listener);
           mRecyclerView.setAdapter(mAdapter);

           mAdapter.notifyDataSetChanged();

        }

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

