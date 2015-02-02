package com.example.simpletodolist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.EditText;
import android.view.View;
import android.content.Intent;

public class EditItemActivity extends Activity {

	private static final String TAG = "EditItemActivity";
	
	private static int id = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int id = getIntent().getIntExtra("id", -1);
		String item = getIntent().getStringExtra("item");
		
		EditItemActivity.id = id;
		
		if (id < 0) {
			Log.e(TAG, "!!! index < 0 !!! : " + id);		
		}
		
		Log.i(TAG, "id: " + id + ", item: " + item);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		EditText edt = (EditText)findViewById(R.id.editText1);
		edt.setText(item);
	}
	
	public void onSave(View view) {		
		EditText edt = (EditText)findViewById(R.id.editText1);
		String item = edt.getText().toString();
		
		Intent data = new Intent();
		data.putExtra("id", EditItemActivity.id);
		data.putExtra("item", item);
				
		setResult(RESULT_OK, data);		
		this.finish();
	}
	
}
