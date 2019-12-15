package com.stocksba.stock.model;

public class ComparePrice {
	
	private String companycode;
	private Double price;
	private String stockdate;
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getStockdate() {
		return stockdate;
	}
	public void setStockdate(String stockdate) {
		this.stockdate = stockdate;
	}
	public ComparePrice(String companycode, Double price, String stockdate) {
		super();
		this.companycode = companycode;
		this.price = price;
		this.stockdate = stockdate;
	}
	public ComparePrice() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ComparePrice [companycode=" + companycode + ", price=" + price + ", stockdate=" + stockdate + "]";
	}
	
	
	
	

}
