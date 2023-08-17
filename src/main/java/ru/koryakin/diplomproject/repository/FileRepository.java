package ru.koryakin.diplomproject.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.entity.User;
import ru.koryakin.diplomproject.exception.FileError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends CrudRepository<FileStorage, Long> {

    List <FileStorage> findAllBy();

    Optional<FileStorage> findByFileName(String filename);

    boolean existsByFileName(String filename);

    default boolean uploadFile(String filename, MultipartFile file, Principal principal) {
        File dir = new File("C://CloudDir/" + principal.getName());
        File file1 = new File(dir + "/" + filename);
        dir.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file1, false)) {
            fos.write(file.getBytes());
            return Arrays.asList(dir.listFiles()).contains(file1);
        } catch (RuntimeException | IOException ex) {
            return false;
        }
    }

    default boolean deleteFile(FileStorage fileToDelete, String username) {
        Path path = Paths.get("C://CloudDir/" + username + "/" + fileToDelete.getFileName());
        try {
            Files.delete(path);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    default byte[] getFileByFilename(String filename, String username) {
        Path path = Paths.get("C://CloudDir/" + username + "/" + filename);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            throw new FileError("Не удалось загрузить файл");
        }
    }

    default boolean renameFile(String filename, String newFilename, String username) {
        File file1 = new File("C://CloudDir/" + username + "/" + filename);
        File file2 = new File("C://CloudDir/" + username + "/" + newFilename);
        file1.renameTo(file2);
        return file2.exists();
    }
}
