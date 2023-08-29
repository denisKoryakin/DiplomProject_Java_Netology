package ru.koryakin.diplomproject.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.koryakin.diplomproject.controller.model.response.ExceptionResponse;
import ru.koryakin.diplomproject.exception.BadCredentials;
import ru.koryakin.diplomproject.exception.FileError;
import ru.koryakin.diplomproject.exception.ServerError;
import ru.koryakin.diplomproject.exception.Unauthorized;

/** Класс обработчик исключений */

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadCredentials.class)
    public ResponseEntity<?> BadCredentialsHandler(BadCredentials ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Unauthorized.class)
    public ResponseEntity<?> UnauthorizedHandler(Unauthorized ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServerError.class)
    public ResponseEntity<Object> ServerError(ServerError ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FileError.class)
    public ResponseEntity<Object> FileError(FileError ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
