package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapterMasGuardadas;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;

import java.util.List;

public class NoticiasMasGuardadasActivity extends AppCompatActivity {

    private LottieAnimationView back;
    //Contenedor que aloja las cards
    private RecyclerView recyclerViewMasGuardadas;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapterMasGuardadas listAdapterMasGuardadas;
    //La lista de las cards a mostrar
    private List<ListElement> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticiasmasguardadas);

        iniciarVariables();
        back();
        recyclerViewLayoutManager();

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

    private void mostrarNoticiasMasGuardadas(){



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


}
