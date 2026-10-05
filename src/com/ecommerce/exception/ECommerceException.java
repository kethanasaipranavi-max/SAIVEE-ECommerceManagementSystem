package com.ecommerce.exception;

public class ECommerceException extends Exception {

    public ECommerceException() {
        super();
    }

    public ECommerceException(String message) {
        super(message);
    }

    public ECommerceException(String message, Throwable cause) {
        super(message, cause);
    }
}