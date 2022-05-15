package org.planetmusick.listen.abstractedmusicapi.util;

import java.io.IOException;
import java.util.List;

import org.planetmusick.listen.abstractedmusicapi.SpotifyConfigProperties;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Component
public class TokenRegenerationUtil {

    private final SpotifyConfigProperties spotifyConfigProperties;

    private final WebClient.Builder webClientBuilder;

    private final ObjectMapper objectMapper;

    public TokenRegenerationUtil(SpotifyConfigProperties spotifyConfigProperties,
                                 WebClient.Builder webClientBuilder,
                                 ObjectMapper objectMapper) {
        this.spotifyConfigProperties = spotifyConfigProperties;
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;
    }

    public void regenerateToken() throws IOException {
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.put("grant_type", List.of("refresh_token"));
        queryParams.put("refresh_token", List.of(spotifyConfigProperties.refreshToken()));

        final String response = webClientBuilder
            .baseUrl("https://accounts.spotify.com/api")
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create(ConnectionProvider.newConnection())))
            .defaultHeaders(httpHeaders -> {
                httpHeaders.add("Authorization",
                                "Basic " + spotifyConfigProperties.concatenatedId());
                httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
            })
            .build()
            .post()
            .uri(uriBuilder ->
                     uriBuilder
                         .path("/token")
                         .queryParams(queryParams)
                         .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();
        AuthorizationToken authToken = objectMapper.readValue(response, AuthorizationToken.class);
        spotifyConfigProperties.setAuthToken(authToken.accessToken());
    }

    @JsonPropertyOrder({
        "accessToken",
        "tokenType"
    })
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    private record AuthorizationToken(@JsonProperty("access_token") String accessToken,
                                      @JsonProperty("token_type") String tokenType) {
    }
}
