package com.ecommerce.exception;
public class OrderException extends ApplicationException {
    public OrderException(String message) { super(message); }
    public OrderException(String message, Throwable cause) { super(message, cause); }
}
