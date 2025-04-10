package org.frequency.frequencyapi.models;

import lombok.Getter;

@Getter
enum Visibility {
    PRIVATE("private"),
    PUBLIC("public");

    private final String value;

    Visibility(String s) {
        this.value = s;
    }
}
