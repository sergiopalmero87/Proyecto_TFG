package com.edix.tfc.proyecto_tfg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.interfac.RetrofitApi;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Noticias;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Source;

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

    // Funcion para cuando se pulse el icono de home
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

    private void respuestaRetrofit() {
        // Configuración de Retrofit para la comunicación con la API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Creación de la instancia de la interfaz RetrofitApi para realizar la llamada
        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        // Llamada a la API para obtener la lista de noticias
        Call<Noticias> respuesta = retrofitApi.getPosts("sports", "e0fb2227e0064938b9b9c7528fea009c");

        // Manejo de la respuesta de la llamada asíncrona a la API
        respuesta.enqueue(new Callback<Noticias>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<Noticias> call, Response<Noticias> response) {
                // Verificar si la respuesta es exitosa
                if (!response.isSuccessful()) {
                    // Mostrar un mensaje de error si la respuesta no es exitosa
                    Toast.makeText(NoticiasGuardadasActivity.this, "Error al obtener noticias", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Inicializar itemList para almacenar los elementos de la lista de noticias
                itemList = new ArrayList<>();

                // Obtener la respuesta de Noticias
                Noticias noticiasResponse = response.body();
                if (noticiasResponse != null && noticiasResponse.getStatus().equals("ok")) {
                    // Obtener la lista de fuentes
                    List<Source> sources = noticiasResponse.getSources();

                    /// Procesar los datos que me llegan de la API
                    for (Source source : sources) {
                        // Crear un ListElement con los datos de la fuente
                        ListElement listElement = new ListElement(source.getName(), source.getDescription(), source.getUrl());
                        // Agregar el ListElement a itemList
                        itemList.add(listElement);
                    }

                    // Crear un adaptador con la lista de elementos y configurarlo en el RecyclerView
                    listAdapterGuardadas = new ListAdapterGuardadas(NoticiasGuardadasActivity.this, itemList);
                    recyclerViewGuardadas.setAdapter(listAdapterGuardadas);

                    // Notificar al adaptador que los datos han cambiado
                    listAdapterGuardadas.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Noticias> call, Throwable throwable) {
                // Obtener el mensaje de error del throwable
                String errorMessage = throwable.getMessage();
                // Mostrar el mensaje de error completo en el Logcat
                Log.e("Error Retrofit", "Error en la solicitud de Retrofit", throwable);
                // Mostrar un mensaje de error en el Toast con un mensaje genérico
                Toast.makeText(NoticiasGuardadasActivity.this, "Error en la solicitud de Retrofit. Revisar el Logcat para más detalles.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
