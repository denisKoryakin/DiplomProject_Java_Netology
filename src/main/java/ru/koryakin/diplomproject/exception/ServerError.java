package ru.koryakin.diplomproject.exception;

public class ServerError extends RuntimeException {

    public ServerError(String message) {
        super(message);
    }
}
