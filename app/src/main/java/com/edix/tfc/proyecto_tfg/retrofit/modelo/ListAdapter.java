package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.edix.tfc.proyecto_tfg.AuthActivity;
import com.edix.tfc.proyecto_tfg.MainActivity;
import com.edix.tfc.proyecto_tfg.R;

import java.util.List;

//Esta clase sirve para relacionar la parte visual de las cards con la parte backend
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;


    // Constructor
    public ListAdapter(Context context, List<ListElement> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = itemList;
        this.context = context;
    }

    // Inflar el layout de la vista de los elementos y crear la ViewHolder.
    //Sirve para mostrar los elementos en la pantalla de la app
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element, parent, false);
        return new ViewHolder(view);
    }

    // Vincular los datos a la vista de cada elemento
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Obtener el elemento de datos en la posición actual
        ListElement element = mData.get(position);

        // Establecer el texto en la TextView dentro de la tarjeta
        holder.textoNoticia.setText(element.getTextoNoticia());

        holder.guardarNoticia(element);

        holder.publicarNoticia(element);
    }


    // Devolver el tamaño de la lista de datos
    @Override
    public int getItemCount() {
        return mData.size();
    }

    //Esta clase static lo que hace es que si o si ambas clases están relacionadas,
    // lo que es bueno para simplificar el codigo y hacer que sea mas fácil de entender.
    // Tambien simplifica el codigo ya que no necesito crear codigo innecesario (getter and setter)
    // porque la clase static puede acceder a las cosas privadas de la clase en la que esta implementada
    // El ViewHolder contendrá las vistas(las cosas) que irán dentro de las cards
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textoNoticia;
        public ImageView guardarNoticia, publicarTwitter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            guardarNoticia = itemView.findViewById(R.id.guardarNoticia);
            publicarTwitter = itemView.findViewById(R.id.imagenCardTwitter);
        }

        // Método guardar la noticia en bbdd
        public void guardarNoticia(final ListElement item) {
            guardarNoticia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(itemView.getContext(), "Guardado", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Método guardar la noticia en bbdd
        public void publicarNoticia(final ListElement item) {
            publicarTwitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(itemView.getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //Sirva para actualizar los elementos de que haya en el ViewHolder
        void bindData(final ListElement item){

        }
    }
}
