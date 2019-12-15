package com.stocksba.stock.rspmodel;

import javax.validation.constraints.NotNull;

import com.stocksba.stock.payload.UploadFileResponse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Response Model")
public class RspModel {
	@ApiModelProperty(notes = "Response Code", example = "200", required = true, dataType = "java.lang.Integer")
	@NotNull
	private Integer code;

	@ApiModelProperty(notes = "Response Message", example = "Ok", required = true, dataType = "java.lang.String")
	@NotNull
	private String message;

	@ApiModelProperty(notes = "Response Data", required = true, dataType = "java.lang.Object")
	private UploadFileResponse data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public UploadFileResponse getData() {
		return data;
	}

	public void setData(UploadFileResponse data) {
		this.data = data;
	}
	
	

}

