package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TimersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimersFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private TimersAdapter mAdapter;
    private RecyclerView mRecyclerView;
    public  List<Timers> timersList;
    private SQLiteDatabaseHandler db;
    private OnFragmentInteractionListener mListener;

    // ints
    public int p; // Position


    private FirebaseAuth mAuth;
    public SQLiteDatabase mDatabase;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context mContext;
    public  CustomItemClickListener cListener;


    public TimersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimersFragment.
     */

    public static TimersFragment newInstance(String param1, String param2) {
        TimersFragment fragment = new TimersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load Libraries into the program for SQLCipher.
        SQLiteDatabase.loadLibs(getActivity());

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        MainActivity mActivity = (MainActivity) getActivity();

        mActivity.mTitle.setText("Initiate Interval");

        // **** Layout for the timer's list *************************************************************

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(getActivity());

        // Exception handing incase the database can't fill the timers list.
        try {

            timersList = db.allTimers();

            if (timersList != null) {

                String[] itemsNames = new String[timersList.size()];

                for (int i = 0; i < timersList.size(); i++) {

                    itemsNames[i] = timersList.get(i).toString();

                }

            }

            if (timersList.size() == 0 && currentUser != null) {

                StyleableToast.makeText(getContext(), "No Timers, let's go make some!" + "\n"+"Press the + to make a new timer.", Toast.LENGTH_SHORT, R.style.mytoast).show();

            }
        }
        catch (Exception e) {
            StyleableToast.makeText(getContext(), "Error, please try again.", Toast.LENGTH_LONG, R.style.warningtoast).show();
            Log.e("APP", "exception", e);
        }



        // Setup our RecyclerView

        View rootView = inflater.inflate(R.layout.fragment_timers, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listings_view);


        CustomItemClickListener listener = new CustomItemClickListener() {
            @Override
            public void onMenu(View view, int position) {
                //Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                p = position;

                showMenu(view);

            }

            public void onClick(View view, int position) {
               // Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                p = position;

                playButton(view);

            }


        };

        // Initialize the Timers adapter.
        mAdapter = new TimersAdapter(getActivity(), R.layout.timers_row, timersList, listener);

        // For performance, tell OS RecyclerView won't change size
        mRecyclerView.setHasFixedSize(true);

        // Attach the adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);

        // Sets the view to have a linear layout design.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // Set the LayoutManager
        mRecyclerView.setLayoutManager(layoutManager);

        return rootView;
    }

    // Start the timer when the play button has been pressed.
    public void playButton(final View v) {

        int id = timersList.get(p).getId();


        Intent i = new Intent(getContext(), PlayingActivity.class);
        i.putExtra("id", id);
        getActivity().startActivityForResult(i, 3);

    }

    /**************************************************************************************
     * Title: Popup Menu source code
     * Author: Bill Phillips, Chris Stewart, Brian Hardy and Kristin Marsicano
     * Date: October 2016
     * Availability: Android Programming: The Big Nerd Ranch Guide (2nd Edition)
     *
     ***************************************************************************************/

    // Show the menu when the three dots button has been pressed.
    public void showMenu(final View v) {

        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(getActivity());


        PopupMenu popup = new PopupMenu(getActivity(), v);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.editItem:
                        // do the edit
                        //Toast toast =  Toast.makeText(getContext(), "Edit Item", Toast.LENGTH_SHORT); toast.show();

                        int id = timersList.get(p).getId();

                        Intent i = new Intent(getContext(), NewTimer.class);
                        i.putExtra("id", id);
                        i.putExtra("pos", p);
                        getActivity().startActivityForResult(i, 2);


                        return true;
                    case R.id.deleteItem:
                        // do the delete

                        new AlertDialog.Builder(getContext())
                                .setTitle("Confirm Delete")
                                .setMessage("Are you sure?")
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            int id = timersList.get(p).getId();
                            // delete one from the data base.
                            db.deleteOne(id);

                            // delete from the displayed list.
                            timersList.remove(p);

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

        void onFragmentInteraction(Uri uri);
    }


}// End of class.

