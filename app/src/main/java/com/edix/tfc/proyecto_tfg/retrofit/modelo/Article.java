package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Article {

    private Source source;
    private String description;
    private String url;
    private String publishedAt;

    public Article() {
    }

    public Article(Source source, String description, String url, String publishedAt) {
        this.source = source;
        this.description = description;
        this.url = url;
        this.publishedAt = publishedAt;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public static String fechaFormateada(String fecha) {
        String fechaRetrofit = "yyyy-MM-dd'T'HH:mm:ss'Z'"; //Asi es como nos llega de retrofit
        String fechaCard = "dd-MM-yyyy"; //Asi es como queremos que se quede

        // Creamos un objeto SimpleDateFormat para la fecha en el formato que nos llega de Retrofit.
        SimpleDateFormat inputFormat = new SimpleDateFormat(fechaRetrofit, Locale.getDefault());
        // Creamos otro objeto SimpleDateFormat para el formato en el que queremos que se quede la fecha.
        SimpleDateFormat outputFormat = new SimpleDateFormat(fechaCard, Locale.getDefault());

        // Objeto Date que actuará como intermediario entre los dos formatos de cadena
        Date date = null;
        String fechaFormateada = null;

        try {
            //Con el objeto date, parseamos la fecha que nos llega de retrofit
            // y despues almacenamos en la fechaFormateada ese objeto date
            date = inputFormat.parse(fecha);
            fechaFormateada = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return fechaFormateada;
    }


}
