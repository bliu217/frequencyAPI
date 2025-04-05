package org.frequency.frequencyapi.Models;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private UUID id;

    @Column(unique=true)
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column(unique=true)
    @NotNull
    private String email;

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User() {

    }
}
