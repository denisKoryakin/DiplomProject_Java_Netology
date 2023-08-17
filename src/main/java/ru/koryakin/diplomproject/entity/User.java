package ru.koryakin.diplomproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private long userid;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String roles;

    //    добавление в List файла приводит к сохранению файла в свою таблицу из-за cascade = CascadeType.ALL
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<FileStorage> userFileStorages;

    public List<FileStorage> getUserFileStorage() {
        return userFileStorages;
    }

    public User(String userName, String password, String roles) {
        this.userName = userName;
        this.password = password;
        this.roles = roles;
    }
}
