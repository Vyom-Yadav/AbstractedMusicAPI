package org.planetmusick.listen.abstractedmusicapi.util;

import java.io.IOException;
import java.util.Arrays;

import org.planetmusick.listen.abstractedmusicapi.model.Album;
import org.planetmusick.listen.abstractedmusicapi.model.Artist;
import org.planetmusick.listen.abstractedmusicapi.model.Track;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class JsonConvertorUtil {

    private final ObjectMapper objectMapper;

    public JsonConvertorUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    //------------------------------------------------------------------------------------------------//

    /* Helper Methods working with TRACKS ---start--- */
    public Track getTrackFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput);
        Artist[] artists = objectMapper.treeToValue(jsonNode.path("artists"), Artist[].class);

        final Track track = objectMapper.readValue(jsonOutput, Track.class);
        String[] artistIds = Arrays.stream(artists)
            .map(Artist::id)
            .toArray(String[]::new);
        track.setArtistId(artistIds);
        final String albumId = jsonNode.path("album").path("id").asText();
        if (!albumId.isBlank()) {
            track.setAlbumId(albumId);
        }
        return track;
    }

    public Track[] getMultipleTracksFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput).path("tracks");
        return getMultipleTracksFromNode(jsonNode);
    }

    public Track[] getTracksFromAlbum(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput).path("items");
        return getMultipleTracksFromNode(jsonNode);
    }

    private Track[] getMultipleTracksFromNode(JsonNode jsonNode) throws IOException {
        Track[] tracks = new Track[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            tracks[i] = getTrackFromString(jsonNode.get(i).toString());
        }
        return tracks;
    }

    /* Helper Methods ---end--- */

    //------------------------------------------------------------------------------------------------//

    /* Helper Methods working with ARTIST ---start--- */
    public Artist getArtistFromString(String jsonOutput) throws IOException {
        return objectMapper.readValue(jsonOutput, Artist.class);
    }

    public Artist[] getMultipleArtistsFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput).path("artists");
        Artist[] artists = new Artist[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            artists[i] = getArtistFromString(jsonNode.get(i).toString());
        }
        return artists;
    }

    /* Helper Methods ---end--- */

    //------------------------------------------------------------------------------------------------//

    /* Helper Methods working with ALBUM ---start--- */
    public Album getAlbumFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput)
            .path("tracks")
            .path("items");
        return getAlbumFromNode(jsonNode, jsonOutput);
    }

    private Album getAlbumFromNode(JsonNode jsonNode, String jsonOutput) throws IOException {
        final String[] trackId = Arrays.stream(getMultipleTracksFromNode(jsonNode))
            .map(Track::getId)
            .toArray(String[]::new);
        Album album = objectMapper.readValue(jsonOutput, Album.class);
        album.setTrackId(trackId);
        return album;
    }

    public Album[] getMultipleAlbumsWithoutTracksFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput)
            .path("items");
        Album[] artistAlbums = new Album[jsonNode.size()];
        for (int i = 0; i < artistAlbums.length; i++) {
            JsonNode node = jsonNode.get(i);
            artistAlbums[i] = objectMapper.readValue(node.toString(), Album.class);
        }
        return artistAlbums;
    }

    public Album[] getMultipleAlbumsFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput)
            .path("albums");
        Album[] artistAlbums = new Album[jsonNode.size()];
        for (int i = 0; i < artistAlbums.length; i++) {
            JsonNode node = jsonNode.get(i);
            artistAlbums[i] = getAlbumFromNode(
                node.path("tracks")
                    .path("items"),
                node.toString());
        }
        return artistAlbums;
    }

    /* Helper Methods ---end--- */

    //------------------------------------------------------------------------------------------------//

}
