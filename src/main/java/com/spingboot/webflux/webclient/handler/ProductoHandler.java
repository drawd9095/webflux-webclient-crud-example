package com.spingboot.webflux.webclient.handler;

import com.spingboot.webflux.webclient.models.Producto;
import com.spingboot.webflux.webclient.services.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_GATEWAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;

@Component
@RequiredArgsConstructor
public class ProductoHandler {

    private final ProductoService service;

    public Mono<ServerResponse> listar(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(service.findAll(), Producto.class);
    }

    public Mono<ServerResponse> ver(ServerRequest request){
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(service.findId(request.pathVariable("id")), Producto.class)
                .switchIfEmpty(ServerResponse.notFound().build()
                        .onErrorResume(err -> {
                            WebClientResponseException errorResponse = (WebClientResponseException) err;
                            if(errorResponse.getStatusCode() == NOT_FOUND){
                                Map<String,Object> map = new HashMap<>();
                                map.put("error","No existe producto: ".concat(errorResponse.getMessage()));
                                map.put("fecha", new Date());
                                map.put("status", errorResponse.getStatusCode().value());
                                return ServerResponse
                                        .status(NOT_FOUND)
                                        .body(fromValue(map));
                            }
                            return Mono.error(errorResponse);
                        }));
    }

    public Mono<ServerResponse> crear(ServerRequest request){
        return request.bodyToMono(Producto.class)
                .flatMap(service::save)
                .flatMap( prd -> ServerResponse
                        .created(URI.create("api/client/".concat(prd.getId())))
                        .contentType(APPLICATION_JSON)
                        .body(fromValue(prd)))
                .onErrorResume(err -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) err;
                    if(errorResponse.getStatusCode() == BAD_GATEWAY){
                        return ServerResponse
                                .badRequest().contentType(APPLICATION_JSON)
                                .body(fromValue(errorResponse.getResponseBodyAsString()));
                    }
                    return Mono.error(errorResponse);
                });
    }

    public Mono<ServerResponse> editar(ServerRequest request){

        String id = request.pathVariable("id");

        return request.bodyToMono(Producto.class)
                .flatMap(p->service.update(p,id))
                .flatMap(p -> ServerResponse
                .created(URI.create("api/client/".concat(id)))
                .contentType(APPLICATION_JSON)
                .body(fromValue(p))
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(err -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) err;
                    if(errorResponse.getStatusCode() == NOT_FOUND){
                        return ServerResponse
                                .notFound().build();
                    }
                    return Mono.error(errorResponse);
                }));

    }

    public Mono<ServerResponse> eliminar(ServerRequest request){
        String id = request.pathVariable("id");

        return service
                .delete(id)
                .then(ServerResponse.noContent().build())
                .switchIfEmpty(ServerResponse.notFound().build())
                .onErrorResume(err -> {
                    WebClientResponseException errorResponse = (WebClientResponseException) err;
                    if(errorResponse.getStatusCode() == NOT_FOUND){
                        return ServerResponse
                                .notFound().build();
                    }
                    return Mono.error(errorResponse);
                });

    }

}
