package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterMasGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElementMasGuardadas;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NoticiasMasGuardadasActivity extends AppCompatActivity {

    private LottieAnimationView back;
    //Contenedor que aloja las cards
    private RecyclerView recyclerViewMasGuardadas;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapterMasGuardadas listAdapterMasGuardadas;
    //La lista de las cards a mostrar
    private List<ListElementMasGuardadas> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticiasmasguardadas);

        iniciarVariables();
        back();
        recyclerViewLayoutManager();
        mostrarNoticiasMasGuardadas();

    }

    public void iniciarVariables(){
        back = findViewById(R.id.back);
        recyclerViewMasGuardadas = findViewById(R.id.recyclerViewMasGuardadas);

    }

    private void recyclerViewLayoutManager() {
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerViewMasGuardadas.setLayoutManager(new LinearLayoutManager(this));
    }

    // Funcion para cuando se pulse el icono de home
    private void back() {
        // Establecer el OnClickListener para el botón de home
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                back.playAnimation();

                // Cerrar sesión después de un breve retraso
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(new Intent(NoticiasMasGuardadasActivity.this, NoticiasGuardadasActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

    private void mostrarNoticiasMasGuardadas() {
        // Obtenemos referencia a la colección de noticias más guardadas en Firebase
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("NoticiasMasGuardadas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<ListElementMasGuardadas> noticiasMasGuardadas = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                String descripcion = document.getString("descripcion");
                                String url = document.getString("url");
                                String name = document.getString("name");
                                String categoria = document.getString("categoria");
                                String fecha = document.getString("fecha");
                                String titulo = document.getString("titulo");
                                Long contador = document.getLong("contador");

                                if (contador != null && contador > 0) {
                                    ListElementMasGuardadas listElementMasGuardadas = new ListElementMasGuardadas(name, descripcion, url, categoria, fecha, titulo, contador);
                                    noticiasMasGuardadas.add(listElementMasGuardadas);
                                } else {
                                    // Eliminar el documento si el contador es 0
                                    document.getReference().delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d("Firestore", "Documento con contador 0 eliminado");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Firestore", "Error al eliminar documento con contador 0", e);
                                        }
                                    });
                                }
                            }

                            if (!noticiasMasGuardadas.isEmpty()) {
                                // Creamos un adaptador con la lista de noticias guardadas y lo configuramos en el RecyclerView
                                listAdapterMasGuardadas = new ListAdapterMasGuardadas(NoticiasMasGuardadasActivity.this, noticiasMasGuardadas);
                                recyclerViewMasGuardadas.setAdapter(listAdapterMasGuardadas);
                                // Notificamos al adaptador que los datos han cambiado
                                listAdapterMasGuardadas.notifyDataSetChanged();
                            } else {
                                // Mostramos un mensaje indicando que no hay noticias guardadas
                                Toast.makeText(NoticiasMasGuardadasActivity.this, "La lista está vacía", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Manejamos el error en caso de que la consulta falle
                            Log.e("Firestore", "Error al obtener noticias guardadas", task.getException());
                        }
                    }
                });
    }



}
