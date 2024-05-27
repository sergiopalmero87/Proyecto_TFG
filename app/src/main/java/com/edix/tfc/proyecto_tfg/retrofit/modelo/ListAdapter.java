package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edix.tfc.proyecto_tfg.MainActivity;
import com.edix.tfc.proyecto_tfg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;


//Esta clase comunica la parte back de las cards con la parte front
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> listaNoticiasMostrar;
    private LayoutInflater mInflater;
    private static Context mContext;

    private static final String CONSUMER_KEY = "pXlnHHaziTj4im59YbRq0U2RS";
    private static final String CONSUMER_SECRET = "5v6f6Wv7KMw1y6EPvLSZEFAYu3uAf5Lgs2cI6j4pSOgKJrVRfQ";
    private static final String ACCESS_TOKEN = "192594075-IX1pQ6weQatfLmhLtaEPmW2338mboxFvaVGIoGyU";
    private static final String ACCESS_TOKEN_SECRET = "6BI16RJ8NtM5Qlk0bv6PrtpfQUqPWW0v1AsX6RO1PB1li";

    public ListAdapter(Context context, List<ListElement> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.listaNoticiasMostrar = itemList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view);
    }

    //Sirve para atar, sujetar, vincular etc.. las cosas que vamos implementando al recyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Aqui accedemos a cada una de las noticias gracias a su position
        ListElement element = listaNoticiasMostrar.get(position);

        //En esta variable guardamos el textoNoticia que haya en cada element
        //que es de tipo ListElement(por lo que es el contenido de las cards)
        //Si el largo es de mas de 100 caracteres, hacemos que se muestre ...
        //para que la card no sea tan grande.
        String descripcion = element.getTextoNoticia();
        if (descripcion.length() > 150) {
            //Descripcion es igual a lo que haya en la variable descripcion
            //pero desde el caracter 0 al 100 y el resto se sustituye por ...
            descripcion = descripcion.substring(0, 150) + "...";
        }

        holder.textoNoticia.setText(element.getTextoNoticia());
        holder.namePeriodico.setText(element.getName());
        switch (element.getCategoria()) {
            case "soccer":
                holder.imagenCard.setImageResource(R.drawable.futbolbalon);
                break;
            case "basket":
                holder.imagenCard.setImageResource(R.drawable.baloncestobalon);
                break;
            case "tennis":
                holder.imagenCard.setImageResource(R.drawable.tenisbalon);
                break;
            default:
                holder.imagenCard.setImageResource(R.drawable.iconoapp);
                break;
        }
        holder.guardarNoticia(element);
        holder.publicarNoticia(element);
        holder.urlVer(element);
    }

    //Devolver el tamaño de la lista de datos
    @Override
    public int getItemCount() {
        return listaNoticiasMostrar.size();
    }


    //Esta clase static lo que hace es que si o si ambas clases están relacionadas,
    // lo que es bueno para simplificar el codigo y hacer que sea mas fácil de entender.
    // Tambien simplifica el codigo ya que no necesito crear codigo innecesario (getter and setter)
    // porque la clase static puede acceder a las cosas privadas de la clase en la que esta implementada
    // El ViewHolder contendrá las vistas(las cosas) que irán dentro de las cards
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoNoticia, urlNoticia, namePeriodico;
        public ImageView guardarNoticia, publicarTwitter, imagenCard, urlVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            namePeriodico = itemView.findViewById(R.id.namePeriodico);
            guardarNoticia = itemView.findViewById(R.id.guardarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
            imagenCard = itemView.findViewById(R.id.imagenCard);
            urlVer = itemView.findViewById(R.id.urlVer);
        }


        public void guardarNoticia(final ListElement item) {
            guardarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acceder a los valores de textoNoticia, urlNoticia, name y categoria a través de la instancia de ListElement
                    String descripcion = item.getTextoNoticia();
                    String url = item.getUrl();
                    String name = item.getName();
                    String categoria = item.getCategoria(); // Añadimos la categoría

                    // Obtener la referencia al usuario actual
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    // Verificar si el usuario está autenticado
                    if (currentUser != null) {
                        String userId = currentUser.getUid();

                        // Instanciamos la bbdd
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        // Verificar si la noticia ya existe en la colección "NoticiasFav" del usuario actual
                        CollectionReference noticiasFavRef = db.collection("users").document(userId).collection("NoticiasFav");
                        Query query = noticiasFavRef.whereEqualTo("descripcion", descripcion)
                                .whereEqualTo("url", url)
                                .whereEqualTo("name", name);

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        // La noticia ya existe en la base de datos
                                        Toast.makeText(itemView.getContext(), "La noticia ya está guardada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Guardar la noticia en la colección "NoticiasFav" del usuario actual
                                        Map<String, Object> noticia = new HashMap<>();
                                        noticia.put("name", name);
                                        noticia.put("descripcion", descripcion);
                                        noticia.put("url", url);
                                        noticia.put("categoria", categoria); // Guardamos la categoría

                                        noticiasFavRef.add(noticia)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        // La noticia se añadió correctamente
                                                        Toast.makeText(itemView.getContext(), "Noticia guardada", Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error al guardar la noticia
                                                        Toast.makeText(itemView.getContext(), "Error al guardar noticia", Toast.LENGTH_SHORT).show();
                                                        Log.e("Firestore", "Error al guardar noticia", e);
                                                    }
                                                });
                                    }
                                } else {
                                    // Error al realizar la consulta
                                    Toast.makeText(itemView.getContext(), "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", "Error al consultar en la base de datos", task.getException());
                                }
                            }
                        });
                    } else {
                        // Si el usuario no está autenticado, manejar la situación en consecuencia
                        // Por ejemplo, redirigir a la pantalla de inicio de sesión
                    }
                }
            });
        }


        public void publicarNoticia(final ListElement item) {
            publicarTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mensaje a publicar
                    String tweetText = item.getTextoNoticia();
                    if (tweetText.length() > 280) {
                        tweetText = tweetText.substring(0, 277) + "...";
                    }

                    // Publicar el tweet en un hilo separado para evitar el bloqueo de la UI
                    String finalTweetText = tweetText;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // Configura las credenciales de Twitter
                                ConfigurationBuilder cb = new ConfigurationBuilder();
                                cb.setDebugEnabled(true)
                                        .setOAuthConsumerKey(CONSUMER_KEY)
                                        .setOAuthConsumerSecret(CONSUMER_SECRET)
                                        .setOAuthAccessToken(ACCESS_TOKEN)
                                        .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);

                                TwitterFactory tf = new TwitterFactory(cb.build());
                                Twitter twitter = tf.getInstance();

                                // Publica el tweet
                                Status status = twitter.updateStatus(finalTweetText);

                                // Mostrar un mensaje de éxito en el hilo principal de la UI
                                ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(itemView.getContext(), "Publicado en Twitter", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (TwitterException e) {
                                // Manejar errores de Twitter y mostrar un mensaje en la UI
                                ((Activity) itemView.getContext()).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(itemView.getContext(), "Error al publicar en Twitter: " + e.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            });
        }

        public void urlVer(final ListElement item){
            urlVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlNoticia = item.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNoticia));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        //Sirve para actualizar los elementos que haya en el ViewHolder
        void bindData(final ListElement item){

        }
    }
}