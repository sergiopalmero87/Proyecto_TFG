package com.edix.tfc.proyecto_tfg.retrofit.modelo;

// Esta clase sirve como molde para lo que contendrán las cards
public class ListElement {

    private String textoNoticia;
    private String url; // Cambiado el nombre del atributo a 'url'

    public ListElement(String nombre, String descripcion, String url) {
        this.textoNoticia = nombre + "\n\n" + descripcion;
        this.url = url;
    }

    public String getTextoNoticia() {
        return textoNoticia;
    }

    public void setTextoNoticia(String textoNoticia) {
        this.textoNoticia = textoNoticia;
    }

    public String getUrl() { // Cambiado el nombre del getter a 'getUrl'
        return url;
    }

    public void setUrl(String url) { // Cambiado el nombre del setter a 'setUrl'
        this.url = url;
    }
}
