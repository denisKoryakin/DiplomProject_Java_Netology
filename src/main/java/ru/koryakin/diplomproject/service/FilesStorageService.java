package ru.koryakin.diplomproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.controller.model.response.FileResponse;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.exception.FileError;
import ru.koryakin.diplomproject.exception.ServerError;
import ru.koryakin.diplomproject.repository.FileRepository;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilesStorageService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    public List<Object> getAllFiles(String limit, Principal principal) {
        try {
            int limitInt = Integer.parseInt(limit);
            if (jpaUserDetailsService.loadUserByUsername(principal.getName()).getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                return fileRepository.findAllBy().stream()
                        .limit(limitInt)
                        .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                        .collect(Collectors.toList());
            } else {
                return fileRepository.findAllBy().stream()
                        .filter(fileStorage -> fileStorage.getUser().getUserName().equals(principal.getName()))
                        .limit(limitInt)
                        .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                        .collect(Collectors.toList());
            }
        } catch (RuntimeException ex) {
            throw new FileError("Не возможно отобразить файлы");
        }
    }

    public ResponseEntity<?> uploadFile(String filename, MultipartFile file, Principal principal) {
        try {
            if (fileRepository.uploadFile(filename, file, principal)) {
                User user = userRepository.findByUserName(principal.getName()).get();
                user.getUserFileStorage().add(new FileStorage(filename, file.getSize(), user));
                userRepository.save(user);
                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                throw new FileError("Не удалось загрузить файл");
            }
        } catch (RuntimeException ex) {
            throw new ServerError("Упс!");
        }
    }

    public ResponseEntity<?> deleteFile(String filename, Principal principal) {
        try {
            FileStorage fileToDelete = fileRepository.findByFileName(filename).isPresent() ? fileRepository.findByFileName(filename).get() : null;
            if (fileToDelete != null) {
                if (fileRepository.deleteFile(fileToDelete, principal)) {
                    User user = userDeterminant(filename, principal);
                    user.getUserFileStorage().remove(new FileStorage(filename, fileToDelete.getSize(), user));
                    userRepository.save(user);
                    return ResponseEntity.ok(HttpStatus.OK);
                } else {
                    throw new FileError("Не удалось удалить файл");
                }
            } else {
                throw new FileError("Не удалось удалить файл");
            }
        } catch (RuntimeException ex) {
            throw new ServerError("Упс!");
        }
    }

    public ResponseEntity<?> editFileName(String filename, String newFilename, Principal principal) {
        try {
            FileStorage fileToEdit = fileRepository.findByFileName(filename).isPresent() ? fileRepository.findByFileName(filename).get() : null;
            if (fileToEdit != null) {
                User user = userDeterminant(filename, principal);
                user.getUserFileStorage().stream()
                        .filter(fileStorage -> fileStorage.getFileName().equals(filename))
                        .forEach(fileStorage -> fileStorage.setFileName(newFilename));
                userRepository.save(user);
                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                throw new FileError("Не удалось изменить имя файла");
            }
        } catch (RuntimeException ex) {
            throw new ServerError("Упс!");
        }
    }

    private User userDeterminant(String filename, Principal principal) {
        User user;
        if (jpaUserDetailsService.loadUserByUsername(principal.getName()).getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            long userid = fileRepository.findByFileName(filename).get().getUser().getUserid();
            user = userRepository.findById(userid).get();
        } else {
            user = userRepository.findByUserName(principal.getName()).get();
        }
        return user;
    }
}
