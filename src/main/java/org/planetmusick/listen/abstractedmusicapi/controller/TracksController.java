package org.planetmusick.listen.abstractedmusicapi.controller;

import java.io.IOException;

import org.planetmusick.listen.abstractedmusicapi.model.Track;
import org.planetmusick.listen.abstractedmusicapi.util.RestApiConsumingUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tracks")
public class TracksController {

    private final RestApiConsumingUtil restApiConsumingUtil;

    public TracksController(RestApiConsumingUtil restApiConsumingUtil) {
        this.restApiConsumingUtil = restApiConsumingUtil;
    }

    @GetMapping("/{id}")
    private Track getTrackById(@PathVariable String id) {
        try {
            final String responseFromSpotifyApi =
                restApiConsumingUtil.makeRestApiCall(id, "tracks");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
