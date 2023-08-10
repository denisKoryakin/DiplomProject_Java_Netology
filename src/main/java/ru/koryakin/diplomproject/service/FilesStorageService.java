package ru.koryakin.diplomproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.koryakin.diplomproject.controller.model.FileResponse;
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

    public List<FileResponse> getFilesList(String token, Principal principal) {
        if (jpaUserDetailsService.loadUserByUsername(principal.getName()).getAuthorities().equals("ADMIN")) {
            return fileRepository.findAllBy().stream()
                    .map(file -> new FileResponse(file.getFileName(), file.getSize()))
                    .collect(Collectors.toList());
        } else {
            return fileRepository.findAllBy().stream()
                    .filter(file -> file.getUser().getUserName().equals(principal.getName()))
                    .map(file -> new FileResponse(file.getFileName(), file.getSize()))
                    .collect(Collectors.toList());
        }
    }
}
