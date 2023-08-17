package ru.koryakin.diplomproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.controller.model.request.EditRequest;
import ru.koryakin.diplomproject.exception.BadCredentials;
import ru.koryakin.diplomproject.service.FilesStorageService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
public class FilesStorageController {

    @Autowired
    FilesStorageService service;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping(path = "/list")
    public List<?> getAllFiles(@RequestParam("limit") String limit, Principal principal) {
        return service.getAllFiles(limit, principal);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PostMapping("/file")
    public ResponseEntity<?> uploadFile(@RequestParam("filename") String filename, MultipartFile file, Principal principal) {
        return service.uploadFile(filename, file, principal);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @DeleteMapping("/file")
    public ResponseEntity<?>deleteFile(@RequestParam("filename") String filename, Principal principal){
        return service.deleteFile(filename, principal);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @PutMapping("/file")
    public ResponseEntity<?> editFilename(@RequestParam("filename") String filename, @RequestBody EditRequest editRequest, Principal principal){
        return service.editFileName(filename,editRequest.filename(), principal);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/file")
    @ResponseBody
    public ByteArrayResource getFileByFilename(String filename, Principal principal){
        return service.getFileByFilename(filename, principal);
    }
}
