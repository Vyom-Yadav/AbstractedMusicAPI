package org.planetmusick.listen.abstractedmusicapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "name",
    "images",
    "trackId",
    "totalTracks",
    "artistId"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record Album(String id, int totalTracks,
                    String[] images, String name,
                    String[] artistId, String[] trackId) {
}
