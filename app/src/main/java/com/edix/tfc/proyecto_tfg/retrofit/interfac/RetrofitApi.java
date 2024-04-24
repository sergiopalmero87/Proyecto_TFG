package com.edix.tfc.proyecto_tfg.retrofit.interfac;


import com.edix.tfc.proyecto_tfg.retrofit.modelo.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    @GET("sources?language=es&country=us&category=sports&apiKey=e0fb2227e0064938b9b9c7528fea009c")
    Call<List<Posts>> getPosts();

}
