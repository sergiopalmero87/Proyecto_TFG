package com.edix.tfc.proyecto_tfg.retrofit.modelo;

// Esta clase sirve como molde para lo que contendrán las cards
public class ListElement {

    private String textoNoticia;
    private String url;
    private String name;
    private String categoria;

    public ListElement(String name, String descripcion, String url,String categoria) {
        this.name = name;
        this.textoNoticia = descripcion;
        this.url = url;
        this.categoria = categoria;
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
}
