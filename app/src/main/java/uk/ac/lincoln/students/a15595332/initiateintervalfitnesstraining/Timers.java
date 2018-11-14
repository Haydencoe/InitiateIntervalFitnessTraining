package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

    public class Timers {

        // Store the id of the timer image
        //private int mImageDrawable;

        private int id;


        // Store the title of the timer.
        private String mTitle;

        // Store the prepare time of the timer.
        private String mPrepare;

        // Store the workout time of the timer.
        private String mWorkout;

        // Store the rest time of the timer.
        private String mRest;

        // Store the cycles amount of the timer.
        private String mCycles;

        // Store the sets amount of the timer.
        private String mSets;

        // Store the set rest time of the timer.
        private String mSetRest;

        // Store the cool down time of the timer.
        private String mCoolDown;

        // Store the total time of the timer.
        private String mTotalTime;

        // Store the calories burnt during the timer.
        private String mCaloriesBurnt;

        // Constructor that is used to create an instance of the Timer object
        public Timers(String mTitle, String mPrepare, String mWorkout, String mRest, String mCycles, Integer id, String mSets, String mSetRest, String mCoolDown, String mTotalTime, String mCaloriesBurnt ) {

            //this.mImageDrawable = mImageDrawable;
            this.id = id;

            this.mTitle = mTitle;
            this.mPrepare = mPrepare;
            this.mWorkout= mWorkout;
            this.mRest = mRest;
            this.mCycles = mCycles;
            this.mSets = mSets;
            this.mSetRest = mSetRest;
            this.mCoolDown = mCoolDown;
            this.mTotalTime= mTotalTime;
            this.mCaloriesBurnt = mCaloriesBurnt;

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

        // Sets amount.
        public String getmSets() {
            return mSets;
        }

        public void setmSets(String mSets) {
            this.mSets = mSets;
        }

        // Set Rest time.
        public String getmSetRest() {
            return mSetRest;
        }

        public void setmSetRest(String mSetRest) {
            this.mSetRest = mSetRest;
        }

        // cool down time.
        public String getmCoolDown() {
            return mCoolDown;
        }

        public void setmCoolDown(String mCoolDown) {
            this.mCoolDown = mCoolDown;
        }

        // total time.
        public String getmTotalTime() {
            return mTotalTime;
        }

        public void setmTotalTime(String mTotalTime) {
            this.mTotalTime = mTotalTime;
        }

        // total time.
        public String getmCaloriesBurnt() {
            return mCaloriesBurnt;
        }

        public void setmCaloriesBurnt(String mCaloriesBurnt) { this.mCaloriesBurnt = mCaloriesBurnt; }


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

