package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import android.content.Context;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Esta clase comunica la parte back de las cards con la parte front
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> listaNoticiasMostrar;
    private LayoutInflater mInflater;
    private Context mContext;


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

        holder.textoNoticia.setText(element.getTextoNoticia());
        holder.urlNoticia.setText(element.getUrl());
        holder.namePeriodico.setText(element.getName());
        holder.guardarNoticia(element);
        holder.publicarNoticia(element);
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
        public ImageView guardarNoticia, publicarTwitter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            urlNoticia = itemView.findViewById(R.id.urlNoticia);
            namePeriodico = itemView.findViewById(R.id.namePeriodico);
            guardarNoticia = itemView.findViewById(R.id.guardarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
        }


        public void guardarNoticia(final ListElement item) {
            guardarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Acceder a los valores de textoNoticia y urlNoticia a través de la instancia de ViewHolder
                    String texto = textoNoticia.getText().toString();
                    String url = urlNoticia.getText().toString();
                    String name = namePeriodico.getText().toString();

                    //Instanciamos la bbdd
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Obtener una referencia a la colección "NoticiasFav" en Firestore
                    // para poder comprobar si la noticia ya existe
                    CollectionReference noticiasRef = db.collection("NoticiasFav");
                    noticiasRef.whereEqualTo("descripcion", texto)
                            .whereEqualTo("url", url)
                            .whereEqualTo("name", name)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        QuerySnapshot querySnapshot = task.getResult();
                                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                            // La noticia ya existe en la base de datos
                                            Toast.makeText(itemView.getContext(), "La noticia ya está guardada", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Guardar la noticia en la base de datos
                                            Map<String, Object> noticia = new HashMap<>();
                                            noticia.put("name", name);
                                            noticia.put("descripcion", texto);
                                            noticia.put("url", url);
                                            db.collection("NoticiasFav").document().set(noticia)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // Se añadió correctamente
                                                            Toast.makeText(itemView.getContext(), "Noticia guardada", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Error al añadir
                                                            Toast.makeText(itemView.getContext(), "Error al guardar noticia", Toast.LENGTH_SHORT).show();
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
                }
            });
        }




        //Metodo para publicar la noticia en redes
        public void publicarNoticia(final ListElement item) {
            publicarTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Sirve para actualizar los elementos que haya en el ViewHolder
        void bindData(final ListElement item){

        }
    }
}
