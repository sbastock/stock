package com.stocksba.stock.model;

import java.util.ArrayList;

public class SerieDate {
	private String name;
	private String type;
	private ArrayList<Double> data;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<Double> getData() {
		return data;
	}
	public void setData(ArrayList<Double> data) {
		this.data = data;
	}
	

}
