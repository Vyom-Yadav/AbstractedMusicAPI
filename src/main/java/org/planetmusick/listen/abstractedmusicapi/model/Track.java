package org.planetmusick.listen.abstractedmusicapi.model;

public record Track(String id, String name,
                    Artist[] artists, Album album,
                    String image, String previewUrl) {
}
