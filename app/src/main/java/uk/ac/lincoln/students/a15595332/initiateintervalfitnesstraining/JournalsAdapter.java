package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class JournalsAdapter extends RecyclerView.Adapter<JournalsHolder> {

    public final List<Journal> journal;
    private Context context;
    private int itemResource;

    private JCustomItemClickListener mListener;




    public JournalsAdapter(Context context, int itemResource, List<Journal> journal, JCustomItemClickListener listener) {

        // Initialise the adapter
        this.journal = journal;
        this.context = context;
        this.itemResource = itemResource;


        mListener = listener;
    }

    // Override the onCreateViewHolder method
    @Override
    public JournalsHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        // Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from(parent.getContext())
                .inflate(this.itemResource, parent, false);
        return new JournalsHolder(this.context, view, mListener);
    }

    // Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(JournalsHolder holder, int position) {

        // Use position to access the correct Timer's object
        Journal journal = this.journal.get(position);

        // Bind the timer's object to the holder
        holder.bindJournal(journal);
    }

    @Override
    public int getItemCount() {

        return this.journal.size();
    }




}// End of class.