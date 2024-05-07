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
        holder.textoNoticia.setText(element.getTextoNoticia());
        holder.borrarNoticia(element);
        holder.publicarNoticia(element);
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
    public static class ViewHolderGuardadas extends RecyclerView.ViewHolder {
        public TextView textoNoticia;
        public ImageView borrarNoticia, publicarTwitter;

        public ViewHolderGuardadas(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            borrarNoticia = itemView.findViewById(R.id.borrarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
        }

        //Metodo para guardar la noticia en bbdd
        public void borrarNoticia(final ListElement item) {
            borrarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(itemView.getContext(), "Borrado", Toast.LENGTH_SHORT).show();
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