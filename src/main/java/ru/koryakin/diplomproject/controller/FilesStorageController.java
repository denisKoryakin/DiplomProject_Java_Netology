package ru.koryakin.diplomproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public List<?>uploadFile(@RequestParam("filename") String filename, MultipartFile file, Principal principal) {
        return service.uploadFile(filename,file, principal);
//        return ResponseEntity.ok(HttpStatus.OK);
    }

//    @RolesAllowed({"ADMIN", "USER"})
//    @DeleteMapping("/file")
//    public ResponseEntity<?>deleteFile(@RequestParam("filename") String filename){
//        service.deleteFile(filename);
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @RolesAllowed({"ADMIN", "USER"})
//    @PutMapping("/file")
//    public ResponseEntity<?> editFilename(EditRequest editRequest){
//        boolean b = service.editFileName(editRequest.getFilename(),editRequest.getNewFilename());
//        return ResponseEntity.ok(HttpStatus.OK);
//    }
//
//    @RolesAllowed({"ADMIN", "USER"})
//    @GetMapping("/file")
//    @ResponseBody
//    public ByteArrayResource getFile(String filename){
//        byte[] file = service.getFile(filename);
//        return new ByteArrayResource(file);
//    }
}
