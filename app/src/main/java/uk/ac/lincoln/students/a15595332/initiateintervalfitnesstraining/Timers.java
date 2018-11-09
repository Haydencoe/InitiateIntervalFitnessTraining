package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

    public class Timers {

        // Store the id of the timer image
        //private int mImageDrawable;

        private int id;


        // Store the title of the timer
        private String mTitle;

        // Store the prepare time of the timer
        private String mPrepare;

        // Store the workout time of the timer
        private String mWorkout;

        // Store the rest time of the timer
        private String mRest;

        // Store the rest time of the timer
        private String mCycles;

        // Constructor that is used to create an instance of the Timer object
        public Timers(String mTitle, String mPrepare, String mWorkout, String mRest, String mCycles, Integer id ) {

            //this.mImageDrawable = mImageDrawable;
            this.id = id;

            this.mTitle = mTitle;
            this.mPrepare = mPrepare;
            this.mWorkout= mWorkout;
            this.mRest = mRest;
            this.mCycles = mCycles;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }



        // Title name.
        public String getmTitle() {
            return mTitle;
        }

        public void setmTitle(String mTitle) {
            this.mTitle = mTitle;
        }

        // Prepare time.
        public String getmPrepare() {
            return mPrepare;
        }

        public void setmPrepare(String mPrepare) {
            this.mPrepare = mPrepare;
        }

        // Workout time.
        public String getmWorkout() {
            return mWorkout;
        }

        public void setmWorkout(String mWorkout) {
            this.mWorkout = mWorkout;
        }

        // Rest time.
        public String getmRest() {
            return mRest;
        }

        public void setmRest(String mRest) {
            this.mRest = mRest;
        }

        // Cycles count.
        public String getmCycles() {
            return mCycles;
        }

        public void setmCycles(String mCycles) {
            this.mCycles = mCycles;
        }





    }// End of class.





/*
 public Timers(int mImageDrawable, String mTitle, String mPrepare)


        public int getmImageDrawable() {
            return mImageDrawable;
        }

        public void setmImageDrawable(int mImageDrawable) {
            this.mImageDrawable = mImageDrawable;
        }
*/

