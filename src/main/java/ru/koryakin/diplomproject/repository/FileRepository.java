package ru.koryakin.diplomproject.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.koryakin.diplomproject.entity.FileStorage;
import ru.koryakin.diplomproject.exception.FileError;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<FileStorage, Long> {
    List <FileStorage> findAllBy();

    default boolean uploadFile(String filename, MultipartFile file, Principal principal) {
        File dir = new File("C://CloudDir/" + principal.getName());
        File file1 = new File(dir + "/" + filename);
        dir.mkdirs();
        try (FileOutputStream fos = new FileOutputStream(file1, false)) {
            fos.write(file.getBytes());
            return Arrays.asList(dir.listFiles()).contains(file1);
        } catch (RuntimeException | IOException ex) {
            throw new FileError("Не удалось записать файл");
        }
    }
}
