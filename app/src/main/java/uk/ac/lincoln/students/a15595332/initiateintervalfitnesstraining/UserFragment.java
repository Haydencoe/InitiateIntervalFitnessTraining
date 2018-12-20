package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.HistoryApi;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.SessionReadResponse;
import com.google.android.gms.fitness.result.SessionReadResult;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.muddzdev.styleabletoast.StyleableToast;

import net.sqlcipher.database.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserFragment() {
        // Required empty public constructor
    }

    private OnFragmentInteractionListener mListener;
    private FirebaseAuth mAuth;
    private GoogleApiClient mClient = null;
    private SQLiteDatabaseHandler db;
    public SQLiteDatabase database;

    public int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1234;

    public String TAG = "Google Fit";

    public boolean sign;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View rootView;

    public TextView heightTV;
    public TextView weightTV;

    public String setWeight;
    public String setHeight;
    public String setCalories;

    public FitnessOptions fitnessOptions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         rootView = inflater.inflate(R.layout.fragment_user, container, false);

        heightTV  = (TextView) rootView.findViewById(R.id.heightTV);
        weightTV  = (TextView) rootView.findViewById(R.id.weightTV);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Load information from Google account
        if (currentUser != null) {
            // Loading profile image

            ImageView profileImage = rootView.findViewById(R.id.userPhoto);
            Uri profilePicUrl = currentUser.getPhotoUrl();
            if (profilePicUrl != null) {
                Glide.with(this)
                        .load(profilePicUrl)

                        .apply(new RequestOptions().placeholder(R.drawable.ic_person_outline_black_24dp).error(R.drawable.ic_error_black_24dp).centerCrop().fitCenter())
                        .into(profileImage);
            }


            TextView profileName = rootView.findViewById(R.id.userName);
            String profileNameText = currentUser.getDisplayName();
            if (profileNameText != null){
                profileName.setText(profileNameText);
            }

        }


            ImageButton button = (ImageButton) rootView.findViewById(R.id.googleFitButton);
            TextView dataLabel = (TextView) rootView.findViewById(R.id.dataTextView);
            Button signOut = (Button) rootView.findViewById(R.id.signOutButton);

            fitnessOptions = FitnessOptions.builder()
                    .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.TYPE_WEIGHT, FitnessOptions.ACCESS_READ)
                    .addDataType(DataType.TYPE_HEIGHT, FitnessOptions.ACCESS_READ)
                    .build();


        // Sign in state
        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        sign =  pref.getBoolean("key_sign", false); // getting boolean
        Log.d("Sign", "Result" + sign);


        if (sign) {
                Log.d("Permission", "Has start up permission");

                button.setVisibility(View.INVISIBLE);
                dataLabel.setVisibility(View.INVISIBLE);

                heightTV.setVisibility(View.VISIBLE);
                weightTV.setVisibility(View.VISIBLE);

                signOut.setVisibility(View.VISIBLE);


                // Network connected check.
                ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                Log.d("NETWORK", "Network Info: " + isConnected);

                if (isConnected) {

                    readHistoryData();
                }

                else // not connected

                {
                    // create our sqlite helper class
                    db = new SQLiteDatabaseHandler(getContext());

                    boolean userData = db.CheckIsDataAlreadyInDBorNot("User", "UId", "1");

                    if (userData) {
                        // load them

                        // create loaded timer
                        User user =  db.getUser(1);

                      String heightText = "Height: " + user.getmUHeight();
                      String weightText = "Weight: " + user.getmUWeight();

                        heightTV.setText(heightText);
                        weightTV.setText(weightText);

                    }

                    if (!userData) {
                        // Nothing to load

                        weightTV.setText("No Network");
                        heightTV.setText("Or Stored Data");
                   }
                }
            }//


            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Google fit button press

                    ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null &&
                            activeNetwork.isConnectedOrConnecting();

                    Log.d("NETWORK", "Network Info: " + isConnected);

                    if (isConnected) {

                        SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putBoolean("key_sign", true); // Storing boolean - true/false
                        editor.apply(); // commit changes
                        sign = true;


                        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions)) {

                            GoogleSignIn.requestPermissions(
                                    getActivity(), // your activity
                                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                                    GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions
                                   );

                            Log.d("Click", "if"); // Debugging

                        } else {

                            Log.d("Click", "else"); // Debugging

                            GoogleSignIn.requestPermissions(
                                    getActivity(), // your activity
                                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                                    GoogleSignIn.getLastSignedInAccount(getContext()), fitnessOptions
                            );

                            readHistoryData();
                        }
                    }


                    else {

                        StyleableToast.makeText(getContext(), "Connect to a network to access Google Fit", Toast.LENGTH_LONG, R.style.warningtoast).show();
                    }

                }
            });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do sign out

                // Exception handing in case logout fails.
                try {

                // log out and disable Google fit
                Fitness.getConfigClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getContext())).disableFit();

                // create our sqlLite helper class
                db = new SQLiteDatabaseHandler(getContext());
                // Delete the user's stored data after they have chosen to log out from Google fit.
                db.deleteOneUser(1);

                }
                catch (Exception e) {
                    StyleableToast.makeText(getContext(), "Error logging out, please try again later.", Toast.LENGTH_LONG, R.style.warningtoast).show();
                    Log.e("APP", "exception", e);
                }


                // Reset the ui
                ImageButton button = (ImageButton) rootView.findViewById(R.id.googleFitButton);
                TextView dataLabel = (TextView) rootView.findViewById(R.id.dataTextView);
                Button signOut = (Button) rootView.findViewById(R.id.signOutButton);
                TextView heightLabel = (TextView) rootView.findViewById(R.id.heightTV);
                TextView weightLabel = (TextView) rootView.findViewById(R.id.weightTV);

                button.setVisibility(View.VISIBLE);
                dataLabel.setVisibility(View.VISIBLE);

                signOut.setVisibility(View.INVISIBLE);
                heightLabel.setVisibility(View.INVISIBLE);
                weightLabel.setVisibility(View.INVISIBLE);


                SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();

                editor.putBoolean("key_sign", false); // Storing boolean - true/false
                editor.apply(); // commit changes
                sign = false;

                Log.d("Sign", "Result" + sign);
                Log.d("Log state", "signed out");


            }

        });

        // Inflate the layout for this fragment
        return rootView;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data); //comment this unless you want to pass your result to the activity.
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {

                if (resultCode == Activity.RESULT_OK) {
                readHistoryData();
            }
        }
    }



    /**************************************************************************************
     * Title: Google Fit read request
     * Author: Google
     * Date: December 2018
     * Availability: https://developers.google.com/fit/android/history
     *
     ***************************************************************************************/

    /**
     * Asynchronous task to read the history data.
     */
    public Task<DataReadResponse> readHistoryData() {

        ImageButton button = (ImageButton) rootView.findViewById(R.id.googleFitButton);
        TextView dataLabel = (TextView) rootView.findViewById(R.id.dataTextView);
        Button signOut = (Button) rootView.findViewById(R.id.signOutButton);

        button.setVisibility(View.INVISIBLE);
        dataLabel.setVisibility(View.INVISIBLE);

        signOut.setVisibility(View.VISIBLE);

        // Begin by creating the query.
        DataReadRequest readRequest = queryFitnessData();



        // Invoke the History API to fetch the data with the query
        return Fitness.getHistoryClient(getActivity(), GoogleSignIn.getLastSignedInAccount(getContext()))
                .readData(readRequest)
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {

                                printData(dataReadResponse);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG, "There was a problem reading the data.", e);
                            }
                        });
    }

     /*
     * Creates and returns a {@link DataSet} of step count data for insertion using the History API.
     */

    /** Returns a {@link DataReadRequest} for all step count changes in the past week. */
    public static DataReadRequest queryFitnessData() {
        // [START build_read_data_request]
        // Setting a start and end date using a large range.
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.YEAR, -5);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("USER", "Range Start: " + dateFormat.format(startTime));
        Log.i("USER", "Range End: " + dateFormat.format(endTime));

        DataReadRequest readRequest =
                new DataReadRequest.Builder()

                        //.aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                        //.aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)

                        .read(DataType.TYPE_WEIGHT)
                        .read(DataType.TYPE_HEIGHT)
                        //.read(DataType.TYPE_HEART_POINTS)
                        //.read(DataType.TYPE_CALORIES_EXPENDED)

                       // .bucketByTime(1, TimeUnit.DAYS)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)

                        .build();
        // [END build_read_data_request]

        return readRequest;
    }

    /**
     * Logs a record of the query result. It's possible to get more constrained data sets by
     * specifying a data source or data type
     */
    public  void printData(DataReadResponse dataReadResult) {
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                    "USER", "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet); // Grab the returned data to this method.
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i("USER", "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {

                dumpDataSet(dataSet);

            }
        }
        // [END parse_read_data_result]
    }


    // [START parse_dataset]


   // Retrieve the data and set into relevant UI
    private  void dumpDataSet(DataSet dataSet) {
        Log.i("USER", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = getTimeInstance();


        String height = "";
        String weight = "";

        String heightText = "";
        String weightText = "";

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i("USER", "Data point:");
            Log.i("USER", "\tType: " + dp.getDataType().getName());
            Log.i("USER", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i("USER", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));

            for (Field field : dp.getDataType().getFields()) {
                Log.i("USER", "\tField: " + field.getName() + " Value: " + dp.getValue(field));

                if (field.getName().equals("height"))
                {
                    setHeight = dp.getValue(field).toString();
                }

                if (field.getName().equals("weight"))
                {
                    setWeight = dp.getValue(field).toString();
                }

            }
            Log.i("USER Weight", weight);
            Log.i("USER Height", height);

        }


        // In case the user has no set weight or height data.
        if (setHeight == null) {
            setHeight = "No height data found";
        }

        if (setWeight == null) {
            setWeight = "No weight data found";
        }
            heightText = "Height: " + setHeight;
            weightText = "Weight: " + setWeight;

        heightTV.setVisibility(View.VISIBLE);
        weightTV.setVisibility(View.VISIBLE);

        heightTV.setText(weightText);
        weightTV.setText(heightText);

        // create our sqlLite helper class
        db = new SQLiteDatabaseHandler(getContext());
        // create new timer
        User user1 = new User(1,setHeight,setWeight);

        // Checker for data in the user SQLite table
        boolean userData = db.CheckIsDataAlreadyInDBorNot("User", "UId", "1");

        if (userData) {
            // add them
            db.updateUser(user1);
        }

        if  (!userData) {
            // update them
            db.addUser(user1);
        }

        Log.i("Boolean ", "userData: " + userData);

    }


    // If the returned data is contained in a session.
    private void dumpSession(Session session) {
        DateFormat dateFormat = getTimeInstance();
        Log.i(TAG, "Data returned for Session: " + session.getName()
                + "\n\tDescription: " + session.getDescription()
                + "\n\tStart: " + dateFormat.format(session.getStartTime(TimeUnit.MILLISECONDS))
                + "\n\tEnd: " + dateFormat.format(session.getEndTime(TimeUnit.MILLISECONDS)));
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
                    + " must implement OnFragmentInteractionListener.");
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

