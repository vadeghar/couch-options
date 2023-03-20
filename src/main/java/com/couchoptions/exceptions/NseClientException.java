package com.couchoptions.exceptions;

public class NseClientException extends RuntimeException {
    public NseClientException(String msg) {
        super(msg);
    }

    public String getMessage() {
        return "NSE error: "+super.getMessage();
    }
}
