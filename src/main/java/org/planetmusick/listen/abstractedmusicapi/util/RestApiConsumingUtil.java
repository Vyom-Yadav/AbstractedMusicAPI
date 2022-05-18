package org.planetmusick.listen.abstractedmusicapi.util;

import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Function;

import org.planetmusick.listen.abstractedmusicapi.SpotifyConfigProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@Component
public class RestApiConsumingUtil {

    private final static String WEB_API_BASE_URL = "https://api.spotify.com/v1";
    public static final String ID_NEEDED_EXCEPTION_MESSAGE = "At-least a single ID is needed";
    private final TokenRegenerationUtil tokenRegenerationUtil;

    private final SpotifyConfigProperties spotifyConfigProperties;

    private final WebClient.Builder builder;

    static {
        disableSslVerification();
    }

    private static void disableSslVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    public RestApiConsumingUtil(TokenRegenerationUtil tokenRegenerationUtil,
                                SpotifyConfigProperties spotifyConfigProperties,
                                WebClient.Builder builder) {
        this.tokenRegenerationUtil = tokenRegenerationUtil;
        this.spotifyConfigProperties = spotifyConfigProperties;
        this.builder = builder;
    }

    public String makeRestApiCall(List<String> ids, MultiValueMap<String, String> queries,
                                  String endpoint) throws IOException {
        final String uri = getUri(ids, queries, endpoint);
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

    public String makeRestApiCallWithAdditionalPath(String id,
                                                    MultiValueMap<String, String> queries,
                                                    String endpoint, String additionalPath) throws
                                                                                            IOException {
        final String uriWithAdditionalPath = getUriWithAdditionalPath(id, queries, endpoint,
                                                                      additionalPath);
        if (spotifyConfigProperties.authToken() == null) {
            tokenRegenerationUtil.regenerateToken();
        }
        String response = makeRestApiCall(uriWithAdditionalPath);
        if (response.contains("The access token expired")) {
            tokenRegenerationUtil.regenerateToken();
        }
        response = makeRestApiCall(uriWithAdditionalPath);
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

    private String getUri(List<String> ids, MultiValueMap<String, String> queries,
                          String endpoint) {
        Function<UriBuilder, URI> uriFunction = uriBuilder -> {
            if (ids.size() == 1) {
                uriBuilder.pathSegment(endpoint, ids.get(0));
            }
            else if (!ids.isEmpty()) {
                uriBuilder.path("/").path(endpoint).queryParam("ids", String.join(",", ids));
            }
            else {
                throw new IllegalArgumentException(ID_NEEDED_EXCEPTION_MESSAGE);
            }
            uriBuilder.queryParams(queries);
            return uriBuilder.build();
        };
        // Need a new builder factory to avoid bad URL's
        return uriFunction.apply(new DefaultUriBuilderFactory().builder()).toString();
    }

    private String getUriWithAdditionalPath(String id, MultiValueMap<String, String> queries,
                                            String endpoint, String additionalPath) {
        Function<UriBuilder, URI> uriFunction = uriBuilder -> {
            if (id != null) {
                uriBuilder.pathSegment(endpoint, id, additionalPath);
            }
            else {
                throw new IllegalArgumentException(ID_NEEDED_EXCEPTION_MESSAGE);
            }
            uriBuilder.queryParams(queries);
            return uriBuilder.build();
        };
        // Need a new builder factory to avoid bad URL's
        return uriFunction.apply(new DefaultUriBuilderFactory().builder()).toString();
    }


}
