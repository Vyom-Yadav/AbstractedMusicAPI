package org.planetmusick.listen.abstractedmusicapi.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
@RequestMapping("/tracks")
public class TracksController {

    private final RestApiConsumingUtil restApiConsumingUtil;

    private final JsonConvertorUtil jsonConvertorUtil;

    public TracksController(RestApiConsumingUtil restApiConsumingUtil,
                            JsonConvertorUtil jsonConvertorUtil) {
        this.restApiConsumingUtil = restApiConsumingUtil;
        this.jsonConvertorUtil = jsonConvertorUtil;
    }

    /**
     * Get track by ID.
     *
     * @param id ID of the track eg - 29m79w9xPMH4YCD6r8JSmV
     * @return {@link Track} associated with that ID
     */
    @GetMapping("/{id}")
    private Track getTrackById(@PathVariable String id) {
        try {
            final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
            queries.add("market", "IN");
            final String restApiCallOutput = restApiConsumingUtil.makeRestApiCall(List.of(id),
                                                                                  queries,
                                                                                  "tracks");
            return jsonConvertorUtil.getTrackFromString(restApiCallOutput);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get several tracks by providing several ID's.
     * Use {@link TracksController#getTrackById(String)} if you want to fetch a single track.
     *
     * @param ids Comma delimited list of ids. eg - sadkj3y2848324,32482398fwjfbsd5,e2348fwk4
     * @return An array of {@link Track} objects
     */
    @GetMapping("")
    private Track[] getSeveralTracksById(@RequestParam String ids) {
        final LinkedMultiValueMap<String, String> queries = new LinkedMultiValueMap<>();
        queries.add("market", "IN");
        final List<String> idList = Arrays.stream(ids.split(",")).toList();
        try {
            return jsonConvertorUtil.getMultipleTracksFromString(
                restApiConsumingUtil.makeRestApiCall(idList, queries,
                                                     "tracks"), idList.size());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
