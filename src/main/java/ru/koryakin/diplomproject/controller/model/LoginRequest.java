package ru.koryakin.diplomproject.controller.model;

import lombok.Data;

@Data
public class LoginRequest {

    String login;

    String password;
}
