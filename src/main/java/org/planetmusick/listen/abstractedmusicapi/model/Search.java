package org.planetmusick.listen.abstractedmusicapi.model;

public record Search(Track[] tracks, Artist[] artists,
                     Album[] albums, Playlist[] playlists) {
}
