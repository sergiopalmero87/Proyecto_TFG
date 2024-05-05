package com.edix.tfc.proyecto_tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.interfac.RetrofitApi;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapter;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.Noticias;
import com.google.firebase.auth.FirebaseAuth;
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
    TextView nombreUsuario;

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

    private void iniciarVariables(){
        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);
        nombreUsuario = findViewById(R.id.textoNombreUser);
        recyclerView = findViewById(R.id.recyclerViewMain);
        noticiasGuardadasButton = findViewById(R.id.verNoticiasGuardadas);

    }



    private void recyclerViewLayoutManager(){
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }


    private void logOut () {
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

    private void config () {
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

    private void verNoticiasGuardadas () {
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
                    Toast.makeText(MainActivity.this, "Error al obtener noticias", Toast.LENGTH_SHORT).show();
                    return;
                }

                itemList = new ArrayList<>();

                List<Noticias> noticias = response.body();
                for (Noticias noticia : noticias) {
                    String content = "";
                    content += "userId" + noticia.getUserId() + "\n";
                    content += "id" + noticia.getId() + "\n";
                    content += "title" + noticia.getTitle() + "\n";
                    content += "body" + noticia.getBody() + "\n";

                    // Agregar el contenido de la noticia a itemList
                    itemList.add(new ListElement(content));
                    listAdapter = new ListAdapter(MainActivity.this, itemList);
                    recyclerView.setAdapter(listAdapter);
                }

                // Notificar al adaptador que los datos han cambiado
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Noticias>> call, Throwable throwable) {
                Toast.makeText(MainActivity.this, "Error en la solicitud de Retrofit", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
