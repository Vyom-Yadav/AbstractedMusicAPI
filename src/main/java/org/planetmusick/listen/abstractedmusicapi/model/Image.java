package org.planetmusick.listen.abstractedmusicapi.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "height",
    "width",
    "url"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public record Image(int height, int width, @JsonInclude(JsonInclude.Include.NON_NULL) String url) {

    @Override
    public String toString() {
        return "Image{" +
            "height=" + height +
            ", width=" + width +
            ", url='" + url + '\'' +
            '}';
    }
}
