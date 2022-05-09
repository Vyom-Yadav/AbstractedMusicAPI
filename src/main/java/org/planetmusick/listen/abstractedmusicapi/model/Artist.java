
package org.planetmusick.listen.abstractedmusicapi.model;

public record Artist(String id, String name,
                     String[] images, Track[] topTracks,
                     Album[] albums) {
}
