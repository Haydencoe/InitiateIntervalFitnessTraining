package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class JournalsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private final TextView jTitle;
    private final TextView jTotalTime;
    private final TextView jCalories;


    private Journal journal;

    private Context context;


    public JournalsHolder(Context context, View itemView) {

        super(itemView);

        // Set the context
        this.context = context;

        // Set up the UI widgets of the holder
        this.jTitle = (TextView) itemView.findViewById(R.id.workout_title_text);
        this.jTotalTime = (TextView) itemView.findViewById(R.id.jTotalTime);
        this.jCalories = (TextView) itemView.findViewById(R.id.jCaloriesBurnt);

    }

    public void bindJournal(Journal journal) {

        //Bind the data to the ViewHolder
        this.journal = journal;

        // this.timerPicture.setImageResource(timers.getmImageDrawable());

        this.jTitle.setText(journal.getmJTitle());
        this.jTotalTime.setText(journal.getmJTotalTime());
        this.jCalories.setText(journal.getmJCalories());

    }

    public void onClick(View v) {


        // Handle the onClick event for the ViewHolder
        if (this.journal != null) {


            Toast.makeText(this.context, "Clicked on " , Toast.LENGTH_SHORT ).show();

        }




    }// End of onClick method.





}// end of class
