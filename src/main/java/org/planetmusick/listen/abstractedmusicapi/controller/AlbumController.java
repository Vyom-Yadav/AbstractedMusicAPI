package org.planetmusick.listen.abstractedmusicapi.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.planetmusick.listen.abstractedmusicapi.model.Album;
import org.planetmusick.listen.abstractedmusicapi.model.Track;
import org.planetmusick.listen.abstractedmusicapi.util.JsonConvertorUtil;
import org.planetmusick.listen.abstractedmusicapi.util.RestApiConsumingUtil;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albums")
public class AlbumController {

    public static final String ENDPOINT = "albums";
    private final RestApiConsumingUtil restApiConsumingUtil;

    private final JsonConvertorUtil jsonConvertorUtil;


    public AlbumController(RestApiConsumingUtil restApiConsumingUtil,
                           JsonConvertorUtil jsonConvertorUtil) {
        this.restApiConsumingUtil = restApiConsumingUtil;
        this.jsonConvertorUtil = jsonConvertorUtil;
    }

    @GetMapping("/{id}")
    private Album getAlbumById(@PathVariable String id) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        queries.add("market", "IN");
        try {
            final String restApiCallOutput = restApiConsumingUtil.makeRestApiCall(List.of(id),
                                                                                  queries,
                                                                                  ENDPOINT);
            return jsonConvertorUtil.getAlbumFromString(restApiCallOutput);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("")
    private Album[] getSeveralAlbumsById(@RequestParam String ids) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        queries.add("market", "IN");
        final List<String> idList = Arrays.stream(ids.split(",")).toList();
        try {
            return jsonConvertorUtil.getMultipleAlbumsFromString(
                restApiConsumingUtil.makeRestApiCall(idList, queries, ENDPOINT));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/tracks")
    private Track[] getAlbumTracks(@PathVariable String id) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        queries.add("market", "IN");
        try {
            Track[] tracks = jsonConvertorUtil.getTracksFromAlbum(
                restApiConsumingUtil.makeRestApiCallWithAdditionalPath(id, queries, ENDPOINT,
                                                                       "tracks"));
            Arrays.stream(tracks).forEach(track -> track.setAlbumId(id));
            return tracks;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
