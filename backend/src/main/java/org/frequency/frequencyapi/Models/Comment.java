package org.frequency.frequencyapi.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String text;
    private int likes;

    @ManyToOne
    private User author;
    private Date date;

    @ManyToOne
    private Post post;

    public Comment() {}
}
