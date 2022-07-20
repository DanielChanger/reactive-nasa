package com.changer.reactivenasa;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReactivePictureService {

    private final NasaApiProperties nasaApiProperties;
    private final ReactorClientHttpConnector redirectingConnector =
            new ReactorClientHttpConnector(HttpClient.create().followRedirect(true));
    public Mono<byte[]> getLargestPictureBySol(int sol) {

        return WebClient.create(nasaApiProperties.createPhotosUri(sol))
                        .get()
                        .exchangeToMono(response -> response.bodyToMono(JsonNode.class))
                        .map(json -> json.findValuesAsText("img_src"))
                        .flatMapIterable(Function.identity())
                        .flatMap(this::getPictureLength)
                        .reduce((p1, p2) -> p1.getValue() > p2.getValue() ? p1 : p2)
                        .map(SimpleEntry::getKey)
                        .flatMap(this::getPictureBytes);
    }

    private Mono<SimpleEntry<String, Long>> getPictureLength(String url) {
        return WebClient.builder()
                        .clientConnector(redirectingConnector)
                        .baseUrl(url)
                        .build()
                        .head()
                        .retrieve()
                        .toBodilessEntity()
                        .flatMap(response -> Mono.justOrEmpty(response.getHeaders().getContentLength()))
                        .map(a -> new AbstractMap.SimpleEntry<>(url, a));

    }

    private Mono<byte[]> getPictureBytes(String uri) {
        int byteSize = 10_000_000;
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                                                     .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(byteSize))
                                                     .build();
        return WebClient.builder()
                        .clientConnector(redirectingConnector)
                        .baseUrl(uri)
                        .exchangeStrategies(strategies)
                        .build()
                        .get()
                        .exchangeToMono(b -> b.bodyToMono(byte[].class));
    }
}
