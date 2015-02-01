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
	
	private static int index = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String item = getIntent().getStringExtra("item");
		int index = getIntent().getIntExtra("index", -1);
		EditItemActivity.index = index;
		
		if (index < 0) {
			Log.e(TAG, "!!! index < 0 !!! : " + index);		
		}
		
		Log.i(TAG, "item: " + item + ", index: " + index);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		EditText edt = (EditText)findViewById(R.id.editText1);
		edt.setText(item);
	}
	
	public void onSave(View view) {		
		EditText edt = (EditText)findViewById(R.id.editText1);
		String item = edt.getText().toString();
		
		Intent data = new Intent();
		data.putExtra("item", item);
		data.putExtra("index", EditItemActivity.index);
		
		setResult(RESULT_OK, data);		
		this.finish();
	}
	
}
