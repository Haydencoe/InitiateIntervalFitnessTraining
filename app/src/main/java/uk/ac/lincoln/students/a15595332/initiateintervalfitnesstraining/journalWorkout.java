package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import android.support.annotation.Keep;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@Keep
public class journalWorkout implements Serializable {


        public String journalId;
        public String title;
        public String time;
        public String calories;
        public String pictureURL;
        // Default constructor required for calls to
        // DataSnapshot.getValue(User.class)
        journalWorkout() {}

        public journalWorkout(String title, String time, String calories, String journalId, String pictureURL) {

            this.title = title;
            this.time = time;
            this.calories = calories;
            this.journalId = journalId;

            this.pictureURL = pictureURL;
        }


    public String getJournalTitle() {
        return title;
    }

    public String getJournalTime() {
        return time;
    }

    public String getJournalCalories() {
        return calories;
    }

    public String getJournalId() {
        return journalId;

    }

    public String getPictureURL() {
            return pictureURL;
    }

}// End of Class.




