package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

public class User {

        // Store the id of the journal entry.
        private Integer mUId;

        // Store the title of the journal entry.
        private String mUWeight ;
        private String mUHeight;

        public User(Integer mUId, String mUHeight, String mUWeight) {

            this.mUId = mUId;
            this.mUHeight = mUHeight;
            this.mUWeight = mUWeight;
        }

        public Integer getmUId() {
            return mUId;
        }
        public void setmUId(Integer mUId) { this.mUId = mUId; }

        public String getmUWeight() { return mUWeight; }
        public void setmUWeight(String mUWeight) { this.mUWeight = mUWeight; }

        public String getmUHeight() { return mUHeight; }
        public void setmUHeight(String mUHeight) { this.mUHeight = mUHeight; }


    }// End of class.

