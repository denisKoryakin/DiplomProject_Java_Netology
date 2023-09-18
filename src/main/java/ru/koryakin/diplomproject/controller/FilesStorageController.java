package ru.koryakin.diplomproject.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.controller.DTO.request.EditRequest;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.exception.FileError;
import ru.koryakin.diplomproject.exception.ServerError;
import ru.koryakin.diplomproject.service.FilesStorageService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
public class FilesStorageController {

    @Autowired
    FilesStorageService service;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping(path = "/list")
    public List<?> getAllFiles(@RequestParam("limit") String limit, Principal principal) {
        try {
            return service.getAllFiles(limit, principal);
        } catch (RuntimeException ex) {
            log.error("File error: {}", ex.getMessage());
            throw new FileError("Не возможно отобразить файлы");
        }
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("filename") String filename, MultipartFile file, Principal principal) {
        try {
            FileStorage fileStorage = new FileStorage(filename, file.getSize(), null, file.getBytes());
            service.uploadFile(fileStorage, principal);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Server error: {}", ex.getMessage());
            throw new ServerError("Server error: Ошибка при загрузке файла");
        }
    }

    @RolesAllowed({"ADMIN", "USER"})
    @DeleteMapping("/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename, Principal principal) {
        try {
            service.deleteFile(filename, principal);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (RuntimeException ex) {
            log.error("Server error: {}", ex.getMessage());
            throw new ServerError("Server error: Ошибка при удалении файла");
        }
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PutMapping("/file")
    public ResponseEntity<?> editFilename(@RequestParam("filename") String filename, @RequestBody EditRequest editRequest, Principal principal) {
        try {
            service.editFileName(filename, editRequest.filename(), principal);
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (RuntimeException ex) {
            log.error("Server error: {}", ex.getMessage());
            throw new ServerError("Server error: Ошибка при редактировании файла");
        }
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/file")
    @ResponseBody
    public ByteArrayResource getFileByFilename(String filename, Principal principal) {
        return service.getFileByFilename(filename, principal);
    }
}
