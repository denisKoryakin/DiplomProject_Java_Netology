package ru.koryakin.diplomproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Transient
    byte[] bytes;

    //    сторона - владелец
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_userid", nullable = false)
    private User user;

    public FileStorage(String fileName, double size, User user, byte[] bytes) {
        this.fileName = fileName;
        this.size = size;
        this.user = user;
        this.bytes = bytes;
    }
}
