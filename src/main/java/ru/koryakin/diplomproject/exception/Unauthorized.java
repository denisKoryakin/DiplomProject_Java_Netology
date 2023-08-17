package ru.koryakin.diplomproject.exception;

public class Unauthorized extends RuntimeException {

    public Unauthorized(String message) {
        super(message);
    }
}
