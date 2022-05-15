package org.planetmusick.listen.abstractedmusicapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public final class Album {
    private final String id;
    @JsonProperty("total_tracks")
    private final int totalTracks;
    private final Image[] images;
    private final String name;
    private String[] trackId;

    public Album(@JsonProperty("id") String id, @JsonProperty("total_tracks") int totalTracks,
                 @JsonProperty("images") Image[] images, @JsonProperty("name") String name) {
        this.id = id;
        this.totalTracks = totalTracks;
        this.images = images;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public Image[] getImages() {
        return images;
    }

    public String getName() {
        return name;
    }
    public String[] getTrackId() {
        return trackId;
    }

    public void setTrackId(String[] trackId) {
        this.trackId = trackId;
    }

    @Override
    public String toString() {
        return "Album{" +
            "id='" + id + '\'' +
            ", totalTracks=" + totalTracks +
            ", images=" + Arrays.toString(images) +
            ", name='" + name + '\'' +
            ", trackId=" + Arrays.toString(trackId) +
            '}';
    }
}
