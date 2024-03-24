package com.edix.tfc.proyecto_tfg;


import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistActivity extends AppCompatActivity {

    EditText userNameText, emailText, passwordText;
    Button registButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        mAuth = FirebaseAuth.getInstance();
        userNameText = findViewById(R.id.textoNombreUser);
        emailText = findViewById(R.id.textoEmail);
        passwordText = findViewById(R.id.textoPassword);
        registButton = findViewById(R.id.loginButtonRegist);

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
                    passwordText.setError("Minimo 6");
                }else {
                    // Crear usuario en FiraBase con email y pass
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                //Al completar la info del nuevo usuario y darle a crear usuario:
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // User registrado correctamente
                                        Toast.makeText(RegistActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(RegistActivity.this, "Algo salió mal. Intentelo de nuevo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                }

        });

    }
}
