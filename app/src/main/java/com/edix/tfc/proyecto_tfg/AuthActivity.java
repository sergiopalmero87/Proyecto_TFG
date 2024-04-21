package com.edix.tfc.proyecto_tfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    Button loginButton;
    TextView signUpButton;
    Button botonVerContraseña;
    private FirebaseAuth mAuth;
    EditText emailText, passText;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        iniciarVariables();
        verContraseña();
        iniciarSesion();
        botonRegistrar();


    }


    private void iniciarVariables() {
        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        // Inicializamos emailText y passText con el contenido de la caja.
        emailText = findViewById(R.id.textoEmail);
        passText = findViewById(R.id.textoPassword);

        //inicializamos la variable del loginButton y lo ponemos a la escucha
        loginButton = findViewById(R.id.loginButton);

        //Incializamos el boton de ver contraseña.
        botonVerContraseña = findViewById(R.id.botonVerContraseña);
    }

    private void verContraseña() {
        botonVerContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar o detener la animación según el estado de la contraseña
                if (passText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    botonVerContraseña.setBackgroundResource(R.drawable.cerrar); // Usar setBackgroundResource para establecer un drawable
                    passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    botonVerContraseña.setBackgroundResource(R.drawable.abrir); // Usar setBackgroundResource para establecer un drawable
                    passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // Mover el cursor al final del texto
                passText.setSelection(passText.getText().length());
            }
        });
    }



    private void iniciarSesion() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Login en FiraBase
                // Almacenamos en estas variables y pasamos a string las varibales emailText y passText
                String email = emailText.getText().toString();
                String password = passText.getText().toString();

                if (email.isEmpty()) {
                    emailText.setError("El email no puede estar vacío.");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailText.setError("Escriba un email válido");
                } else if (password.length() < 6) {
                    passText.setError("Mínimo 6 caracteres");
                } else {
                    //Comprobamos en la bbdd si el email existe
                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(task -> {
                                //Si lo que obtenemos de la bbdd esta vacio es porque no existe.
                                if (task.getResult().isEmpty()) {
                                    Toast.makeText(AuthActivity.this, "Email o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Accedemos con email y pass a nuestra cuenta
                                    mAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // User autenticado correctamente
                                                        Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                                        startActivity(intent);
                                                    } else if (!task.isSuccessful()) {
                                                        Toast.makeText(AuthActivity.this, "Contraseña incorrecta", Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(AuthActivity.this, "Algo salió mal. Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }


    private void botonRegistrar() {
        //Inicializamos la variable del signUpButton y la ponemos a la escucha
        signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, RegistActivity.class);
                startActivity(intent);

            }
        });
    }

}

