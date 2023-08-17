package ru.koryakin.diplomproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import java.util.Set;
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

    @Transactional
    public ResponseEntity<?> uploadFile(String filename, MultipartFile file, Principal principal) {
        try {
            User user = userRepository.findByUserName(principal.getName()).orElseThrow();
            user.getUserFileStorage().add(new FileStorage(filename, file.getSize(), user));
            userRepository.save(user);
            if (fileRepository.existsByFileName(filename)) {
                fileRepository.uploadFile(filename, file, principal);
                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                throw new FileError("Не удалось загрузить файл");
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new ServerError("Упс!");
        }
    }

    @Transactional
    public ResponseEntity<?> deleteFile(String filename, Principal principal) {
        try {
            FileStorage fileToDelete = fileRepository.findByFileName(filename).orElseThrow();
            User user = userDeterminant(filename, principal);
            Set<FileStorage> removed = user.getUserFileStorage().stream()
                    .filter(fileStorage -> fileStorage.getFileName().equals(filename))
                    .collect(Collectors.toSet());
            user.getUserFileStorage().removeAll(removed);
            userRepository.save(user);
            if (!fileRepository.existsByFileName(filename)) {
                if (!fileRepository.deleteFile(fileToDelete, user.getUserName())) {
//                    пробрасываем исключения для rollback, если файл не найден в файловой системе
                    throw new FileError("Не удалось удалить файл");
                }
                return ResponseEntity.ok(HttpStatus.OK);
            } else {
                throw new FileError("Не удалось удалить файл");
            }
        } catch (RuntimeException ex) {
            throw new ServerError("Упс!");
        }
    }

    @Transactional
    public ResponseEntity<?> editFileName(String filename, String newFilename, Principal principal) {
        try {
            User user = userDeterminant(filename, principal);
            user.getUserFileStorage().stream()
                    .filter(fileStorage -> fileStorage.getFileName().equals(filename))
                    .forEach(fileStorage -> fileStorage.setFileName(newFilename));
            userRepository.save(user);
            if (fileRepository.existsByFileName(newFilename)) {
                if (!fileRepository.renameFile(filename, newFilename, user.getUserName())) {
                    throw new FileError("Не удалось переименовать файл");
                }
            }
            return ResponseEntity.ok(HttpStatus.OK);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw new ServerError("Упс!");
        }
    }

    public ByteArrayResource getFileByFilename(String filename, Principal principal) {
        try {
            User user = userDeterminant(filename, principal);
            return new ByteArrayResource(fileRepository.getFileByFilename(filename, user.getUserName()));
        } catch (RuntimeException ex) {
            throw new ServerError("Упс!");
        }
    }

    private User userDeterminant(String filename, Principal principal) {
        User user;
        if (jpaUserDetailsService.loadUserByUsername(principal.getName()).getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            long userid = fileRepository.findByFileName(filename).orElseThrow().getUser().getUserid();
            user = userRepository.findById(userid).orElseThrow();
        } else {
            user = userRepository.findByUserName(principal.getName()).orElseThrow();
        }
        return user;
    }
}

