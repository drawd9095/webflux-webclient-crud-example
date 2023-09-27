package com.spingboot.webflux.webclient.models;

import lombok.Data;

import java.util.Date;

@Data
public class Producto {
    private String id;
    private String nombre;
    private Double precio;
    private Date createAt;
}
