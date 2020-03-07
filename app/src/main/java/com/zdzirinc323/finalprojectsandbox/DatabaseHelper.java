package com.zdzirinc323.finalprojectsandbox;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Notes.db";

    // The first table, used for storing user information
    private static final String TABLE_NAME_1 = "User_Table";
    private static final String USER_COLUMN_1 = "User_id";
    private static final String USER_COLUMN_2 = "User_email";
    private static final String USER_COLUMN_3 = "User_name";

    // The second table, used for storing task information
    private static final String TABLE_NAME_2 = "Task_Table";
    private static final String TASK_COLUMN_1 = "Task_id";
    private static final String TASK_COLUMN_2 = "Task_title";
    private static final String TASK_COLUMN_3 = "Task_description";
    private static final String TASK_COLUMN_4 = "Task_duedate";
    private static final String TASK_COLUMN_5 = "Task_duetime";
    private static final String TASK_COLUMN_6 = "Task_image";
    private static final String TASK_COLUMN_7 = "Task_latitude";
    private static final String TASK_COLUMN_8 = "Task_longitude";
    private static final String TASK_COLUMN_9 = "User_id"; // This is a foreign key from the other table
    private static final String TASK_COLUMN_10 = "Completed_yn"; // Whether or not task is complete
    private static final String TASK_COLUMN_11 = "inrange";
    Context context;

    GeocodeHelper geocodeHelper;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
        this.context = context;
        this.geocodeHelper = new GeocodeHelper(context, this);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //SQLite for creating the first table
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_1 + " ("+USER_COLUMN_1 +" INTEGER PRIMARY KEY AUTOINCREMENT, "+USER_COLUMN_2+" TEXT, "+USER_COLUMN_3+" TEXT)");

        //SQLite for creating the second table
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME_2 + " ("+TASK_COLUMN_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+TASK_COLUMN_2+" TEXT, "+TASK_COLUMN_3+" TEXT, "+
                TASK_COLUMN_4+" TEXT, "+TASK_COLUMN_5+" TEXT, "+TASK_COLUMN_6+" TEXT, "+TASK_COLUMN_7+" TEXT DEFAULT 0, "+TASK_COLUMN_8+" TEXT DEFAULT 0, "+
                TASK_COLUMN_9+" INT, "+TASK_COLUMN_10+" INTEGER DEFAULT 0, " +TASK_COLUMN_11+" INTEGER DEFAULT 0, FOREIGN KEY("+TASK_COLUMN_9+") REFERENCES "+TABLE_NAME_1+"("+USER_COLUMN_1+"))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
        onCreate(sqLiteDatabase);

    }

    public boolean addUser(String email, String name) {
        //Adds the email and name to table of users.

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cV = new ContentValues();

        cV.put(USER_COLUMN_2, email);
        cV.put(USER_COLUMN_3, name);

        long result =db.insert(TABLE_NAME_1,null,cV);

        //returns true if the data is added succesfully
        return (result == -1) ? false : true;

    }

    public boolean addTask(String title, String description, String duedate,
                           String duetime, String address, int userID)
    { //Adds a new task to the database

        SQLiteDatabase db = this.getWritableDatabase();



        //Set up content values
        ContentValues cV = new ContentValues();
        cV.put(TASK_COLUMN_2, title); cV.put(TASK_COLUMN_3, description);
        cV.put(TASK_COLUMN_4, duedate); cV.put(TASK_COLUMN_5, duetime);
        cV.put(TASK_COLUMN_9, userID);



        Toast.makeText(context, "Added note: " + title, Toast.LENGTH_SHORT).show();

        long result = db.insert(TABLE_NAME_2, null, cV);

        getCoordinates(getTaskID(title, description, userID), address);

        return (result == -1) ? false : true;

    }

    public void getCoordinates(int task_id, String address) {

        geocodeHelper.setTask_id(task_id);
        geocodeHelper.execute(address);

    }


    public void updateCoordinates(int task_id, Coordinate coordinate) {

        ContentValues cV = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String id = Integer.toString(task_id);

        String lat = Double.toString(coordinate.getLat());
        String lon = Double.toString(coordinate.getLng());

        Log.i("CHECK", "Lat: " + lat + " Long: " + lon);

        cV.put(TASK_COLUMN_7, lat);
        cV.put(TASK_COLUMN_8, lon);

        long result = db.update(TABLE_NAME_2, cV, TASK_COLUMN_1+"="+id, null);
        if (result == -1) {
            Log.i("CHECK", "Not adding to database");
        }
    }


    public Cursor getIncompleteUserTasks(int user) {
        //Gets all the uncompleted notes associated with the specified user id

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Task_id, Task_title, Task_description, Task_duedate, task_duetime FROM Task_Table" +
                                " WHERE User_id=? AND Completed_yn=?",
                                new String[] {Integer.toString(user),"0"});

    }

    public Cursor getCompleteUserTasks(int user) {
        //Gets all the completed notes associated with the specified user id

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Task_id, Task_title, Task_description, Task_duedate, task_duetime FROM Task_Table" +
                                " WHERE User_id=? AND Completed_yn=?",
                                new String[] {Integer.toString(user),"1"});

    }

    public Cursor getTasksOnDay(String selectedDate, int user) {

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT Task_id, Task_title, Task_description, Task_duedate, task_duetime FROM Task_Table" +
                        " WHERE User_id=? AND "+TASK_COLUMN_10+"=? AND Task_duedate=?",
                new String[] {Integer.toString(user), "0", selectedDate});

    }

    public Cursor getUser(int user) {
        //Gets the user associated with the specified user id

        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT User_email, User_name FROM User_Table WHERE User_id=?",
                            new String[] {Integer.toString(user)});

    }

    public int getUserID(String email, String name) {
        //Gets user id matching a name and email

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT User_id FROM User_Table WHERE User_email=? AND User_name=?", new String[] {email, name});
        c.moveToFirst();
        return c.getInt(0);

    }

    public int getTaskID(String title, String description, int user_id) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT Task_id FROM Task_Table WHERE Task_title=? AND Task_description=? AND User_id=?", new String[] {title, description, Integer.toString(user_id)});
        c.moveToFirst();
        return c.getInt(0);

    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_1,null,null);
        db.delete(TABLE_NAME_2,null,null);
    }

    public boolean markCompleted(int task_id) {

        String id = Integer.toString(task_id);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_COLUMN_10, 1);
        long result = db.update(TABLE_NAME_2, cv, TASK_COLUMN_1 +"="+id, null);
        return (result == -1) ? false : true;

    }

    public boolean deleteTask(int task_id) {

        String id = Integer.toString(task_id);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME_2, TASK_COLUMN_1+"="+id, null);
        return (result == -1) ? false : true;

    }

    public Coordinate getTaskLatLng(int task_id) {

        double lat, lng;
        String id = Integer.toString(task_id);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_NAME_2, new String[] {TASK_COLUMN_7, TASK_COLUMN_8},TASK_COLUMN_1+"=?",new String[] {id},null,null,null);
        c.moveToFirst();

        if (c.getString(0) != null && c.getString(1) != null) {
            lat = Double.parseDouble(c.getString(0));
            lng = Double.parseDouble(c.getString(1));
            return new Coordinate(lat, lng);
        } else {
            Log.i("CHECK", "null in database");
            return new Coordinate(0, 0);
        }

    }

    public Cursor getForMapView(int user) {

        String id = Integer.toString(user);
        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(TABLE_NAME_2, new String[] {TASK_COLUMN_2,TASK_COLUMN_3,TASK_COLUMN_7,TASK_COLUMN_8}, TASK_COLUMN_9 + "=? AND " + TASK_COLUMN_10 + "=?", new String[] {id, "0"}, null, null, null, null);



    }


    public Cursor getTaskInfo(int task_id) {

        String id = Integer.toString(task_id);
        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(TABLE_NAME_2, new String[] {TASK_COLUMN_2, TASK_COLUMN_3}, TASK_COLUMN_1+"=?", new String[] {id},null,null,null,null);

    }

    public void inRange(int task_id) {

        String id = Integer.toString(task_id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cV = new ContentValues();
        cV.put(TASK_COLUMN_11, 1);

        db.update(TABLE_NAME_2, cV, TASK_COLUMN_1+"=?", new String[] {id});

    }

    public void outRange(int task_id) {

        String id = Integer.toString(task_id);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cV = new ContentValues();
        cV.put(TASK_COLUMN_11, 0);

        db.update(TABLE_NAME_2, cV, TASK_COLUMN_1+"=?", new String[] {id});

    }

    public Cursor getTasksForBackground(int user) {

        String id = Integer.toString(user);
        SQLiteDatabase db = this.getWritableDatabase();

        return db.query(TABLE_NAME_2, new String[] {TASK_COLUMN_1, TASK_COLUMN_7, TASK_COLUMN_8, TASK_COLUMN_11, TASK_COLUMN_2}, TASK_COLUMN_9+"=?", new String[] {id},null,null,null,null);

        // task_id = c.getString(0), lat = c.getInt(1), long = c.getInt(2)

    }
}
