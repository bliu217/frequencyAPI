package org.frequency.frequencyapi.util;

import lombok.Getter;

@Getter
public enum Visibility {
    PRIVATE("private"),
    PUBLIC("public");

    private final String value;

    Visibility(String s) {
        this.value = s;
    }
}
