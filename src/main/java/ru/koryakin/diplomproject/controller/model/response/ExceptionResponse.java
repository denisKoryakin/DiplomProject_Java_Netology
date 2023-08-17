package ru.koryakin.diplomproject.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {

    private String message;
    private int id;
}
