package ru.koryakin.diplomproject.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Arrays;
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
                        .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                        .limit(limitInt)
                        .collect(Collectors.toList());
            } else {
                return fileRepository.findAllBy().stream()
                        .filter(fileStorage -> fileStorage.getUser().getUserName().equals(principal.getName()))
                        .map(fileStorage -> new FileResponse(fileStorage.getFileName(), fileStorage.getSize()))
                        .limit(limitInt)
                        .collect(Collectors.toList());
            }
        } catch (RuntimeException ex) {
            throw new ServerError("Не возможно отобразить файлы");
        }
    }

    public List<Object> uploadFile(String filename, MultipartFile file, Principal principal) {
        if (fileRepository.uploadFile(filename, file, principal)) {
            User user = userRepository.findByUserName(principal.getName()).get();
            fileRepository.save(new FileStorage(filename, file.getSize(), user));
            user.getUserFileStorages().add(new FileStorage(filename, file.getSize(), user));
            return getAllFiles("3", principal);

            // TODO: 17.08.2023 лимит указан вручную, что не верно

        } else {
            throw new FileError("Не удалось записать файл");
        }
    }
}
