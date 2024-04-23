package com.edix.tfc.proyecto_tfg.retrofit.interfac;


import com.edix.tfc.proyecto_tfg.retrofit.modelo.Posts;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApi {

    @GET
    Call<List<Posts>> getPosts();

}
