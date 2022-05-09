package org.planetmusick.listen.abstractedmusicapi;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
@ConfigurationProperties("spotify")
public final class SpotifyConfigProperties {
    private String authToken;
    private final String apiUrl;
    private final String refreshToken;
    private final String concatenatedId;

    @ConstructorBinding
    public SpotifyConfigProperties(String apiUrl, String refreshToken,
                                   String concatenatedId) {
        this.apiUrl = apiUrl;
        this.refreshToken = refreshToken;
        this.concatenatedId = concatenatedId;
    }

    public String authToken() {
        return authToken;
    }

    public String apiUrl() {
        return apiUrl;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public String concatenatedId() {
        return concatenatedId;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (SpotifyConfigProperties) obj;
        return Objects.equals(this.authToken, that.authToken) &&
            Objects.equals(this.apiUrl, that.apiUrl) &&
            Objects.equals(this.refreshToken, that.refreshToken) &&
            Objects.equals(this.concatenatedId, that.concatenatedId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, apiUrl, refreshToken, concatenatedId);
    }

    @Override
    public String toString() {
        return "SpotifyConfigProperties[" +
            "authToken=" + authToken + ", " +
            "apiUrl=" + apiUrl + ", " +
            "refreshToken=" + refreshToken + ", " +
            "concatenatedId=" + concatenatedId + ']';
    }

}
