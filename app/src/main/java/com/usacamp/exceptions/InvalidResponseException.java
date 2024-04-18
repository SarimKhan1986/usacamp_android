package com.usacamp.exceptions;

public class InvalidResponseException extends Exception {
	
	private String responseMessage;
	private String exceptionMessage;
	private int exceptionCode;
	private String uIMessage;
	
	public String getResponseMessage() {
		return responseMessage;
	}
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	public int getExceptionCode() {
		return exceptionCode;
	}
	public void setExceptionCode(int exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	public String getuIMessage() {
		return uIMessage;
	}
	public void setuIMessage(String uIMessage) {
		this.uIMessage = uIMessage;
	}
	
}
