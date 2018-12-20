package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;

public class JournalsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private final TextView jTitle;
    private final TextView jTotalTime;
    private final TextView jCalories;
    private final TextView jTotalTime_Text;
    private final TextView jCalories_Text;

    private final ImageView jPicture;

    private Journal journal;
    private Context context;
    private final JCustomItemClickListener mListener;
    private final ImageButton menuJButton;


    public JournalsHolder(Context context, View itemView, JCustomItemClickListener listener) {

        super(itemView);

        // Set the context
        this.context = context;

        // Set up the UI widgets of the holder
        this.jTitle = (TextView) itemView.findViewById(R.id.workout_title_text);
        this.jTotalTime = (TextView) itemView.findViewById(R.id.jTotalTime);
        this.jCalories = (TextView) itemView.findViewById(R.id.jCaloriesBurnt);

        this.jPicture = (ImageView) itemView.findViewById(R.id.workoutPic);

        this.jCalories_Text = (TextView) itemView.findViewById(R.id.jCaloriesBurnt_Text);
        this.jTotalTime_Text = (TextView) itemView.findViewById(R.id.jTotalTime_Text);


        this.menuJButton = (ImageButton) itemView.findViewById(R.id.menuJButton);
        mListener = listener;
        menuJButton.setOnClickListener(this);
        // Set the "onClick" listener of the holder
        itemView.setOnClickListener(this);

    }

    public void bindJournal(Journal journal) {

        //Bind the data to the ViewHolder
        this.journal = journal;

        this.jTitle.setText(journal.getmJTitle());
        this.jTotalTime.setText(journal.getmJTotalTime());
        this.jCalories.setText(journal.getmJCalories());

        String imageCheck = journal.getmPictureURL();

        if (imageCheck != null)

        {
            try {

                Bitmap image = decodeFromFirebaseBase64(journal.getmPictureURL());

                if (image != null)
                    //this.jPicture.setImageBitmap(image);

                    jTotalTime.setTextColor(Color.WHITE);
                    jTotalTime_Text.setTextColor(Color.WHITE);
                    jCalories.setTextColor(Color.WHITE);
                    jCalories_Text.setTextColor(Color.WHITE);

                Glide
                        .with(context)
                        .load(image)
                        .apply(new RequestOptions()
                        .fitCenter()
                        .centerCrop()
                        )

                        //.centerCrop()
                        .into(jPicture);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void onClick(View v) {


        // Handle the onClick event for the ViewHolder
        if (this.journal != null) {
            //Toast.makeText(this.context, "Clicked on " , Toast.LENGTH_SHORT ).show();
        }

        switch (v.getId()) {
            case R.id.menuJButton:
                mListener.onJMenu(v, this.getLayoutPosition());
                break;

            default:
                break;
        }

    }// End of onClick method.

    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}// end of class
