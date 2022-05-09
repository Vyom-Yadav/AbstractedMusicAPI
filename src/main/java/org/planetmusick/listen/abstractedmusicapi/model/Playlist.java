package org.planetmusick.listen.abstractedmusicapi.model;

public record Playlist(String id, String name,
                       String[] images, Track[] tracks) {
}
