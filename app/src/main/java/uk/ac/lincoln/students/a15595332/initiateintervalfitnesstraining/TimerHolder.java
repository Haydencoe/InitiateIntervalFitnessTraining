package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TimerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //private final ImageView timerPicture;

    private final TextView title;
    private final TextView prepare;
    private final TextView workout;
    private final TextView rest;
    private final TextView cycles;

    private final CustomItemClickListener mListener;

    private final ImageButton menuButton;
    private final ImageButton playButton;

    private Timers timers;





    private Context context;

    public TimerHolder(Context context, View itemView, CustomItemClickListener listener) {

        super(itemView);

        // Set the context
        this.context = context;

        // Set up the UI widgets of the holder
       // this.timerPicture = (ImageView) itemView.findViewById(R.id.imageView_timerPicture);
        this.title = (TextView) itemView.findViewById(R.id.title_text);
        this.prepare = (TextView) itemView.findViewById(R.id.prepare_text);
        this.workout = (TextView) itemView.findViewById(R.id.workout_text);
        this.rest = (TextView) itemView.findViewById(R.id.rest_text);
        this.cycles = (TextView) itemView.findViewById(R.id.cycles_text);


        this.menuButton = (ImageButton) itemView.findViewById(R.id.menuButton);
        this.playButton = (ImageButton) itemView.findViewById(R.id.playButton);

        mListener = listener;

        menuButton.setOnClickListener(this);

        playButton.setOnClickListener(this);

        // Set the "onClick" listener of the holder
       itemView.setOnClickListener(this);
    }

    public void bindTimers(Timers timers) {

        //Bind the data to the ViewHolder
        this.timers = timers;

        // this.timerPicture.setImageResource(timers.getmImageDrawable());

        this.title.setText(timers.getmTitle());
        this.prepare.setText(timers.getmPrepare());
        this.workout.setText(timers.getmWorkout());
        this.rest.setText(timers.getmRest());
        this.cycles.setText(timers.getmCycles());



    }






    @Override
    public void onClick(View v) {

        /*
        if (this.timers != null) {

            Toast.makeText(this.context, "Clicked on " + this.timers.getmTitle(), Toast.LENGTH_SHORT ).show();

        }
*/
        switch (v.getId()) {
            case R.id.menuButton:
                mListener.onMenu(v, this.getLayoutPosition());
                break;
            case R.id.playButton:
                mListener.onClick(v, this.getLayoutPosition());

                break;
            default:
                break;



/*
        // 5. Handle the onClick event for the ViewHolder
        if (this.timers != null) {

            //Toast.makeText(this.context, "Clicked on " + this.timers.getmTitle(), Toast.LENGTH_SHORT ).show();

            Toast.makeText(this.context, "Clicked on " + this.timers.getId(), Toast.LENGTH_SHORT ).show();

        }
  */


        }


    }


}// End of class.