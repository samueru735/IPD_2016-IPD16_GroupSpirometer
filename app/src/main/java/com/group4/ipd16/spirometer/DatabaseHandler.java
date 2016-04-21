package com.group4.ipd16.spirometer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "userInfo";
    // Contacts table name
    private static final String TABLE_USERS = "users";
    // Users Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_DOCTOR_EMAIL = "doctor_email";
    private static final String KEY_AGE = "age";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ETHNICITY = "ethnicity";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT," + KEY_DOCTOR_EMAIL + " TEXT," + KEY_AGE + " INTEGER,"
                + KEY_HEIGHT + " INTEGER," + KEY_WEIGHT + " INTEGER," + KEY_GENDER + " TEXT,"
                + KEY_ETHNICITY + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        // Creating tables again
        onCreate(db);
    }

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.getFirst_name());
        values.put(KEY_LAST_NAME, user.getLast_name());
        values.put(KEY_DOCTOR_EMAIL, user.getDoctor_email());
        values.put(KEY_AGE, user.getAge());
        values.put(KEY_HEIGHT, user.getHeight());
        values.put(KEY_WEIGHT, user.getWeight());
        values.put(KEY_GENDER, user.getGender());
        values.put(KEY_ETHNICITY, user.getEthnicity());
        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }

    // Getting one user
    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_FIRST_NAME, KEY_LAST_NAME,KEY_DOCTOR_EMAIL, KEY_AGE,
                        KEY_HEIGHT, KEY_WEIGHT, KEY_GENDER, KEY_ETHNICITY }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        User contact = new User(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                Integer.parseInt(cursor.getString(4)), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), cursor.getString(7), cursor.getString(8));
        // return user
        return contact;
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<User>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setFirst_name(cursor.getString(1));
                user.setLast_name(cursor.getString(2));
                user.setDoctor_email(cursor.getString(3));
                user.setAge(Integer.parseInt(cursor.getString(5)));
                user.setHeight(Integer.parseInt(cursor.getString(5)));
                user.setWeight(Integer.parseInt(cursor.getString(6)));
                user.setGender(cursor.getString(7));
                user.setEthnicity(cursor.getString(8));
        // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        // return contact list
        return userList;
    }

    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    // Updating a user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FIRST_NAME, user.getFirst_name());
        values.put(KEY_LAST_NAME, user.getLast_name());
        values.put(KEY_DOCTOR_EMAIL, user.getDoctor_email());
        values.put(KEY_AGE, user.getAge());
        values.put(KEY_HEIGHT, user.getHeight());
        values.put(KEY_WEIGHT, user.getWeight());
        values.put(KEY_GENDER, user.getGender());
        values.put(KEY_ETHNICITY, user.getEthnicity());
        // updating row
        return db.update(TABLE_USERS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Deleting a user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }

}
