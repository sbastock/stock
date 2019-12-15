package com.stocksba.stock.model;

import java.util.ArrayList;

public class SeriesData {
	private String companycode;
	private String companyname;
	private String type;
	private ArrayList<Double> prices;

	public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<Double> getPrices() {
		return prices;
	}

	public void setPrices(ArrayList<Double> prices) {
		this.prices = prices;
	}

}
