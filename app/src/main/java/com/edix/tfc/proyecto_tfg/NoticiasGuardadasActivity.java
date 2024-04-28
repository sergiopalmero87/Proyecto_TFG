package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapter;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;

import java.util.ArrayList;
import java.util.List;

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
        mostrarTarjetas();
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

    private void mostrarTarjetas(){
        // lista de elementos de ejemplo
        itemList = new ArrayList<>();
        itemList.add(new ListElement("Este texto es de prueba para las noticias para ver como se adapta a la card"));
        itemList.add(new ListElement("okalsnjdkaksdmñjalkdklañndjmkalsndklasdnm´kañsjdjañklksjdmañjs-kn.dmajslmdbnakjslbdakjslbdajklbsdjaksbdjaksndkjasndkalñnsdalkñsndl"));
        itemList.add(new ListElement("Noticia 3"));
        itemList.add(new ListElement("Noticia 4"));
        itemList.add(new ListElement("Este texto es de prueba para las noticias para ver como se adapta a la card"));
        itemList.add(new ListElement("okalsnjdkaksdmñjalkdklañndjmkalsndklasdnm´kañsjdjañklksjdmañjs-kn.dmajslmdbnakjslbdakjslbdajklbsdjaksbdjaksndkjasndkalñnsdalkñsndl"));
        itemList.add(new ListElement("Noticia 7"));
        itemList.add(new ListElement("Noticia 8"));

        //Metemos en el adapter la lista de elementos a mostrar
        listAdapterGuardadas = new ListAdapterGuardadas(this, itemList);

        //Seteamos el adapter dentro del recyclerView(contenedor)
        recyclerViewGuardadas.setAdapter(listAdapterGuardadas);
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
