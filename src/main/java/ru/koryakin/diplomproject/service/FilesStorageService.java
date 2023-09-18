package ru.koryakin.diplomproject.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import ru.koryakin.diplomproject.controller.DTO.response.FileResponse;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.exception.FileError;
import ru.koryakin.diplomproject.exception.ServerError;
import ru.koryakin.diplomproject.repository.FileRepository;
import ru.koryakin.diplomproject.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilesStorageService {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JpaUserDetailsService jpaUserDetailsService;

    public List<Object> getAllFiles(String limit, Principal principal) {
        int limitInt = Integer.parseInt(limit);
        if (jpaUserDetailsService.loadUserByUsername(principal.getName()).getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return fileRepository.findAllBy().stream()
                    .limit(limitInt)
                    .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                    .collect(Collectors.toList());
        } else {
            return fileRepository.findAllBy().stream()
                    .filter(fileStorage -> Objects.equals(principal.getName(), fileStorage.getUser().getUserName()))
                    .limit(limitInt)
                    .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    public void uploadFile(FileStorage fileStorage, Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).orElseThrow();
        fileStorage.setUser(user);
        user.getUserFileStorage().add(fileStorage);
        userRepository.save(user);
        if (fileRepository.existsByFileName(fileStorage.getFileName())) {
            fileRepository.uploadFile(fileStorage, principal.getName());
        } else {
            log.error("File error: Не удалось загрузить файл");
            throw new FileError("Не удалось загрузить файл");
        }
    }

    @Transactional
    public void deleteFile(String filename, Principal principal) {
        FileStorage fileToDelete = fileRepository.findByFileName(filename).orElseThrow();
        User user = userDeterminant(filename, principal);
        Set<FileStorage> removed = user.getUserFileStorage().stream()
                .filter(fileStorage -> fileStorage.getFileName().equals(filename))
                .collect(Collectors.toSet());
        user.getUserFileStorage().removeAll(removed);
        userRepository.save(user);
        if (!fileRepository.existsByFileName(filename)) {
            if (!fileRepository.deleteFile(fileToDelete, user.getUserName())) {
//              пробрасываем исключения для rollback, если файл не найден в файловой системе
                log.error("File error: Не удалось удалить файл");
                throw new FileError("Не удалось удалить файл");
            }
        } else {
            log.error("File error: Не удалось удалить файл");
            throw new FileError("Не удалось удалить файл");
        }
    }

    @Transactional
    public void editFileName(String filename, String newFilename, Principal principal) {
        User user = userDeterminant(filename, principal);
        user.getUserFileStorage().stream()
                .filter(fileStorage -> fileStorage.getFileName().equals(filename))
                .forEach(fileStorage -> fileStorage.setFileName(newFilename));
        userRepository.save(user);
        if (fileRepository.existsByFileName(newFilename)) {
            if (!fileRepository.renameFile(filename, newFilename, user.getUserName())) {
                log.error("File error: Не удалось переименовать файл");
                throw new FileError("Не удалось переименовать файл");
            }
        }
    }

    public ByteArrayResource getFileByFilename(String filename, Principal principal) {
        try {
            User user = userDeterminant(filename, principal);
            return new ByteArrayResource(fileRepository.getFileByFilename(filename, user.getUserName()));
        } catch (RuntimeException ex) {
            log.error("Server error: {}", ex.getMessage());
            throw new ServerError("Server error: Ошибка при скачивании файла");
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

