package com.edix.tfc.proyecto_tfg.retrofit.modelo;

// Esta clase sirve como molde para lo que contendrán las cards
public class ListElement {

    private String textoNoticia;
    private String url;
    private String name;

    public ListElement(String name, String descripcion, String url) {
        this.name = name;
        this.textoNoticia = descripcion;
        this.url = url;
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
}
