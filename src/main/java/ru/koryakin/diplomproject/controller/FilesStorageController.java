package ru.koryakin.diplomproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.koryakin.diplomproject.service.FilesStorageService;

import java.security.Principal;

@RestController
@RequestMapping("/cloud")
public class FilesStorageController {

    @Autowired
    FilesStorageService service;

//    @PreAuthorize("hasAuthority('ADMIN') || hasAuthority('USER')")
    @GetMapping(path = "/list")
    public ResponseEntity<?> getFilesList(@RequestHeader("auth-token") String token, Principal principal) {
        return ResponseEntity.ok(service.getFilesList(token, principal));
    }
    // TODO: 07.08.2023 возвращать ResponseEntity или нет

//    @GetMapping("/list")
//    public String list(Principal principal) {
//        return principal.getName();
//    }
}
