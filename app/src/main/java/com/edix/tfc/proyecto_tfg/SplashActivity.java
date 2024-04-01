package com.edix.tfc.proyecto_tfg;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieListener;

public class SplashActivity extends AppCompatActivity implements LottieListener {

    private LottieAnimationView splashAnim;
    private TextView splashText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        // Iniciamos las variables
        iniciarVariables();

        splash();

    }

    private void iniciarVariables() {
        splashAnim = findViewById(R.id.splashAnim);
    }

    private void splash(){
        splashAnim.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Cuando acaba la animación, iniciamos la actividad de login
                startActivity(new Intent(SplashActivity.this, AuthActivity.class));

                // Finalizamos esta actividad para que el usuario no pueda volver atrás con el botón de retroceso
                finish();
            }
        });
    }

    @Override
    public void onResult(Object result) {

    }
}
