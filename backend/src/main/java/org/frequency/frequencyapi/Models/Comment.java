package org.frequency.frequencyapi.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    private String id;
    private String text;
    private int likes;

    @ManyToOne
    private User author;
    private Date date;

    @ManyToOne
    private Post post;

    public Comment() {}
}
