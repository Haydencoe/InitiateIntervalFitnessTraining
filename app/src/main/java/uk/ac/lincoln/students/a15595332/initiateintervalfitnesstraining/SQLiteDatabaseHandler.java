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


    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE Timers ( "
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, " + "title TEXT, "
                + "prepare TEXT, " + "workout TEXT, " + "rest TEXT, "
                + "cycles TEXT, " + "sets TEXT, " + "setRest TEXT, " + "coolDown TEXT, " + "totalTime TEXT, " + "caloriesBurnt TEXT  )";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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

}