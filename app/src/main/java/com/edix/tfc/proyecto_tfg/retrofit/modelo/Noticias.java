package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import java.util.List;

public class Noticias {

    private String status;
    private List<Article> articles;

    public Noticias() {
    }

    public Noticias(String status, List<Article> articles) {
        this.status = status;
        this.articles = getArticles();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
