package com.stocksba.stock.model;

public class StockPrice {

	private Integer id;
	private String companycode;
	private String stockexchange;
	private Double ppr;
	private String stockdate;
	private String stocktime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCompanycode() {
		return companycode;
	}

	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}

	public String getStockexchange() {
		return stockexchange;
	}

	public void setStockexchange(String stockexchange) {
		this.stockexchange = stockexchange;
	}

	public Double getPpr() {
		return ppr;
	}

	public void setPpr(Double ppr) {
		this.ppr = ppr;
	}

	public String getStockdate() {
		return stockdate;
	}

	public void setStockdate(String stockdate) {
		this.stockdate = stockdate;
	}

	public String getStocktime() {
		return stocktime;
	}

	public void setStocktime(String stocktime) {
		this.stocktime = stocktime;
	}

}
