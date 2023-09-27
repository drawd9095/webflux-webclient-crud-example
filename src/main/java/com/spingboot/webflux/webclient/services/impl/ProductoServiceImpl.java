package com.spingboot.webflux.webclient.services.impl;

import com.spingboot.webflux.webclient.models.Producto;
import com.spingboot.webflux.webclient.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final WebClient client;

    @Override
    public Flux<Producto> findAll() {
        return client.get()
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Producto.class);
    }

    @Override
    public Mono<Producto> findId(String id) {
        return client.get()
                .uri("/{id}", Collections.singletonMap("id",id))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Producto.class);
    }

    @Override
    public Mono<Producto> save(Producto producto) {
        return client.post()
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(fromValue(producto))
                .retrieve()
                .bodyToMono(Producto.class);
    }

    @Override
    public Mono<Producto> update(Producto producto, String id) {
        return client.put()
                .uri("/{id}",Collections.singletonMap("id",id))
                .accept(APPLICATION_JSON)
                .contentType(APPLICATION_JSON)
                .body(fromValue(producto))
                .retrieve()
                .bodyToMono(Producto.class);
    }

    @Override
    public Mono<Void> delete(String id) {
        return client.delete()
                .uri("/{id}",Collections.singletonMap("id",id))
                .accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }
}
