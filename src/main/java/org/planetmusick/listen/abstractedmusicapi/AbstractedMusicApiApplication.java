package org.planetmusick.listen.abstractedmusicapi;

import org.planetmusick.listen.abstractedmusicapi.util.JsonConvertorUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableConfigurationProperties(value = SpotifyConfigProperties.class)
public class AbstractedMusicApiApplication {

    @Bean
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public JsonConvertorUtil getJsonConvertorUtil(
        @Qualifier("getObjectMapper") ObjectMapper objectMapper) {
        return new JsonConvertorUtil(objectMapper);
    }

    public static void main(String[] args) {
        SpringApplication.run(AbstractedMusicApiApplication.class, args);
    }

}
