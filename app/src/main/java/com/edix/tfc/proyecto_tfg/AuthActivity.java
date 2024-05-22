package com.edix.tfc.proyecto_tfg;




import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;

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
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.HashMap;
import java.util.Map;


public class AuthActivity extends AppCompatActivity {

    Button loginButton;
    Button botonVerContraseña;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    EditText emailText, passText;
    TextView registrarText;
    private FirebaseFirestore db;
    private ImageButton btnRegistTwitter, btnRegistGoogle;
    FirebaseUser currentUser;
    int RC_SING_IN = 20;


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

        currentUser = mAuth.getCurrentUser();

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
                                            // Obtenemos el usuario actual
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            // Verificar si el usuario ya está registrado
                                            db.collection("users")
                                                    .whereEqualTo("email", user.getEmail())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                // Usuario ya está registrado
                                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                                Toast.makeText(AuthActivity.this, "Login con Twitter correcto", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Usuario no está registrado, guardarlo en la base de datos
                                                                Map<String, Object> userTwitter = new HashMap<>();
                                                                userTwitter.put("email", user.getEmail());
                                                                userTwitter.put("nombreUsuario", user.getDisplayName());

                                                                db.collection("users")
                                                                        .add(userTwitter)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                                                Toast.makeText(AuthActivity.this, "Registro con Twitter correcto", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e("AuthActivity", "Error al agregar usuario a la base de datos", e);
                                                                                Toast.makeText(AuthActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
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
                                            // Obtenemos el usuario actual
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            // Verificar si el usuario ya está registrado
                                            db.collection("users")
                                                    .whereEqualTo("email", user.getEmail())
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                                // Usuario ya está registrado
                                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                                Toast.makeText(AuthActivity.this, "Login con Twitter correcto", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                // Usuario no está registrado, guardarlo en la base de datos
                                                                Map<String, Object> userTwitter = new HashMap<>();
                                                                userTwitter.put("email", user.getEmail());
                                                                userTwitter.put("nombreUsuario", user.getDisplayName());

                                                                db.collection("users")
                                                                        .add(userTwitter)
                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                            @Override
                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                                                Toast.makeText(AuthActivity.this, "Registro con Twitter correcto", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Log.e("AuthActivity", "Error al agregar usuario a la base de datos", e);
                                                                                Toast.makeText(AuthActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    });
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
                googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

                googleSignInClient = GoogleSignIn.getClient(AuthActivity.this, googleSignInOptions);

                googleSingIn();
            }
        });
    }

    private void googleSingIn() {
        // Intent de google
        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SING_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SING_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            } catch (ApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void firebaseAuth(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Obtenemos el usuario actual
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Verificar si el usuario ya está registrado
                            db.collection("users")
                                    .whereEqualTo("email", user.getEmail())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                                // Usuario ya está registrado
                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                Toast.makeText(AuthActivity.this, "Login con Google correcto", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Usuario no está registrado, guardarlo en la base de datos
                                                Map<String, Object> userGoogle = new HashMap<>();
                                                userGoogle.put("email", user.getEmail());
                                                userGoogle.put("nombreUsuario", user.getDisplayName());

                                                db.collection("users")
                                                        .add(userGoogle)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                                                                Toast.makeText(AuthActivity.this, "Registro con Google correcto", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.e("RegistActivity", "Error al agregar usuario a la base de datos", e);
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(AuthActivity.this, "NO", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
