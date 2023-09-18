package ru.koryakin.diplomproject.controller.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {

    @JsonProperty("auth-token")
    String token;
}
