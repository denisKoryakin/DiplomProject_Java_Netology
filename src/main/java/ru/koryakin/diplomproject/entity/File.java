package ru.koryakin.diplomproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true, unique = true)
    private String fileName;

    private double size;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    public File(String fileName, double size) {
        this.fileName = fileName;
        this.size = size;
    }
}
