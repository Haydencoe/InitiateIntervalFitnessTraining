package uk.ac.lincoln.students.a15595332.initiateintervalfitnesstraining;

import java.util.LinkedList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TimersDB";
    private static final String TABLE_NAME = "Timers";

    private static final String KEY_ID = "id";

    private static final String KEY_TITLE = "title";
    private static final String KEY_PREPARE = "prepare";
    private static final String KEY_WORKOUT = "workout";
    private static final String KEY_REST = "rest";
    private static final String KEY_CYCLES = "cycles";
    private static final String KEY_SETS = "sets";
    private static final String KEY_SETREST = "setRest";
    private static final String KEY_COOLDOWN = "coolDown";
    private static final String KEY_TOTALTIME = "totalTime";
    private static final String KEY_CALORIES_BURNT = "caloriesBurnt";


    private static final String[] COLUMNS = { KEY_ID, KEY_TITLE, KEY_PREPARE, KEY_WORKOUT, KEY_REST, KEY_CYCLES, KEY_SETS, KEY_SETREST, KEY_COOLDOWN, KEY_TOTALTIME, KEY_CALORIES_BURNT };


    private static final String JOURNAL_TABLE = "Journal";

    private static final String KEY_J_ID = "jId";

    private static final String KEY_J_TITLE = "jTitle";
    private static final String KEY_J_TOTALTIME = "jTotalTime";
    private static final String KEY_J_CALORIES = "jCalories";
    private static final String KEY_J_PICTURE = "jPicture";

    private static final String[] JCOLUMNS = { KEY_J_ID, KEY_J_TITLE, KEY_J_TOTALTIME, KEY_J_CALORIES, KEY_J_PICTURE };


    private static final String USER_TABLE = "User";

    private static final String KEY_U_ID = "uId";

    private static final String KEY_U_WEIGHT = "uWeight";
    private static final String KEY_U_HEIGHT = "uHeight";

    private static final String[] UCOLUMNS = { KEY_U_ID, KEY_U_HEIGHT, KEY_U_WEIGHT };

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create the timers table.
        String CREATION_TABLE = "CREATE TABLE Timers ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, "
                + "prepare TEXT, " + "workout TEXT, " + "rest TEXT, "
                + "cycles TEXT, " + "sets TEXT, " + "setRest TEXT, " + "coolDown TEXT, " + "totalTime TEXT, " + "caloriesBurnt TEXT  )";



        // Create the journal table.
        String CREATION_TABLE_J = "CREATE TABLE Journal ( "
                + "jId INTEGER PRIMARY KEY AUTOINCREMENT, " + "jTitle TEXT, "
                + "jTotalTime TEXT, " + "jCalories TEXT, " + "jPicture TEXT )";


        // Create the user table.
        String CREATION_TABLE_U = "CREATE TABLE User ( "
                + "uId INTEGER, " + "uWeight TEXT, "
                + "uHeight TEXT )";


        db.execSQL(CREATION_TABLE_U);
        db.execSQL(CREATION_TABLE_J);
        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        db.execSQL("DROP TABLE IF EXISTS " + JOURNAL_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);

        this.onCreate(db);



    }

    public void deleteOne(int id) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase("secret");
        db.delete(TABLE_NAME, "id = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public Timers getTimer(int id) {
        SQLiteDatabase db = this.getReadableDatabase("secret");
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Timers timers = new Timers("", "","","","",0, "", "", "", "", "");
        timers.setmTitle(cursor.getString(1));
        timers.setmPrepare(cursor.getString(2));
        timers.setmWorkout(cursor.getString(3));
        timers.setmRest(cursor.getString(4));
        timers.setmCycles(cursor.getString(5));
        timers.setmSets(cursor.getString(6));
        timers.setmSetRest(cursor.getString(7));
        timers.setmCoolDown(cursor.getString(8));
        timers.setmTotalTime(cursor.getString(9));
        timers.setmCaloriesBurnt(cursor.getString(10));

        cursor.close();
        db.close();
        return timers;
    }

    public List<Timers> allTimers() {

        List<Timers> timers = new LinkedList<Timers>();
        String query = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase("secret");
        Cursor cursor = db.rawQuery(query, null);
        Timers timer = null;

        if (cursor.moveToFirst()) {
            do {
                timer = new Timers("", "", "", "", "", 0, "", "", "", "", "");
                timer.setId(cursor.getInt(0));
                timer.setmTitle(cursor.getString(1));
                timer.setmPrepare(cursor.getString(2));
                timer.setmWorkout(cursor.getString(3));
                timer.setmRest(cursor.getString(4));
                timer.setmCycles(cursor.getString(5));
                timer.setmSets(cursor.getString(6));
                timer.setmSetRest(cursor.getString(7));
                timer.setmCoolDown(cursor.getString(8));
                timer.setmTotalTime(cursor.getString(9));
                timer.setmCaloriesBurnt(cursor.getString(10));
                timers.add(timer);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return timers;
    }

    public void addTimer(Timers timers) {
        SQLiteDatabase db = this.getWritableDatabase("secret");
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, timers.getmTitle());
        values.put(KEY_PREPARE, timers.getmPrepare());
        values.put(KEY_WORKOUT, timers.getmWorkout());
        values.put(KEY_REST, timers.getmRest());
        values.put(KEY_CYCLES, timers.getmCycles());
        values.put(KEY_SETS, timers.getmSets());
        values.put(KEY_SETREST, timers.getmSetRest());
        values.put(KEY_COOLDOWN, timers.getmCoolDown());
        values.put(KEY_TOTALTIME, timers.getmTotalTime());
        values.put(KEY_CALORIES_BURNT, timers.getmCaloriesBurnt());

        // insert
        db.insert(TABLE_NAME,null, values);
        db.close();
    }

    public int updateTimer(Timers timers) {
        SQLiteDatabase db = this.getWritableDatabase("secret");
        ContentValues values = new ContentValues();

        values.put(KEY_TITLE, timers.getmTitle());
        values.put(KEY_PREPARE, timers.getmPrepare());
        values.put(KEY_WORKOUT, timers.getmWorkout());
        values.put(KEY_REST, timers.getmRest());
        values.put(KEY_CYCLES, timers.getmCycles());
        values.put(KEY_SETS, timers.getmSets());
        values.put(KEY_SETREST, timers.getmSetRest());
        values.put(KEY_COOLDOWN, timers.getmCoolDown());
        values.put(KEY_TOTALTIME, timers.getmTotalTime());
        values.put(KEY_CALORIES_BURNT, timers.getmCaloriesBurnt());

        int i = db.update(TABLE_NAME, // table
                values, // column/value
                "id = ?", // selections
                new String[] { String.valueOf(timers.getId()) });


        db.close();

        return i;
    }


    //**************************** Methods for journal *************************************//

    public void deleteOneJournal(int id) {
        // Get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase("secret");
        db.delete(JOURNAL_TABLE, "jId = ?", new String[] { String.valueOf(id) });
        db.close();
    }

    public Journal getJournal(int jId) {
        SQLiteDatabase db = this.getReadableDatabase("secret");
        Cursor cursor = db.query(JOURNAL_TABLE, // a. table
                JCOLUMNS, // b. column names
                " jId = ?", // c. selections
                new String[] { String.valueOf(jId) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        Journal journal = new Journal("", "", "", 0, "");
        journal.setmJTitle(cursor.getString(1));
        journal.setmJTotalTime(cursor.getString(2));
        journal.setmJCalories(cursor.getString(3));
        journal.setmPictureURL(cursor.getString(4));

        cursor.close();
        db.close();
        return journal;
    }


    public List<Journal> allJournal() {

        List<Journal> journals = new LinkedList<Journal>();
        String query = "SELECT  * FROM " + JOURNAL_TABLE;
        SQLiteDatabase db = this.getWritableDatabase("secret");
        Cursor cursor = db.rawQuery(query, null);
        Journal journal = null;

        if (cursor.moveToFirst()) {
            do {
                journal = new Journal("", "", "", 0, "");
                journal.setmJId(cursor.getInt(0));
                journal.setmJTitle(cursor.getString(1));
                journal.setmJTotalTime(cursor.getString(2));
                journal.setmJCalories(cursor.getString(3));
                journal.setmPictureURL(cursor.getString(4));

                journals.add(journal);
            } while (cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return journals;
    }

    public void addJournal(Journal journal) {
        SQLiteDatabase db = this.getWritableDatabase("secret");
        ContentValues values = new ContentValues();

        values.put(KEY_J_TITLE, journal.getmJTitle());
        values.put(KEY_J_TOTALTIME, journal.getmJTotalTime());
        values.put(KEY_J_CALORIES, journal.getmJCalories());
        values.put(KEY_J_PICTURE, journal.getmPictureURL());

        // insert
        db.insert(JOURNAL_TABLE,null, values);
        db.close();
    }


    //**************************** Methods for user *************************************//

    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase("secret");
        ContentValues values = new ContentValues();

        values.put(KEY_U_ID, user.getmUId());
        values.put(KEY_U_HEIGHT, user.getmUHeight());
        values.put(KEY_U_WEIGHT, user.getmUWeight());

        // insert
        db.insert(USER_TABLE,null, values);
        db.close();
    }

    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase("secret");
        ContentValues values = new ContentValues();

        values.put(KEY_U_ID, user.getmUId());
        values.put(KEY_U_HEIGHT, user.getmUHeight());
        values.put(KEY_U_WEIGHT, user.getmUWeight());

        int i = db.update(USER_TABLE, // table
                values, // column/value
                "UId = ?", // selections
                new String[] { String.valueOf(user.getmUId()) });

        db.close();

        return i;
    }

    public boolean CheckIsDataAlreadyInDBorNot(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getWritableDatabase("secret");

            String Query = "Select * from " + TableName + " where " + dbfield + " = " + fieldValue;

            Cursor cursor = db.rawQuery(Query, null);


            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }


            cursor.close();
            db.close();
            return true;

        }

    public User getUser(int UId) {
        SQLiteDatabase db = this.getReadableDatabase("secret");
        Cursor cursor = db.query(USER_TABLE, // a. table
                UCOLUMNS, // b. column names
                " UId = ?", // c. selections
                new String[] { String.valueOf(UId) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        User user = new User( 0, "", "");
        user.setmUId(cursor.getInt(0));
        user.setmUHeight(cursor.getString(1));
        user.setmUWeight(cursor.getString(2));

        cursor.close();
        return user;
    }


}// End of class.