package org.frequency.frequencyapi.payloads;

import java.util.UUID;

public record UserSummary(UUID id, String username, String profilePictureUrl) {

}
