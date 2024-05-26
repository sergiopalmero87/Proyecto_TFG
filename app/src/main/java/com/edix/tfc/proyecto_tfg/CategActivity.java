package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CategActivity extends AppCompatActivity {

    private Button botonFutbol, botonBaloncesto, botonTenis;
    public static final String CATEGORY_KEY = "categoria";
    public static final String PREFS_NAME = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categ);

        iniciarVariables();
        botonesCategoria();
    }

    private void iniciarVariables(){
        botonFutbol = findViewById(R.id.botonSoccer);
        botonBaloncesto = findViewById(R.id.botonBasket);
        botonTenis = findViewById(R.id.botonTennis);
    }


    public void botonesCategoria() {
        botonFutbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("soccer");
                startActivity(new Intent(CategActivity.this, MainActivity.class));
            }
        });

        botonBaloncesto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("basket");
                startActivity(new Intent(CategActivity.this, MainActivity.class));
            }
        });

        botonTenis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCategoria("tennis");
                startActivity(new Intent(CategActivity.this, MainActivity.class));
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
        Toast.makeText(CategActivity.this, "Categoría elegida: " + categoria, Toast.LENGTH_SHORT).show();
    }
}
