package org.planetmusick.listen.abstractedmusicapi.util;

import java.io.IOException;
import java.util.Arrays;

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

    public Track getTrackFromString(String jsonOutput) throws IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput);
        Artist[] artists = objectMapper.treeToValue(jsonNode.path("artists"), Artist[].class);

        final Track track = objectMapper.readValue(jsonOutput, Track.class);
        String[] artistIds = Arrays.stream(artists)
            .map(Artist::id)
            .toArray(String[]::new);
        track.setArtistId(artistIds);
        final String albumId = jsonNode.path("album").path("id").asText();
        track.setAlbumId(albumId);
        return track;
    }

    public Track[] getMultipleTracksFromString(String jsonOutput) throws
                                                                  IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput).path("tracks");
        Track[] tracks = new Track[jsonNode.size()];
        for (int i = 0; i < jsonNode.size(); i++) {
            tracks[i] = getTrackFromString(jsonNode.get(i).toString());
        }
        return tracks;
    }

    public Artist getArtistFromString(String jsonOutput) throws IOException {
        return objectMapper.readValue(jsonOutput, Artist.class);
    }

    public Artist[] getMultipleArtistsFromString(String jsonOutput, int noOfArtists) throws
                                                                                     IOException {
        final JsonNode jsonNode = objectMapper.readTree(jsonOutput).path("artists");
        Artist[] artists = new Artist[noOfArtists];
        for (int i = 0; i < noOfArtists; i++) {
            artists[i] = getArtistFromString(jsonNode.get(i).toString());
        }
        return artists;
    }

}
