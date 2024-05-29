package com.edix.tfc.proyecto_tfg.retrofit.modelo;

// Esta clase sirve como molde para lo que contendrán las cards
public class ListElement {

    private String textoNoticia;
    private String url;
    private String name;
    private String categoria;
    private String fechaPublicacion;
    private String titulo;
    private Long contador;

    public ListElement(String name, String descripcion, String url,String categoria, String fechaPublicacion, String titulo, Long contador) {
        this.name = name;
        this.textoNoticia = descripcion;
        this.url = url;
        this.categoria = categoria;
        this.fechaPublicacion = fechaPublicacion;
        this.titulo = titulo;
        this.contador = contador;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTextoNoticia() {
        return textoNoticia;
    }

    public void setTextoNoticia(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getContador() {
        return contador;
    }

    public void setContador(Long contador) {
        this.contador = contador;
    }
}
