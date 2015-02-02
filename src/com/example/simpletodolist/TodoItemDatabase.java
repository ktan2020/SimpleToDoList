package com.example.simpletodolist;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TodoItemDatabase extends SQLiteOpenHelper {

	private static final String TAG = "TodoItemDatabase";
	
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "todoListDatabase.db";

	// Todo table name
	private static final String TABLE_TODO = "todo_items";

	// Todo Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_BODY = "body";


	public TodoItemDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating our initial tables
	// These is where we need to write create table statements.
	// This is called when database is created.
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Construct a table for todo items
		String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "(" + KEY_ID
				+ " INTEGER PRIMARY KEY," + KEY_BODY + " TEXT" + ")";
		
		Log.v(TAG, "TodoItemDatabase onCreate");
		db.execSQL(CREATE_TODO_TABLE);
		
		//String INSERT_INTO_TABLE = "INSERT INTO " + TABLE_TODO + "(" + KEY_ID + "," + KEY_BODY + ") VALUES('1','blah blah blah')";
		//db.execSQL(INSERT_INTO_TABLE);
	}

	// Upgrading the database between versions
	// This method is called when database is upgraded like modifying the table
	// structure,
	// adding constraints to database, etc
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion == 1) {
			// Wipe older tables if existed
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
			// Create tables again
			onCreate(db);
		}
	}

	// Insert record into the database
	public void addTodoItem(TodoItem item) {
		// Open database connection
		SQLiteDatabase db = this.getWritableDatabase();
		// Define values for each field
		ContentValues values = new ContentValues();
		values.put(KEY_BODY, item.getBody());
		
		// Insert Row
		db.insertOrThrow(TABLE_TODO, null, values);
		db.close(); // Closing database connection
	}

	// Returns a single todo item by id
	public TodoItem getTodoItem(int id) {
		// Open database for reading
		SQLiteDatabase db = this.getReadableDatabase();
		// Construct and execute query
		Cursor cursor = db.query(TABLE_TODO, // TABLE
				new String[] { KEY_ID, KEY_BODY }, // SELECT
				KEY_ID + "= ?", new String[] { String.valueOf(id) }, // WHERE
				null, null, null, null); // ARGS, GROUP BY, HAVING, ORDER BY
		if (cursor != null)
			cursor.moveToFirst();
		// Load result into model object
		TodoItem item = new TodoItem(cursor.getString(1));
		item.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
		// return todo item
		return item;
	}

	public List<TodoItem> getAllTodoItems() {
		List<TodoItem> todoItems = new ArrayList<TodoItem>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_TODO;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				TodoItem item = new TodoItem(cursor.getString(1));
				item.setId(cursor.getInt(0));
				
				// Adding todo item to list
				todoItems.add(item);
			} while (cursor.moveToNext());
		}

		// return todo list
		return todoItems;
	}

	public int getTodoItemCount() {
		String countQuery = "SELECT  * FROM " + TABLE_TODO;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();
		// return count
		return cursor.getCount();
	}

	public int updateTodoItem(TodoItem item) {
		// Open database for writing
		SQLiteDatabase db = this.getWritableDatabase();
		// Setup fields to update
		ContentValues values = new ContentValues();
		values.put(KEY_BODY, item.getBody());

		// Updating row
		int result = db.update(TABLE_TODO, values, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
		// Close the database
		db.close();
		return result;
	}

	public void deleteTodoItem(TodoItem item) {
		// Open database for writing
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete the record with the specified id
		db.delete(TABLE_TODO, KEY_ID + " = ?",
				new String[] { String.valueOf(item.getId()) });
		// Close the database
		db.close();
	}

}
