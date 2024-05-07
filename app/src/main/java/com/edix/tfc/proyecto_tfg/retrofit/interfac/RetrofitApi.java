package com.edix.tfc.proyecto_tfg.retrofit.interfac;


import com.edix.tfc.proyecto_tfg.retrofit.modelo.Noticias;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Source;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitApi {

    @GET("top-headlines/sources")
    Call<Noticias> getPosts(@Query("category") String category, @Query("apiKey") String apiKey);

}
