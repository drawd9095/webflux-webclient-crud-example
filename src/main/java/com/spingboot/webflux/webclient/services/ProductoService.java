package com.spingboot.webflux.webclient.services;

import com.spingboot.webflux.webclient.models.Producto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<Producto> findAll();
    Mono<Producto> findId(String id);

    Mono<Producto> save(Producto producto);

    Mono<Producto> update(Producto producto, String id);

    Mono<Void> delete(String id);
}
