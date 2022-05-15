package org.planetmusick.listen.abstractedmusicapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "name",
    "artistId",
    "albumId",
    "previewUrl"
})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Track {
    private final String id;
    private final String name;
    private String[] artistId;
    private String albumId;
    @JsonProperty("preview_url")
    private final String previewUrl;

    public Track(@JsonProperty("id") String id, @JsonProperty("name") String name,
                 @JsonProperty("preview_url") String previewUrl) {
        this.id = id;
        this.name = name;
        this.previewUrl = previewUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getArtistId() {
        return artistId;
    }

    public void setArtistId(String[] artistId) {
        this.artistId = artistId;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    @Override
    public String toString() {
        return "Track{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", artistId=" + Arrays.toString(artistId) +
            ", albumId='" + albumId + '\'' +
            ", previewUrl='" + previewUrl + '\'' +
            '}';
    }
}
