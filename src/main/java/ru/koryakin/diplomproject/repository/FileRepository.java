package ru.koryakin.diplomproject.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.koryakin.diplomproject.entity.File;

import java.util.List;

@Repository
public interface FileRepository extends CrudRepository<File, Long> {
    List <File> findAllBy();
}
