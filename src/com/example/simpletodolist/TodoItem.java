package com.example.simpletodolist;

import java.util.Date;

public class TodoItem {
	private int id;
	private String body;
	private int year, month, day;

	public TodoItem(String body) {
		super();
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public void setDate(int y, int m, int d)
	{
		this.year = y; 
		this.month = m;
		this.day = d;
	}
	
	@SuppressWarnings("deprecation")
	public Date getDate()
	{
		if (this.year==0 || this.month==0 || this.day==0) return null;
		return new Date(this.year, this.month, this.day);
	}
	
	public String toString() {
		return body;
	}
	
}