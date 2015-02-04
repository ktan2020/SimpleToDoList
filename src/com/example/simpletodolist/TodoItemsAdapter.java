package com.example.simpletodolist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TodoItemsAdapter<T> extends ArrayAdapter<T> {

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
		TextView todo = (TextView) convertView.findViewById(R.id.todo_text);
		//TextView tvHome = (TextView) convertView.findViewById(R.id.tvHometown);
		// Populate the data into the template view using the data object
		
		todo.setText(item.getBody());
		//tvHome.setText(user.hometown);
		
		// Return the completed view to render on screen
		return convertView;
	}
	
}
