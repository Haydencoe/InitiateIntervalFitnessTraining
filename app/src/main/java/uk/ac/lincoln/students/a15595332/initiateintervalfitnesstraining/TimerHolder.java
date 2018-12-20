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

    // Text views
    private final TextView title;
    private final TextView prepare;
    private final TextView workout;
    private final TextView rest;
    private final TextView cycles;
    private final TextView sets;
    private final TextView setRest;
    private final TextView coolDown;
    private final TextView totalTime;

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
        this.title = (TextView) itemView.findViewById(R.id.title_text2);
        this.prepare = (TextView) itemView.findViewById(R.id.prepare_text2);
        this.workout = (TextView) itemView.findViewById(R.id.workout_text2);
        this.rest = (TextView) itemView.findViewById(R.id.rest_text2);
        this.cycles = (TextView) itemView.findViewById(R.id.cycles_text2);
        this.sets = (TextView) itemView.findViewById(R.id.sets_text2);
        this.setRest = (TextView) itemView.findViewById(R.id.setrest_text2);
        this.coolDown = (TextView) itemView.findViewById(R.id.cooldown_text2);
        this.totalTime = (TextView) itemView.findViewById(R.id.totalTime_Text2);

        this.menuButton = (ImageButton) itemView.findViewById(R.id.menuButton2);
        this.playButton = (ImageButton) itemView.findViewById(R.id.playButton2);

        mListener = listener;

        menuButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

        // Set the "onClick" listener of the holder
       itemView.setOnClickListener(this);
    }

    public void bindTimers(Timers timers) {

        //Bind the data to the ViewHolder
        this.timers = timers;

        this.title.setText(timers.getmTitle());
        this.prepare.setText(timers.getmPrepare());
        this.workout.setText(timers.getmWorkout());
        this.rest.setText(timers.getmRest());
        this.cycles.setText(timers.getmCycles());
        this.sets.setText(timers.getmSets());
        this.setRest.setText(timers.getmSetRest());
        this.coolDown.setText(timers.getmCoolDown());
        this.totalTime.setText(timers.getmTotalTime());

    }

    // Menu items
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.menuButton2:
                mListener.onMenu(v, this.getLayoutPosition());
                break;
            case R.id.playButton2:
                mListener.onClick(v, this.getLayoutPosition());

                break;
            default:
                break;

        }

    }

}// End of class.
