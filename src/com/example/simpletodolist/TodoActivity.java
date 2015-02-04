package com.example.simpletodolist;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class TodoActivity extends Activity {

	private static final String TAG = "TodoActivity";	
	private final int REQUEST_CODE = 20;

	
	ArrayList<TodoItem> items;
	TodoItemsAdapter<TodoItem> itemsAdapter; 
	ListView lvItems;
	TodoItemDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		db = new TodoItemDatabase(this);
		items = (ArrayList<TodoItem>) db.getAllTodoItems();
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		
		//itemsAdapter = new ArrayAdapter<TodoItem>(this, android.R.layout.simple_list_item_1, items);
		itemsAdapter = new TodoItemsAdapter<TodoItem>(this, items);
		lvItems.setAdapter(itemsAdapter);
		
		setUpListViewListener(this);
	}

	public void setUpListViewListener(final Context ctx) {
		lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				TodoItem todo = (TodoItem)adapter.getItemAtPosition(pos);
				
				Log.i(TAG, "onItemClick => id: " + todo.getId() + ", body: " + todo.getBody() + ", pos: " + pos + ", id: " + id);
				
				launchEditView(pos+1, todo.getBody());
			}
		});
		
		lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, final View view, int pos, long id) {
				final TodoItem todo = (TodoItem)adapter.getItemAtPosition(pos);
				
				Log.i(TAG, "onItemLongClick => id: " + todo.getId() + ", body: " + todo.getBody());
								
				view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable(){
					@Override
					public void run() {
						items.remove(todo);
						itemsAdapter.notifyDataSetChanged();
						view.setAlpha(1);
					}
				});
				
				Toast.makeText(ctx, "Removing [" + todo.getBody() + "]", Toast.LENGTH_LONG).show();
				
				TodoItem remove = new TodoItem(todo.getBody());
				remove.setId(pos+1);
				db.deleteTodoItem(remove);
				
				return true;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void onAddItem(View v) {
		EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
		String itemText = etNewItem.getText().toString();
		
		TodoItem item = new TodoItem(itemText);
		db.addTodoItem(item);
		etNewItem.setText("");
		
		itemsAdapter.add(item);
		itemsAdapter.notifyDataSetChanged();
	}
	
	public void launchEditView(int id, String itemtext) {
		Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
		i.putExtra("id", id);
		i.putExtra("item", itemtext);
				
		Log.v(TAG, " launching EditItemActivity ... ");
		startActivityForResult(i, REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode==RESULT_OK && requestCode==REQUEST_CODE) {
			int id = data.getExtras().getInt("id");
			String item = data.getExtras().getString("item");
						
			Log.i(TAG, "EditItemActivity returned: (" + id + "), (" + item + ")");
			
			TodoItem todo = new TodoItem(item);
			todo.setId(id);
			
			db.updateTodoItem(todo);
			((TodoItem)itemsAdapter.getItem(id-1)).setBody(item);
			itemsAdapter.notifyDataSetChanged();
			
			Toast.makeText(getApplicationContext(), "... saving to do list ...", Toast.LENGTH_SHORT).show();
		}
	}
}
