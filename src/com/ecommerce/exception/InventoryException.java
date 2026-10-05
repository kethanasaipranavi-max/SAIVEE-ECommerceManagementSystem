package com.ecommerce.exception;
public class InventoryException extends ApplicationException {
    public InventoryException(String message) { super(message); }
    public InventoryException(String message, Throwable cause) { super(message, cause); }
}
