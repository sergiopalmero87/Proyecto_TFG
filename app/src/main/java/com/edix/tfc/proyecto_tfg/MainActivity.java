package com.edix.tfc.proyecto_tfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListAdapter;
import com.edix.tfc.proyecto_tfg.retrofit.modelo.ListElement;
import com.google.firebase.auth.FirebaseAuth;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView logOutButton, configButton;
    private FirebaseAuth mAuth;
    TextView nombreUsuario;
    private RecyclerView recyclerView;
    private ListAdapter listAdapter;
    private List<ListElement> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ponerNombreUser();
        iniciarVariables();
        logOut();
        config();

        // Inicializa el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // lista de elementos de ejemplo
        itemList = new ArrayList<>();
        itemList.add(new ListElement("Este texto es de prueba para las noticias para ver como se adapta a la card"));
        itemList.add(new ListElement("okalsnjdkaksdmñjalkdklañndjmkalsndklasdnm´kañsjdjañklksjdmañjs-kn.dmajslmdbnakjslbdakjslbdajklbsdjaksbdjaksndkjasndkalñnsdalkñsndl"));
        itemList.add(new ListElement("Noticia 3"));
        itemList.add(new ListElement("Noticia 4"));

        // Inicializa el adaptador con la lista de elementos
        listAdapter = new ListAdapter(this, itemList);
        recyclerView.setAdapter(listAdapter);
    }

    private void iniciarVariables(){
        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);
        nombreUsuario = findViewById(R.id.textoNombreUser);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void ponerNombreUser(){
        Intent intent = getIntent();
        if(intent.hasExtra("nombreUsuario")){
            String nombreUserRegist = intent.getStringExtra("nombreUsuario");
            Toast.makeText(this,"Hola " + nombreUserRegist,Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Hola 👋🏼",Toast.LENGTH_SHORT).show();
        }
    }

    private void logOut () {
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutButton.playAnimation();
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

    private void config () {
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configButton.playAnimation();
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
