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

    //Contenedor que aloja las cards
    private RecyclerView recyclerView;
    //Comunica la parte back con la front de las cards. Hace
    //Hace que se muestren las cosas en las cards
    private ListAdapter listAdapter;
    //La lista de las cards a mostrar
    private List<ListElement> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ponerNombreUser();
        iniciarVariables();
        recyclerViewLayoutManager();
        mostrarTarjetas();
        logOut();
        config();



    }

    private void iniciarVariables(){
        mAuth = FirebaseAuth.getInstance();
        logOutButton = findViewById(R.id.logOut);
        configButton = findViewById(R.id.config);
        nombreUsuario = findViewById(R.id.textoNombreUser);
        recyclerView = findViewById(R.id.recyclerView);

    }



    private void recyclerViewLayoutManager(){
        // Establecemos como se mostraran las cosas en el recyclerview
        // (que es el contenedor donde se alojaran las cards en la pantalla.
        // Por defecto vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void mostrarTarjetas(){
        // lista de elementos de ejemplo
        itemList = new ArrayList<>();
        itemList.add(new ListElement("Este texto es de prueba para las noticias para ver como se adapta a la card"));
        itemList.add(new ListElement("okalsnjdkaksdmñjalkdklañndjmkalsndklasdnm´kañsjdjañklksjdmañjs-kn.dmajslmdbnakjslbdakjslbdajklbsdjaksbdjaksndkjasndkalñnsdalkñsndl"));
        itemList.add(new ListElement("Noticia 3"));
        itemList.add(new ListElement("Noticia 4"));
        itemList.add(new ListElement("Este texto es de prueba para las noticias para ver como se adapta a la card"));
        itemList.add(new ListElement("okalsnjdkaksdmñjalkdklañndjmkalsndklasdnm´kañsjdjañklksjdmañjs-kn.dmajslmdbnakjslbdakjslbdajklbsdjaksbdjaksndkjasndkalñnsdalkñsndl"));
        itemList.add(new ListElement("Noticia 7"));
        itemList.add(new ListElement("Noticia 8"));

        //Metemos en el adapter la lista de elementos a mostrar
        listAdapter = new ListAdapter(this, itemList);

        //Seteamos el adapter dentro del recyclerView(contenedor)
        recyclerView.setAdapter(listAdapter);
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
