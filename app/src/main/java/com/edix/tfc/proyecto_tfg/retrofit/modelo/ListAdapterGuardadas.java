package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edix.tfc.proyecto_tfg.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Esta clase comunica la parte back de las cards con la parte front
public class ListAdapterGuardadas extends RecyclerView.Adapter<ListAdapterGuardadas.ViewHolderGuardadas> {
    private List<ListElement> mDataGuardadas;
    private LayoutInflater mInflater;
    private Context mContext;


    public ListAdapterGuardadas(Context context, List<ListElement> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataGuardadas = itemList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolderGuardadas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element_guardadas, parent, false);
        return new ViewHolderGuardadas(view);
    }

    //Sirve para atar, sujetar, vincular etc.. las cosas que vamos implementando al recyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolderGuardadas holder, int position) {
        ListElement element = mDataGuardadas.get(position);

        //En esta variable guardamos el textoNoticia que haya en cada element
        //que es de tipo ListElement(por lo que es el contenido de las cards)
        //Si el largo es de mas de 100 caracteres, hacemos que se muestre ...
        //para que la card no sea tan grande.
        String descripcion = element.getTextoNoticia();
        if (descripcion.length() > 100) {
            //Descripcion es igual a lo que haya en la variable descripcion
            //pero desde el caracter 0 al 100 y el resto se sustituye por ...
            descripcion = descripcion.substring(0, 97) + "...";
        }

        holder.tituloNoticia.setText(element.getTitulo());
        holder.textoNoticia.setText(descripcion);
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
        String fecha = Article.fechaFormateada(element.getFechaPublicacion());
        holder.fechaPublicacion.setText(fecha);
        holder.borrarNoticia(element);
        holder.publicarNoticiaIntent(element);
        holder.urlVer(element);
    }

    //Devolver el tamaño de la lista de datos
    @Override
    public int getItemCount() {
        return mDataGuardadas.size();
    }

    //Esta clase static lo que hace es que si o si ambas clases están relacionadas,
    // lo que es bueno para simplificar el codigo y hacer que sea mas fácil de entender.
    // Tambien simplifica el codigo ya que no necesito crear codigo innecesario (getter and setter)
    // porque la clase static puede acceder a las cosas privadas de la clase en la que esta implementada
    // El ViewHolder contendrá las vistas(las cosas) que irán dentro de las cards
    public class ViewHolderGuardadas extends RecyclerView.ViewHolder {
        public TextView textoNoticia, urlNoticia, namePeriodico, fechaPublicacion, tituloNoticia;
        public ImageView borrarNoticia, publicarTwitter,imagenCard,urlVer;

        public ViewHolderGuardadas(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            namePeriodico = itemView.findViewById(R.id.namePeriodico);
            fechaPublicacion = itemView.findViewById(R.id.fecha);
            tituloNoticia = itemView.findViewById(R.id.tituloNoticia);
            borrarNoticia = itemView.findViewById(R.id.borrarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
            imagenCard = itemView.findViewById(R.id.imagenCard);
            urlVer = itemView.findViewById(R.id.urlVer);
        }

        public void borrarNoticia(final ListElement item) {
            borrarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Obtener la referencia al usuario actual
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    // Verificar si el usuario está autenticado
                    if (currentUser != null) {
                        String userId = currentUser.getUid();

                        // Obtener la referencia a la colección "NoticiasFav" del usuario actual en Firestore
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        CollectionReference noticiasFavRef = db.collection("users").document(userId).collection("NoticiasFav");

                        // Consultar la noticia a borrar por URL
                        Query query = noticiasFavRef.whereEqualTo("url", item.getUrl());

                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Se encontró un documento con la URL especificada, lo eliminamos
                                        document.getReference().delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // Noticia borrada exitosamente
                                                        mDataGuardadas.remove(item);
                                                        notifyDataSetChanged();
                                                        String descripcion = item.getTextoNoticia();
                                                        String url = item.getUrl();
                                                        String name = item.getName();
                                                        String categoria = item.getCategoria();
                                                        String fecha = item.getFechaPublicacion();
                                                        String titulo = item.getTitulo();
                                                        Toast.makeText(itemView.getContext(), "Noticia borrada", Toast.LENGTH_SHORT).show();
                                                        CollectionReference noticiasMasGuardadasRef = db.collection("NoticiasMasGuardadas");
                                                        Query queryMas = noticiasMasGuardadasRef.whereEqualTo("descripcion", descripcion)
                                                                .whereEqualTo("url", url)
                                                                .whereEqualTo("name", name)
                                                                .whereEqualTo("fecha", fecha)
                                                                .whereEqualTo("titulo", titulo);

                                                        queryMas.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()) {
                                                                    QuerySnapshot querySnapshot = task.getResult();
                                                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                                                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                                                            Long contadorActual = document.getLong("contador");
                                                                            if (contadorActual != null) {
                                                                                document.getReference().update("contador", contadorActual - 1)
                                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                            @Override
                                                                                            public void onSuccess(Void aVoid) {
                                                                                                Log.d("Firestore", "Contador actualizado correctamente");
                                                                                            }
                                                                                        })
                                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Log.e("Firestore", "Error al actualizar contador", e);
                                                                                            }
                                                                                        });
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    Log.e("Firestore", "Error al consultar en la base de datos", task.getException());
                                                                }
                                                            }
                                                        });
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        // Error al borrar la noticia.
                                                        // Log para ver el error en logcat más detallado
                                                        Log.e(TAG, "Error al borrar noticia", e);
                                                        Toast.makeText(itemView.getContext(), "Error al borrar noticia", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    // Error al ejecutar la consulta. Log para más detalle
                                    Log.e(TAG, "Error al ejecutar la consulta", task.getException());
                                    Toast.makeText(itemView.getContext(), "Error al ejecutar la consulta", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {

                    }
                }
            });
        }


        public void publicarNoticiaIntent(final ListElement item) {
            publicarTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Mensaje a publicar
                    String tweetText = item.getTextoNoticia();
                    String mediaUrl = item.getUrl();
                    String finalTweetText = tweetText;

                    if (tweetText.length() > 200) {
                        finalTweetText = tweetText.substring(0, 150) + "...";
                    }
                    finalTweetText += "\n " + mediaUrl + "\n\nCompartido a traves de Sporthub";

                    // Crear un intent para compartir en Twitter
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, finalTweetText);
                    intent.setType("text/plain");

                    // Verificar si hay aplicaciones que pueden manejar el intent
                    if (intent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                        // Abrir la actividad para compartir en Twitter
                        itemView.getContext().startActivity(intent);
                    } else {
                        // Si no hay aplicaciones que puedan manejar el intent, mostrar un mensaje de error
                        Toast.makeText(itemView.getContext(), "No se encontró una aplicación para compartir en Twitter", Toast.LENGTH_SHORT).show();
                    }
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