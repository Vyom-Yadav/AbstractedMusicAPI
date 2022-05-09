package org.planetmusick.listen.abstractedmusicapi.model;

import java.util.Arrays;
import java.util.Objects;

public class Track {

    private String id;

    private String name;

    private Artist[] artists;

    private Album album;

    private String image;

    private String previewUrl;

    public Track() {
    }

    public Track(String id, String name, Artist[] artists, Album album, String image,
                 String previewUrl) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.album = album;
        this.image = image;
        this.previewUrl = previewUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Artist[] getArtists() {
        return artists;
    }

    public Album getAlbum() {
        return album;
    }

    public String getImage() {
        return image;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Track track)) {
            return false;
        }
        return id.equals(track.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Track{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", artists=" + Arrays.toString(artists) +
            ", album=" + album +
            ", image='" + image + '\'' +
            ", previewUrl='" + previewUrl + '\'' +
            '}';
    }
}
