package com.edix.tfc.proyecto_tfg;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class NoticiasGuardadasActivity extends AppCompatActivity {

    private LottieAnimationView goHome, moreSaved;
    private TextView textoNoticia, urlNoticia, namePeriodico;
    //Contenedor que aloja las cards
    private RecyclerView recyclerViewGuardadas;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapterGuardadas listAdapterGuardadas;
    //La lista de las cards a mostrar
    private List<ListElement> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticiasguardadas);

        iniciarVariables();
        goHome();
        recyclerViewLayoutManager();
        mostrarNoticiasGuardadas();
        goMasGuardadas();
    }

    // Funcion para iniciar variables
    private void iniciarVariables() {
        goHome = findViewById(R.id.goHome);
        moreSaved = findViewById(R.id.moresave);
        recyclerViewGuardadas = findViewById(R.id.recyclerViewGuardadas);

    }

    private void recyclerViewLayoutManager() {
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerViewGuardadas.setLayoutManager(new LinearLayoutManager(this));
    }

    // Funcion para cuando se pulse el icono de home
    private void goHome() {
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


    private void goMasGuardadas() {
        // Establecer el OnClickListener para el botón de home
        moreSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                moreSaved.playAnimation();

                // Cerrar sesión después de un breve retraso
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(new Intent(NoticiasGuardadasActivity.this, NoticiasMasGuardadasActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }




    private void mostrarNoticiasGuardadas() {
        // Obtenemos la referencia al usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Verificamos si el usuario está autenticado
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Obtenemos referencia a la colección de noticias guardadas del usuario actual en Firebase Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(userId).collection("NoticiasFav")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<ListElement> noticiasGuardadas = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    String descripcion = document.getString("descripcion");
                                    String url = document.getString("url");
                                    String name = document.getString("name");
                                    String categoria = document.getString("categoria");
                                    String fecha = document.getString("fecha");
                                    String titulo = document.getString("titulo");
                                    ListElement listElement = new ListElement(name, descripcion, url,categoria, fecha,titulo);
                                    noticiasGuardadas.add(listElement);
                                }

                                if (!noticiasGuardadas.isEmpty()) {
                                    // Creamos un adaptador con la lista de noticias guardadas y lo configuramos en el RecyclerView
                                    listAdapterGuardadas = new ListAdapterGuardadas(NoticiasGuardadasActivity.this, noticiasGuardadas);
                                    recyclerViewGuardadas.setAdapter(listAdapterGuardadas);
                                    // Notificamos al adaptador que los datos han cambiado
                                    listAdapterGuardadas.notifyDataSetChanged();
                                } else {
                                    // Mostramos un mensaje indicando que no hay noticias guardadas
                                    Toast.makeText(NoticiasGuardadasActivity.this, "No tienes noticias guardadas", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Manejamos el error en caso de que la consulta falle
                                Log.e("Firestore", "Error al obtener noticias guardadas", task.getException());
                            }
                        }
                    });
        } else {
            // Si el usuario no está autenticado, manejar la situación en consecuencia
            // Por ejemplo, redirigir a la pantalla de inicio de sesión
        }
    }




}


