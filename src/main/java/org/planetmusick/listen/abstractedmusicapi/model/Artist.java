
package org.planetmusick.listen.abstractedmusicapi.model;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "id",
    "name",
    "images"
})
@JsonIgnoreProperties(ignoreUnknown = true)
public record Artist(String id, String name,
                     @JsonInclude(JsonInclude.Include.NON_NULL) Image[] images) {
    @Override
    public String toString() {
        return "Artist{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", images=" + Arrays.toString(images) +
            '}';
    }
}
