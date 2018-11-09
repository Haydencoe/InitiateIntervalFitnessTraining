package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TimersAdapter extends RecyclerView.Adapter<TimerHolder> {

    public final List<Timers> timers;
    private Context context;
    private int itemResource;

    private CustomItemClickListener mListener;




    public TimersAdapter(Context context, int itemResource, List<Timers> timers, CustomItemClickListener listener) {

        // Initialise the adapter
        this.timers = timers;
        this.context = context;
        this.itemResource = itemResource;


        mListener = listener;
    }

    // Override the onCreateViewHolder method
    @Override
    public TimerHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new TimerHolder(this.context, view, mListener);
    }

    // Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(TimerHolder holder, int position) {

        // Use position to access the correct Timer's object
        Timers timers = this.timers.get(position);

        // Bind the timer's object to the holder
        holder.bindTimers(timers);
    }

    @Override
    public int getItemCount() {

        return this.timers.size();
    }




}


/*

public class TimersAdapter extends ArrayAdapter<Timers> {

        private Context mContext;
        private List<Timers> timersList = new ArrayList<>();

        public TimersAdapter(@NonNull Context context, ArrayList<Timers> list) {
            super(context, 0, list);
            mContext = context;
            timersList = list;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listItem = convertView;
            if (listItem == null)
                listItem = LayoutInflater.from(mContext).inflate(R.layout.timer_row, parent, false);

            Timers currentTimers = timersList.get(position);

            ImageView image = (ImageView) listItem.findViewById(R.id.imageView_poster);
            image.setImageResource(currentTimers.getmImageDrawable());

            TextView name = (TextView) listItem.findViewById(R.id.textView_name);
            name.setText(currentTimers.getmName());

            TextView release = (TextView) listItem.findViewById(R.id.textView_release);
            release.setText(currentTimers.getmRelease());

            return listItem;
        }
    }


*/

