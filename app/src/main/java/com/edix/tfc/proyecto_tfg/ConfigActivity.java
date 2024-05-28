package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class ConfigActivity extends AppCompatActivity {

    private LottieAnimationView goHome;
    private Button botonFutbol, botonBaloncesto, botonTenis;
    public static final String CATEGORY_KEY = "categoria";
    public static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        iniciarVariables();
        goHome();
        botonesCategoria();
    }

    // Funcion para iniciar variables
    private void iniciarVariables() {
        goHome = findViewById(R.id.goHome);
        botonFutbol = findViewById(R.id.botonSoccer);
        botonBaloncesto = findViewById(R.id.botonBasket);
        botonTenis = findViewById(R.id.botonTennis);
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
                                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

    public void botonesCategoria() {
        botonFutbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("soccer");
            }
        });

        botonBaloncesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("basket");
            }
        });

        botonTenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("tennis");
            }
        });
    }

    private void setCategoria(String categoria) {
        // Instancia de SharedPreferences con el nombre PREFS_NAME en modo privado
        // para que solo sean accesibles desde nuestra app.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Editor para modificar los valores en SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        // Guarda el valor de la categoría en SharedPreferences con la clave CATEGORY_KEY
        editor.putString(CATEGORY_KEY, categoria);
        // Aplica los cambios de manera asincrónica
        editor.apply();
        // Muestra un Toast para confirmar que la categoría ha sido cambiada
        Toast.makeText(ConfigActivity.this, "Categoría cambiada", Toast.LENGTH_SHORT).show();
        // Crear un Handler para programar el retraso
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Código para iniciar la MainActivity después del retraso
                startActivity(new Intent(ConfigActivity.this, MainActivity.class));
                // Opcionalmente, puedes finalizar la actividad actual si no deseas que el usuario vuelva a ella
                // finish();
            }
        }, 250);
    }
}
