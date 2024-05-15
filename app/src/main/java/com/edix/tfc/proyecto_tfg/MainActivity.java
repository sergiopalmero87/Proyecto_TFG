package com.edix.tfc.proyecto_tfg;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.interfac.RetrofitApi;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapter;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Noticias;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Source;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView logOutButton, configButton, noticiasGuardadasButton;
    private FirebaseAuth mAuth;
    private String userId;
    FirebaseUser user;
    private TextView nombreUsuarioMain;

    //Contenedor que aloja las cards
    private RecyclerView recyclerView;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapter listAdapter;
    //La lista de las cards a mostrar
    private List<ListElement> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarVariables();
        recyclerViewLayoutManager();

        logOut();
        config();
        verNoticiasGuardadas();
        respuestaRetrofit();
    }

    private void iniciarVariables() {
        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);
        recyclerView = findViewById(R.id.recyclerViewMain);
        noticiasGuardadasButton = findViewById(R.id.verNoticiasGuardadas);
        user = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void recyclerViewLayoutManager() {
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void logOut() {
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutButton.playAnimation();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                mAuth.signOut();
                                startActivity(new Intent(MainActivity.this, AuthActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

    private void config() {
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configButton.playAnimation();
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(new Intent(MainActivity.this, ConfigActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

    private void verNoticiasGuardadas() {
        noticiasGuardadasButton.setOnClickListener(v -> {
            noticiasGuardadasButton.playAnimation();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            startActivity(new Intent(MainActivity.this, NoticiasGuardadasActivity.class));
                            finish();
                        }
                    },
                    1000 // Retraso de 1 segundo para dar tiempo a la animación
            );
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
                    Toast.makeText(MainActivity.this, "Error al obtener noticias", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Inicializar itemList para almacenar los elementos de la lista de noticias.
                //Es una lista de tipo ListElemet que es molde de lo que contienen las cards.
                itemList = new ArrayList<>();

                // Obtener la respuesta de Noticias
                Noticias noticiasResponse = response.body();
                if (noticiasResponse != null && noticiasResponse.getStatus().equals("ok")) {
                    // Obtener la lista de fuentes
                    List<Source> sources = noticiasResponse.getSources();

                    // Procesar los datos que me llegan de la API
                    for (Source source : sources) {
                        // Crear un ListElement con los datos de la fuente
                        ListElement listElement = new ListElement(source.getName(),source.getDescription(), source.getUrl());
                        // Agregar el ListElement a itemList
                        itemList.add(listElement);
                    }

                    // Crear un adaptador con la lista de elementos y configurarlo en el RecyclerView
                    listAdapter = new ListAdapter(MainActivity.this, itemList);
                    recyclerView.setAdapter(listAdapter);

                    // Notificar al adaptador que los datos han cambiado
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Noticias> call, Throwable throwable) {
                // Mostrar el mensaje de error completo en el Logcat
                Log.e("Error Retrofit", "Error en la solicitud de Retrofit", throwable);
                // Mostrar un mensaje de error en el Toast con un mensaje genérico
                Toast.makeText(MainActivity.this, "Error en la solicitud de Retrofit. Revisar el Logcat para más detalles.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}