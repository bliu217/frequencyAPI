package org.frequency.frequencyapi.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Tag {

    @Id
    private String id;

    private String name;

    @ManyToMany
    private Set<Song> songs;

    public Tag() {

    }
}
