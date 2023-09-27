package com.spingboot.webflux.webclient.config;

import com.spingboot.webflux.webclient.handler.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> rutas(ProductoHandler handler){
        return RouterFunctions
                .route(RequestPredicates.GET("/api/client"),handler::listar)
                .andRoute(RequestPredicates.GET("/api/client/{id}"),handler::ver)
                .andRoute(RequestPredicates.POST("/api/client"),handler::crear)
                .andRoute(RequestPredicates.PUT("/api/client/{id}"),handler::editar)
                .andRoute(RequestPredicates.DELETE("/api/client/{id}"),handler::eliminar);
    }
}
