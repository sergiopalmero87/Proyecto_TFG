package com.edix.tfc.proyecto_tfg;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends AppCompatActivity {

    EditText userNameText, emailText, passwordText;
    Button registButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LottieAnimationView volver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);


        iniciarVariables();
        registrarUser();
        volver();

    }


    private void iniciarVariables(){
        //autenticacion
        mAuth = FirebaseAuth.getInstance();
        //bbdd
        db = FirebaseFirestore.getInstance();

        userNameText = findViewById(R.id.textoNombreUser);
        emailText = findViewById(R.id.textoEmail);
        passwordText = findViewById(R.id.textoPassword);
        registButton = findViewById(R.id.loginButtonRegist);
        volver = findViewById(R.id.volver);
    }

    private void registrarUser(){
        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getText y toString porque accedemos a la caja y luego al contenido.
                String nombreUser = userNameText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();

                if(nombreUser.isEmpty()){
                    userNameText.setError("El nombre de usuario no puede estar vacío");
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailText.setError("Introduce un email correcto");
                } else if(password.length() < 6){
                    passwordText.setError("Mínimo 6 caracteres para la contraseña");
                } else {
                    // Verificar si el correo electrónico ya está en uso
                    db.collection("users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    //Si lo que obtenemos de la bbdd no esta vacio es porque el correo ya esta usado
                                    if (!task.getResult().isEmpty()) {
                                        // El email ya está en uso
                                        Toast.makeText(RegistActivity.this, "El correo electrónico ya está en uso", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // El email no está en uso, registramos user con email y password
                                        mAuth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Guardar usuario en la base de datos con email y nombreUsuario
                                                            Map<String, Object> user = new HashMap<>();
                                                            user.put("email", email);
                                                            user.put("nombreUsuario", nombreUser);

                                                            db.collection("users")
                                                                    .add(user)
                                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                        @Override
                                                                        public void onSuccess(DocumentReference documentReference) {
                                                                            Toast.makeText(RegistActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Log.e("RegistActivity", "Error al agregar usuario a la base de datos", e);
                                                                        }
                                                                    });

                                                            // Ir a la actividad principal y mandamos el nombre de usuario para que la UI sea mas personal.
                                                            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                        } else {
                                                            // Error al crear el usuario
                                                            Toast.makeText(RegistActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                                                            Log.e("RegistActivity", "Error al crear usuario", task.getException());
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    // Error al verificar el correo electrónico
                                    Toast.makeText(RegistActivity.this, "Error al verificar el correo electrónico", Toast.LENGTH_SHORT).show();
                                    Log.e("RegistActivity", "Error al verificar el correo electrónico", task.getException());
                                }
                            });
                }
            }
        });
    }


    private void volver(){
        volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reproducir la animación
                volver.playAnimation();

                // Cerrar sesión después de un breve retraso
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                startActivity(new Intent(RegistActivity.this, AuthActivity.class));
                                finish();
                            }
                        },
                        1000 // Retraso de 1 segundo para dar tiempo a la animación
                );
            }
        });
    }

}
