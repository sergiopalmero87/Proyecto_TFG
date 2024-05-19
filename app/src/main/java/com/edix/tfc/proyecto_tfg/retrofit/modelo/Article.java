package com.edix.tfc.proyecto_tfg.retrofit.modelo;

public class Article {

    private Source source;
    private String description;
    private String url;

    public Article() {
    }

    public Article(Source source, String description, String url) {
        this.source = source;
        this.description = description;
        this.url = url;
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
}
