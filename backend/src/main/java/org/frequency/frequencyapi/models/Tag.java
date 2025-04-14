package org.frequency.frequencyapi.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@CompoundIndex(name= "cmp_name_category", def = "{'name': 1, 'category': 1}", unique = true)
@Document(collection = "tags")
public class Tag {

    @Id
    private String id;

    @Indexed(name = "idx_tag_usageCount")
    private int usageCount;

    @TextIndexed
    private String name;

    @TextIndexed
    @Indexed(name = "idx_tag_category")
    private String category;

    public Tag(String name, String category) {
        setName(name);
        this.category = category;
    }

    public void setName(String name) {
        this.name = name.trim().toLowerCase();
        this.usageCount = 1;
    }

    public void incrementUsageCount() {
        this.usageCount++;
    }

    public void decrementUsageCount() {
        this.usageCount--;
    }

    public Tag() {}
}
