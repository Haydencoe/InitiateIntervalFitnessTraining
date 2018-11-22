package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link JournalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link JournalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class JournalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private JournalsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public List<Journal> journalList;

    public int p;

    private SQLiteDatabaseHandler db;

    public JournalFragment() {
        // Required empty public constructor
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


        // create our sqlite helper class
        db = new SQLiteDatabaseHandler(getActivity());


        journalList = db.allJournal();

        if (journalList != null) {

            String[] itemsNames = new String[journalList.size()];

            for (int i = 0; i < journalList.size(); i++) {

                itemsNames[i] = journalList.get(i).toString();

            }

        }

        if (journalList == null) {


        }

        // Puts the list in most recently added order.
        Collections.reverse(journalList);


        // Setup our RecyclerView

        View rootView = inflater.inflate(R.layout.fragment_journal, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.journal_view);


        JCustomItemClickListener listener = new JCustomItemClickListener() {
            @Override
            public void onJMenu(View view, int position) {
                //Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();

                p = position;

                showJMenu(view);

            }

        };



        // Initialize the Journal adapter.
        mAdapter = new JournalsAdapter(getActivity(), R.layout.journal_row, journalList, listener);

        // Initialize ItemAnimator, LayoutManager and ItemDecorators


        // For performance, tell OS RecyclerView won't change size
        mRecyclerView.setHasFixedSize(true);



        // Attach the adapter to RecyclerView
        mRecyclerView.setAdapter(mAdapter);



        // Sets the view to have two columns in a grid fashion.
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(),1);




        // Set the LayoutManager
        mRecyclerView.setLayoutManager(layoutManager);



        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_journal, container, false);




        return rootView;

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

                    /*
                    case R.id.editItem:


                        return true;
                      */
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



    // TODO: Rename method, update argument and hook method into UI event
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
