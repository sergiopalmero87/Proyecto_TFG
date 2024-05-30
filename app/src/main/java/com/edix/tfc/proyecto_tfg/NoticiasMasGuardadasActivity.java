package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class NoticiasMasGuardadasActivity extends AppCompatActivity {

    private LottieAnimationView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticiasmasguardadas);

        iniciarVariables();
        back();

    }

    public void iniciarVariables(){

        back = findViewById(R.id.back);

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
