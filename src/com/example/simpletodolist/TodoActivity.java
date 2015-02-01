package com.example.simpletodolist;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;
import android.util.Log;
import android.content.Intent;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;

public class TodoActivity extends Activity {

	private static final String TAG = "TodoActivity";	
	private final int REQUEST_CODE = 20;

	
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		readItems();
		
		lvItems = (ListView)findViewById(R.id.lvItems);
		
		itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
		lvItems.setAdapter(itemsAdapter);
		
		//items = new ArrayList<String>();
		//items.add("First Item");
		//items.add("Second Item");
		setUpListViewListener(this);
	}

	public void setUpListViewListener(final Context ctx) {
		lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View item, int pos, long id) {
				String s = (String)items.get(pos);
				
				Log.i(TAG, "onItemClick => s: " + s + ", pos: " + pos);
				
				launchEditView(s,pos);
			}
		});
		
		lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
				String s = (String)items.get(pos);
				items.remove(pos);
				itemsAdapter.notifyDataSetChanged();
				writeItems();
				
				Log.i(TAG, "onItemLongClick => s: " + s);
				Toast.makeText(ctx, "Removing [" + s + "]", Toast.LENGTH_LONG).show();
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
		itemsAdapter.add(itemText);
		etNewItem.setText("");
		writeItems();
	}
	
	private void readItems() {
		File filesDir = getFilesDir();
		File toDoFile = new File(filesDir, "todo.txt");
		try {
			items = new ArrayList<String>(FileUtils.readLines(toDoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			Log.w(TAG, "!!! Caught an IOException: " + e.toString());
		}		
	}
	
	private void writeItems() {
		File filesDir = getFilesDir();
		File toDoFile = new File(filesDir, "todo.txt");
		try {
			FileUtils.writeLines(toDoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void launchEditView(String itemtext, int itemidx) {
		Intent i = new Intent(TodoActivity.this, EditItemActivity.class);
		i.putExtra("item", itemtext);
		i.putExtra("index", itemidx);
		
		//startActivity(i);
		
		Log.v(TAG, " launching EditItemActivity ... ");
		startActivityForResult(i, REQUEST_CODE);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode==RESULT_OK && requestCode==REQUEST_CODE) {
			String item = data.getExtras().getString("item");
			int index = data.getExtras().getInt("index");
			
			Log.i(TAG, "EditItemActivity returned: (" + item + "), (" + index + ")");
			
			items.set(index, item);
			itemsAdapter.notifyDataSetChanged();
			
			Toast.makeText(getApplicationContext(), "... saving to do list ...", Toast.LENGTH_SHORT).show();
			writeItems();
		}
	}
}
