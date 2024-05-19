package com.edix.tfc.proyecto_tfg.retrofit.modelo;

public class Source {

    private String id;
    private String name;


    public Source() {
    }

    public Source(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

