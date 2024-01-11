package org.pbuczek.exception;


public class DuplicateIdException extends Exception {
    public DuplicateIdException(String errorMessage) {
        super(errorMessage);
    }
}