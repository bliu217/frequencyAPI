package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@CompoundIndex(def = "{'name': 1, 'category': 1}", unique = true)
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    private String name;

    private String category;

    List<String> songIds;

    public Tag() {}
}
