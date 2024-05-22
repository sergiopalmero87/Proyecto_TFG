package com.edix.tfc.proyecto_tfg;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends AppCompatActivity {

    EditText userNameText, emailText, passwordText;
    Button registButton, botonVerContraseña;
    private ImageButton btnRegistTwitter, btnRegistGoogle;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private LottieAnimationView volver;
    FirebaseUser currentUser;
    int RC_SING_IN = 20;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);


        iniciarVariables();
        registrarUser();
        verContraseña();
        volver();
        btnRegistTwitter();
        btnRegistGoogle();

    }


    private void iniciarVariables(){
        //autenticacion
        mAuth = FirebaseAuth.getInstance();
        //bbdd
        db = FirebaseFirestore.getInstance();

        currentUser = mAuth.getCurrentUser();

        userNameText = findViewById(R.id.textoNombreUser);
        emailText = findViewById(R.id.textoEmail);
        passwordText = findViewById(R.id.textoPassword);
        registButton = findViewById(R.id.loginButtonRegist);
        botonVerContraseña = findViewById(R.id.botonVerContraseña);
        volver = findViewById(R.id.volver);
        btnRegistTwitter = findViewById(R.id.btnRegistTwitter);
        btnRegistGoogle = findViewById(R.id.btnRegistGoogle);
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
                                // Si el task de comparar si existe el email en la collection es exitoso:
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
                                                            intent.putExtra("nombreUsuario",nombreUser);
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

    private void verContraseña() {
        botonVerContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si cuando demos al boton la contraseña esta visible, el ojo se cierra y la contraseña se oculta
                if (passwordText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    botonVerContraseña.setBackgroundResource(R.drawable.cerrar); // Usar setBackgroundResource para establecer un drawable
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    //Si cuando demos al boton la contraseña no se ve, el ojo se abre y la contraseña se muestra
                } else {
                    botonVerContraseña.setBackgroundResource(R.drawable.abrir); // Usar setBackgroundResource para establecer un drawable
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                // Mover el cursor al final del texto
                passwordText.setSelection(passwordText.getText().length());
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

    private void btnRegistTwitter(){
        btnRegistTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Twitter
                OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

                Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
                if (pendingResultTask != null) {
                    // There's something already here! Finish the sign-in for your user.
                    pendingResultTask
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(RegistActivity.this, MainActivity.class));
                                            Toast.makeText(RegistActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                } else {
                    mAuth.startActivityForSignInWithProvider(RegistActivity.this, provider.build())
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(RegistActivity.this, MainActivity.class));
                                            Toast.makeText(RegistActivity.this, "Registro con Twitter correcto", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegistActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                }



            }
        });
    }

    private void btnRegistGoogle() {
        btnRegistGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

                googleSignInClient = GoogleSignIn.getClient(RegistActivity.this,googleSignInOptions);

                googleSingIn();

            }
        });
    }

    private void googleSingIn(){

        //Intent de google
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SING_IN);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SING_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{

                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());

            }catch (ApiException e) {
                throw new RuntimeException(e);
            }



        }
    }

    private void firebaseAuth(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            //Obtenemos el usuario actual
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Guardar usuario en la base de datos con google
                            Map<String, Object> userGoogle = new HashMap<>();
                            userGoogle.put("email", user.getEmail());
                            userGoogle.put("nombreUsuario", user.getDisplayName());

                            db.collection("users")
                                    .add(userGoogle)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("RegistActivity", "Error al agregar usuario a la base de datos", e);
                                        }
                                    });

                            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegistActivity.this, "Registro con Google correcto", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(RegistActivity.this, "NO", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

}
