package ru.koryakin.diplomproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileStorage {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String fileName;

    private double size;

    //    сторона - владелец
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_userid", nullable = false)
    private User user;

    public FileStorage(String fileName, double size, User user) {
        this.fileName = fileName;
        this.size = size;
        this.user = user;
    }
}
