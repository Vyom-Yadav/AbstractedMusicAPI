package org.planetmusick.listen.abstractedmusicapi.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.planetmusick.listen.abstractedmusicapi.model.Artist;
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
@RequestMapping("/artists")
public class ArtistController {

    public static final String ENDPOINT = "artists";
    private final RestApiConsumingUtil restApiConsumingUtil;

    private final JsonConvertorUtil jsonConvertorUtil;

    public ArtistController(RestApiConsumingUtil restApiConsumingUtil,
                            JsonConvertorUtil jsonConvertorUtil) {
        this.restApiConsumingUtil = restApiConsumingUtil;
        this.jsonConvertorUtil = jsonConvertorUtil;
    }

    @GetMapping("/{id}")
    private Artist getArtistById(@PathVariable String id) {
        try {
            final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
            final String restApiCallOutput = restApiConsumingUtil.makeRestApiCall(List.of(id),
                                                                                  queries,
                                                                                  ENDPOINT);
            return jsonConvertorUtil.getArtistFromString(restApiCallOutput);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("")
    private Artist[] getSeveralArtistsById(@RequestParam String ids) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        final List<String> idList = Arrays.stream(ids.split(",")).toList();
        try {
            return jsonConvertorUtil.getMultipleArtistsFromString(
                restApiConsumingUtil.makeRestApiCall(idList, queries, ENDPOINT), idList.size());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/{id}/top-tracks")
    private Track[] getArtistsTopTracks(@PathVariable String id) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        queries.add("market", "IN");
        try {
            return jsonConvertorUtil.getMultipleTracksFromString(
                restApiConsumingUtil.makeRestApiCallWithAdditionalPath(id, queries,
                                                                       ENDPOINT, "top-tracks"));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
