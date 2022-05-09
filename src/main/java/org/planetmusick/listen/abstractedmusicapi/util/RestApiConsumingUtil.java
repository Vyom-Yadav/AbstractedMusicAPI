package org.planetmusick.listen.abstractedmusicapi.util;

import java.io.IOException;

import org.planetmusick.listen.abstractedmusicapi.SpotifyConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RestApiConsumingUtil {

    private final static String WEB_API_BASE_URL = "https://api.spotify.com/v1";
    private final TokenRegenerationUtil tokenRegenerationUtil;

    private final SpotifyConfigProperties spotifyConfigProperties;

    private final WebClient.Builder builder;

    public RestApiConsumingUtil(TokenRegenerationUtil tokenRegenerationUtil,
                                SpotifyConfigProperties spotifyConfigProperties,
                                WebClient.Builder builder) {
        this.tokenRegenerationUtil = tokenRegenerationUtil;
        this.spotifyConfigProperties = spotifyConfigProperties;
        this.builder = builder;
    }

    public String makeRestApiCall(String id, String endpoint) throws IOException {
        final String uri = "/" + endpoint + "/" + id;
        if (spotifyConfigProperties.authToken() == null) {
            tokenRegenerationUtil.regenerateToken();
        }
        String response = makeRestApiCall(uri);
        if (response.contains("The access token expired")) {
            tokenRegenerationUtil.regenerateToken();
        }
        response = makeRestApiCall(uri);
        return response;
    }

    private String makeRestApiCall(String uri) {
        return builder
            .baseUrl(WEB_API_BASE_URL)
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add("Authorization", "Bearer " + spotifyConfigProperties.authToken());
                httpHeaders.add("Content-Type", "application/json");
            })
            .build()
            .get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }


}
