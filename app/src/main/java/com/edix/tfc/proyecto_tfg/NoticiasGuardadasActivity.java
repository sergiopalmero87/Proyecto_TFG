package com.edix.tfc.proyecto_tfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.interfac.RetrofitApi;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapter;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Noticias;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NoticiasGuardadasActivity extends AppCompatActivity {

    private LottieAnimationView goHome;

    //Contenedor que aloja las cards
    private RecyclerView recyclerViewGuardadas;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapterGuardadas listAdapterGuardadas;
    //La lista de las cards a mostrar
    private List<ListElement> itemList;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticiasguardadas);

        iniciarVariables();
        goHome();
        recyclerViewLayoutManager();
        respuestaRetrofit();
    }

    // Funcion para iniciar variables
    private void iniciarVariables(){
        goHome = findViewById(R.id.goHome);
        recyclerViewGuardadas = findViewById(R.id.recyclerViewGuardadas);
    }

    private void recyclerViewLayoutManager(){
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerViewGuardadas.setLayoutManager(new LinearLayoutManager(this));
    }

    private void respuestaRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Call<List<Noticias>> respuesta = retrofitApi.getPosts();

        respuesta.enqueue(new Callback<List<Noticias>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<List<Noticias>> call, Response<List<Noticias>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(NoticiasGuardadasActivity.this, "Error al obtener noticias", Toast.LENGTH_SHORT).show();
                    return;
                }

                itemList = new ArrayList<>();

                List<Noticias> noticias = response.body();
                for (Noticias noticia : noticias) {
                    String content = "";
                    content += "userId" + noticia.getUserId();
                    content += "id" + noticia.getId();
                    content += "title" + noticia.getTitle();
                    content += "body" + noticia.getBody();

                    // Agregar el contenido de la noticia a itemList
                    itemList.add(new ListElement(content));
                    listAdapterGuardadas = new ListAdapterGuardadas(NoticiasGuardadasActivity.this, itemList);
                    recyclerViewGuardadas.setAdapter(listAdapterGuardadas);
                }

                // Notificar al adaptador que los datos han cambiado
                listAdapterGuardadas.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Noticias>> call, Throwable throwable) {
                Toast.makeText(NoticiasGuardadasActivity.this, "Error en la solicitud de Retrofit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void goHome () {
        // Establecer el OnClickListener para el botón de home
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                goHome.playAnimation();

                // Cerrar sesión después de un breve retraso
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(new Intent(NoticiasGuardadasActivity.this, MainActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

}
