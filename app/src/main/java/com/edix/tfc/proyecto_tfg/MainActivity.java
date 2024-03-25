package com.edix.tfc.proyecto_tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView logOutButton, configButton;
    private TextView nombreUserMain;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);
        nombreUserMain = findViewById(R.id.nombreUserMain);

        logOut();
        config();
        nombreUserMain();

    }


    private void logOut () {
        // Listener para el botón de logOut
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                logOutButton.playAnimation();

                // Cerrar sesión después de un breve retraso
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

    //Listener para el boton de config
    private void config () {
        // Establecer el OnClickListener para el botón de salida
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                configButton.playAnimation();

                // Cerrar sesión después de un breve retraso
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

    private void nombreUserMain(){
        // Obtener el nombre de usuario del Intent RegistActivity
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        // Verificar si el nombre de usuario es nulo y lo mostramos
        if (nombreUsuario != null) {
            // Establecer el nombre de usuario en el TextView
            nombreUserMain.setText("Hola " + nombreUsuario);
        } else {
            // Si el nombre de usuario es nulo, mostrar un mensaje predeterminado
            nombreUserMain.setText("Hola user");
        }
    }
}
