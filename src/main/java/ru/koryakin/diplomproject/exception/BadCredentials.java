package ru.koryakin.diplomproject.exception;

public class BadCredentials extends RuntimeException{

    public BadCredentials(String message) {
        super(message);
    }
}
