package com.example.simpletodolist;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

public class TodoItemsAdapter<T> extends ArrayAdapter<T> {

	private static final String TAG = "TodoItemsAdapter";
	
	public TodoItemsAdapter(Context context, ArrayList<T> items) {
		super(context, 0, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		TodoItem item = (TodoItem) getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
		}
		
		// Lookup view for data population
		final TextView todo = (TextView) convertView.findViewById(R.id.todo_text);
		final TextView changeDate = (TextView) convertView.findViewById(R.id.date);
		
		// Populate the data into the template view using the data object		
		todo.setText(item.getBody());		
		if (item.getDate()!=null) {
			Date d=item.getDate();
			changeDate.setText(String.format("%d/%d/%d",d.getMonth()+1,d.getDay(),d.getYear()));
		} 
		
		changeDate.setOnClickListener(new OnClickListener() {						
			@Override
			public void onClick(View arg0) {
				final Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				int day = c.get(Calendar.DAY_OF_MONTH);
				
				Dialog d = new DatePickerDialog(getContext(), new myDatePickListener(changeDate), year, month, day);
				d.show();
			}
		});
		
		// Return the completed view to render on screen
		return convertView;
	}

}

class myDatePickListener implements DatePickerDialog.OnDateSetListener {
	private final TextView date;
	public myDatePickListener(TextView d) { this.date = d; }
		
	@Override
	public void onDateSet(DatePicker view, int year, int mon,
			int day) {
		date.setText(String.format("%d/%d/%d", mon+1, day, year));		
	}	
}

