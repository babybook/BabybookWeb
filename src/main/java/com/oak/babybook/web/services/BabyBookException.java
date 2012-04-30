package com.oak.babybook.web.services;

public class BabyBookException extends Exception {

	private static final long serialVersionUID = 8074010252828744187L;

	public BabyBookException(String message){
		super(message);
	}

	public BabyBookException(Exception e) {
		super(e);
	}
}
