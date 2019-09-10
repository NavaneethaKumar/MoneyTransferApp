package com.banking.exception;



public class  BankingException extends Exception {
	
	private static final long serialVersionUID = 4664456874499611218L;

	private String httpStatusCode;
	
	public  BankingException() {
		super();
    }    
	public  BankingException(String httpStatusCode, String message){
        super(message);
        this.httpStatusCode = httpStatusCode;  
	}


}
