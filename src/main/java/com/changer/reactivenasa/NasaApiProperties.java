package com.changer.reactivenasa;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Data
@Component
public class NasaApiProperties {
    @Value("${nasa.api.base.url}")
    private String baseUrl;
    @Value("${nasa.api.key}")
    private String apiKey;

    public String createPhotosUri(int sol) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                                          .queryParam("sol", sol)
                                          .queryParam("api_key", apiKey)
                                          .build()
                                          .toString();
    }
}
