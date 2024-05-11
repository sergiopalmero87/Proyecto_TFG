package com.edix.tfc.proyecto_tfg.retrofit.modelo;

//Esta clase sirve como molde para lo que contendran las cards
public class ListElement {

    private String textoNoticia;
    private String urlNoticia;


    public ListElement(String nombre, String descripcion, String url) {
        this.textoNoticia = nombre + "\n\n" + descripcion;
        this.urlNoticia = url;
    }


    public String getTextoNoticia() {
        return textoNoticia;
    }

    public void setTextoNoticia(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }

    public String getUrlNoticia() {
        return urlNoticia;
    }

    public void setUrlNoticia(String urlNoticia) {
        this.urlNoticia = urlNoticia;
    }
}
