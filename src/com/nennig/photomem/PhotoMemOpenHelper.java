package com.nennig.photomem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PhotoMemOpenHelper extends SQLiteOpenHelper {


	
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "MemTable";
    private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
//                Mem.UID + " INTEGER PRIMARY KEY, " + 
                Mem.MEM_NAME + " TEXT PRIMARY KEY, " +
                Mem.TIME_CREATED + " DATETIME, " + 
                Mem.TIMES_COMPLETED + "INTEGER, " +
                Mem.COMPLETION_RATE + "DOUBLE, " + 
                Mem.RANDOMIZE + "BOOLEAN);";
	private static final String DATABASE_NAME = "photo_mem.db";
	private static final String TAG = "PhotoMemOpenHelper";

	

    PhotoMemOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Logs that the database is being upgraded
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        // Kills the table and existing data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Recreates the database with a new version
        onCreate(db);
	}

	public void addMem(String name){
		SQLiteDatabase db = this.getWritableDatabase();
		
		Mem m = new Mem(name);
		m.create();
		
		ContentValues values = new ContentValues();
		values.put(Mem.MEM_NAME, m.getName());
		values.put(Mem.TIME_CREATED, m.getTimeCreated());
		values.put(Mem.TIMES_COMPLETED, m.getTimesMemCompleted());
		values.put(Mem.COMPLETION_RATE, m.getCompletionRate());
		values.put(Mem.RANDOMIZE, m.getRandomize());
		
		db.insert(DATABASE_NAME, null, values);
		db.close();
	}
	
	public void deleteMem(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Mem.MEM_NAME + " = ?",
                new String[] { name });
        db.close();
	}
	
	public Mem getMem(String name){
		 SQLiteDatabase db = this.getReadableDatabase();
		 
		 
		 
	        Cursor cursor = db.query(TABLE_NAME, Mem.getMemColumns(), Mem.MEM_NAME + "=?",
	                new String[] {name}, null, null, null, null);
	        if (cursor != null)
	            cursor.moveToFirst();
	 
	        Mem m = new Mem(name);
	        m.recreate(cursor.getString(1),Integer.valueOf(cursor.getString(2)), 
	        		Double.valueOf(cursor.getString(3)), Boolean.valueOf(cursor.getString(4)));
	        // return mem
	        return m;
	}
	
	public List<Mem> getAllMems(){
		List<Mem> memList = new ArrayList<Mem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Mem m = new Mem(cursor.getString(0));
    	        m.recreate(cursor.getString(1),Integer.valueOf(cursor.getString(2)), 
    	        		Double.valueOf(cursor.getString(3)), Boolean.valueOf(cursor.getString(4)));
                // Adding mem to list
                memList.add(m);
            } while (cursor.moveToNext());
        }
 
        // return mem list
        return memList;
	}
	
	public void updateMemCompletionRate(double num){
		
	}
	
	public void updateMemRandomize(boolean val){
		
	}
}
