package org.planetmusick.listen.abstractedmusicapi.model;

public record Album(String id, int totalTracks,
                    String[] images, String name,
                    Artist[] artists, Track[] tracks) {
}
