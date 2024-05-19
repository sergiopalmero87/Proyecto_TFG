package com.edix.tfc.proyecto_tfg;




import android.content.Intent;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.InputType;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.GetPasswordOption;
import androidx.credentials.GetPublicKeyCredentialOption;
import androidx.credentials.PasswordCredential;
import androidx.credentials.PublicKeyCredential;
import androidx.credentials.exceptions.GetCredentialException;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class AuthActivity extends AppCompatActivity {

    Button loginButton;
    Button botonVerContraseña;
    private FirebaseAuth mAuth;
    EditText emailText, passText;
    TextView registrarText;
    private FirebaseFirestore db;
    private ImageButton btnRegistTwitter, btnRegistGoogle;
    private static final String WEB_CLIENT_ID = "922915579598-g61gmnm51udf52482gmoah1v8d5qi0gs.apps.googleusercontent.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        iniciarVariables();
        verContraseña();
        iniciarSesion();
        registUser();
        btnRegistTwitter();
        btnRegistGoogle();

    }


    private void iniciarVariables() {

        // Inicializamos Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        // Inicializamos emailText y passText con el contenido de la caja.
        emailText = findViewById(R.id.textoEmail);
        passText = findViewById(R.id.textoPassword);

        //inicializamos la variable del loginButton
        loginButton = findViewById(R.id.loginButton);

        //Iniciamos la variable btnRegistTwitter
        btnRegistTwitter = findViewById(R.id.btnRegistTwitter);

        //Iniciamos la variable btnRegistGoogle
        btnRegistGoogle = findViewById(R.id.btnRegistGoogle);

        //Incializamos el boton de ver contraseña.
        botonVerContraseña = findViewById(R.id.botonVerContraseña);

        //Inicializamos text registar cuenta
        registrarText = findViewById(R.id.textRegistrar);

    }

    private void verContraseña() {
        botonVerContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si cuando demos al boton la contraseña esta visible, el ojo se cierra y la contraseña se oculta
                if (passText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)) {
                    botonVerContraseña.setBackgroundResource(R.drawable.cerrar); // Usar setBackgroundResource para establecer un drawable
                    passText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    //Si cuando demos al boton la contraseña no se ve, el ojo se abre y la contraseña se muestra
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
                                    Toast.makeText(AuthActivity.this, "El usuario no está registrado", Toast.LENGTH_SHORT).show();
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
                                                        Toast.makeText(AuthActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
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


    private void registUser() {
        registrarText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthActivity.this, RegistActivity.class);
                startActivity(intent);

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
                                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                            Toast.makeText(AuthActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AuthActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                } else {
                    mAuth.startActivityForSignInWithProvider(AuthActivity.this, provider.build())
                            .addOnSuccessListener(
                                    new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                            Toast.makeText(AuthActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(AuthActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

                CredentialManager credentialManager = CredentialManager.create(AuthActivity.this);

                GetPasswordOption getPasswordOption = new GetPasswordOption();



                GetGoogleIdOption getGoogleIdOption = new GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(true)
                        .setServerClientId(WEB_CLIENT_ID)
                        .setAutoSelectEnabled(true)
                        .setNonce("my_nonce")
                        .build();


                GetCredentialRequest getCredRequest = new GetCredentialRequest.Builder()
                        .addCredentialOption(getGoogleIdOption)
                        .build();

                credentialManager.getCredentialAsync(
                        AuthActivity.this, // Use activity-based context to avoid undefined system UI launching behavior
                        getCredRequest,
                        null, // No need for cancellation signal in this case
                        Executors.newSingleThreadExecutor(), // Use a single thread executor
                        new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {
                            @Override
                            public void onResult(GetCredentialResponse result) {
                                // Handle the successfully returned credential.
                                Credential credential = result.getCredential();
                                if (credential instanceof GoogleIdTokenCredential) {
                                    GoogleIdTokenCredential googleIdTokenCredential = (GoogleIdTokenCredential) credential;
                                    // Use googleIdTokenCredential.getId() or googleIdTokenCredential.getToken() to authenticate
                                    // You can get the Google ID token or the entire token string from here
                                    String idToken = googleIdTokenCredential.getType();
                                    // Now you can use this token for authentication
                                    Log.d("AuthActivity", "Google ID Token: " + idToken);
                                } else {
                                    // Handle unexpected credential type
                                    Log.e("AuthActivity", "Unexpected type of credential");
                                }
                            }

                            @Override
                            public void onError(GetCredentialException e) {
                                // Handle error
                                Log.e("AuthActivity", "Error retrieving credentials", e);
                            }
                        }
                );
            }
        });
    }

}
