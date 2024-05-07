package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import java.util.List;

public class Noticias {

    private String status;
    private List<Source> sources;

    public Noticias() {
    }

    public Noticias(String status, List<Source> sources) {
        this.status = status;
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }
}
