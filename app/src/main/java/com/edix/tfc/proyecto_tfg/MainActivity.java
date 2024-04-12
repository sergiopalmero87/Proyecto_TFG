package com.edix.tfc.proyecto_tfg;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.Toast;


import com.airbnb.lottie.LottieAnimationView;

import com.google.firebase.auth.FirebaseAuth;





public class MainActivity extends AppCompatActivity {

    private LottieAnimationView logOutButton, configButton;

    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recibirNombreUserRegist();
        iniciarVariables();
        logOut();
        config();


    }

    private void iniciarVariables(){
        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);

    }


    private void recibirNombreUserRegist(){
        Intent intent = getIntent();
        if(intent.hasExtra("nombreUsuario")){
            String nombreUserRegist = intent.getStringExtra("nombreUsuario");
            Toast.makeText(this,"Hola " + nombreUserRegist,Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Bienvenid@",Toast.LENGTH_SHORT).show();
        }
    }

    // Funcion para cuando se pulse el icono de log out
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


    // Funcion para cuando se pulse el icono de configuracion
    private void config () {
        // Establecer el OnClickListener para el botón de config
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



}
