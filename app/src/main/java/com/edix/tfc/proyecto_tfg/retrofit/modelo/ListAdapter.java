package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edix.tfc.proyecto_tfg.R;

import java.util.List;

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
        holder.urlNoticia.setText(element.getUrlNoticia());
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
        public TextView textoNoticia,urlNoticia;
        public ImageView guardarNoticia, publicarTwitter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            urlNoticia = itemView.findViewById(R.id.urlNoticia);
            guardarNoticia = itemView.findViewById(R.id.guardarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
        }

        //Metodo para guardar la noticia en bbdd
        public void guardarNoticia(final ListElement item) {
            guardarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Guardado", Toast.LENGTH_SHORT).show();
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
