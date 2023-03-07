package com.job.delivery.exception;

public class TransactionException extends RuntimeException{

    public TransactionException(String text){
        super(text);
    }

}
