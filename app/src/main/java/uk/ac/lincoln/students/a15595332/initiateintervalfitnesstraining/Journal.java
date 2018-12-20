package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

public class Journal {

    // Store the title of the journal entry.
    private String mJTitle ;

    // Store the total time of the journal entry.
    private String mJTotalTime;

    // Store the calories burnt of workout in journal entry.
    private String mJCalories;


    // Store the id of the journal entry.
    private Integer mJId;

    // Store the picture URL
    private String mPictureURL;


    public Journal(String mJTitle, String  mJTotalTime, String mJCalories, Integer mJId, String mPictureURL) {

        this.mJTitle = mJTitle;
        this.mJTotalTime = mJTotalTime;
        this.mJCalories = mJCalories;
        this.mJId = mJId;

        this.mPictureURL = mPictureURL;
    }

    public String getmJTitle() {
        return mJTitle;
    }
    public void setmJTitle(String mJTitle) { this.mJTitle = mJTitle; }

    public String getmJTotalTime() {
        return mJTotalTime;
    }
    public void setmJTotalTime(String mJTotalTime) { this.mJTotalTime = mJTotalTime; }


    public String getmJCalories() {
        return mJCalories;
    }
    public void setmJCalories(String mJCalories) { this.mJCalories = mJCalories; }

    public Integer getmJId() {
        return mJId;
    }
    public void setmJId(Integer mJId) { this.mJId = mJId; }

    public String getmPictureURL() {
        return mPictureURL;
    }
    public void setmPictureURL(String mPictureURL) { this.mPictureURL = mPictureURL; }


}// End of class.

