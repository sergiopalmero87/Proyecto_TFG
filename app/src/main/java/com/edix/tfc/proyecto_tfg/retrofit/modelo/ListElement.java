package com.edix.tfc.proyecto_tfg.retrofit.modelo;

public class ListElement {

    private String textoNoticia;

    public ListElement() {
    }

    public ListElement(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }

    public String getTextoCategoriaNoticia() {
        return textoNoticia;
    }

    public void setTextoCategoriaNoticia(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }
}
