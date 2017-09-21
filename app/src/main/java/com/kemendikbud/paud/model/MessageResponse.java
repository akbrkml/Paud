package com.kemendikbud.paud.model;

public class MessageResponse{

	private boolean error;
	private String message;

	public MessageResponse(){
	}

	public void setError(boolean error){
		this.error = error;
	}

	public boolean isError(){
		return error;
	}

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}
}