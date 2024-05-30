package com.edix.tfc.proyecto_tfg.retrofit.modelo;

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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> listaNoticiasMostrar;
    private LayoutInflater mInflater;
    private static Context mContext;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListElement element = listaNoticiasMostrar.get(position);
        String descripcion = element.getTextoNoticia();
        if (descripcion.length() > 100) {
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
        holder.guardarNoticia(element);
        holder.publicarNoticiaIntent(element);
        holder.urlVer(element);
    }

    @Override
    public int getItemCount() {
        return listaNoticiasMostrar.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textoNoticia, urlNoticia, namePeriodico, fechaPublicacion, tituloNoticia;
        public ImageView guardarNoticia, publicarTwitter, imagenCard, urlVer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            namePeriodico = itemView.findViewById(R.id.namePeriodico);
            fechaPublicacion = itemView.findViewById(R.id.fecha);
            tituloNoticia = itemView.findViewById(R.id.tituloNoticia);
            guardarNoticia = itemView.findViewById(R.id.guardarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
            imagenCard = itemView.findViewById(R.id.imagenCard);
            urlVer = itemView.findViewById(R.id.urlVer);
        }

        public void guardarNoticia(final ListElement item) {
            guardarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String descripcion = item.getTextoNoticia();
                    String url = item.getUrl();
                    String name = item.getName();
                    String categoria = item.getCategoria();
                    String fecha = item.getFechaPublicacion();
                    String titulo = item.getTitulo();

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    if (currentUser != null) {
                        String userId = currentUser.getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                        CollectionReference noticiasFavRef = db.collection("users").document(userId).collection("NoticiasFav");
                        Query queryFav = noticiasFavRef.whereEqualTo("descripcion", descripcion)
                                .whereEqualTo("url", url)
                                .whereEqualTo("name", name)
                                .whereEqualTo("fecha", fecha)
                                .whereEqualTo("titulo", titulo);

                        queryFav.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                        Toast.makeText(itemView.getContext(), "La noticia ya está guardada", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Map<String, Object> noticia = new HashMap<>();
                                        noticia.put("name", name);
                                        noticia.put("descripcion", descripcion);
                                        noticia.put("url", url);
                                        noticia.put("categoria", categoria);
                                        noticia.put("fecha", fecha);
                                        noticia.put("titulo", titulo);

                                        noticiasFavRef.add(noticia)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(itemView.getContext(), "Noticia guardada", Toast.LENGTH_SHORT).show();
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
                                                                                document.getReference().update("contador", contadorActual + 1)
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
                                                                    } else {
                                                                        Map<String, Object> noticia = new HashMap<>();
                                                                        noticia.put("name", name);
                                                                        noticia.put("descripcion", descripcion);
                                                                        noticia.put("url", url);
                                                                        noticia.put("categoria", categoria);
                                                                        noticia.put("fecha", fecha);
                                                                        noticia.put("titulo", titulo);
                                                                        noticia.put("contador", 1);

                                                                        noticiasMasGuardadasRef.add(noticia)
                                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                    @Override
                                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                                        Log.d("Firestore", "Noticia guardada en NoticiasMasGuardadas");
                                                                                    }
                                                                                })
                                                                                .addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        Log.e("Firestore", "Error al guardar noticia en NoticiasMasGuardadas", e);
                                                                                    }
                                                                                });
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
                                                        Toast.makeText(itemView.getContext(), "Error al guardar noticia", Toast.LENGTH_SHORT).show();
                                                        Log.e("Firestore", "Error al guardar noticia", e);
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(itemView.getContext(), "Error al consultar la base de datos", Toast.LENGTH_SHORT).show();
                                    Log.e("Firestore", "Error al consultar en la base de datos", task.getException());
                                }
                            }
                        });

                    } else {
                        Toast.makeText(itemView.getContext(), "Debes estar autenticado para guardar noticias", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void publicarNoticiaIntent(final ListElement item) {
            publicarTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tweetText = item.getTextoNoticia();
                    String mediaUrl = item.getUrl();
                    String finalTweetText = tweetText;

                    if (tweetText.length() > 200) {
                        finalTweetText = tweetText.substring(0, 150) + "...";
                    }
                    finalTweetText += "\n " + mediaUrl + "\n\nCompartido a traves de Sporthub";

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT, finalTweetText);
                    intent.setType("text/plain");

                    if (intent.resolveActivity(itemView.getContext().getPackageManager()) != null) {
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "No se encontró una aplicación para compartir en Twitter", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        public void urlVer(final ListElement item) {
            urlVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String urlNoticia = item.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlNoticia));
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        void bindData(final ListElement item) {

        }
    }
}
