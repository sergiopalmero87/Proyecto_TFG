package com.edix.tfc.proyecto_tfg.retrofit.modelo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        ListElement element = mData.get(position);
        // Aquí puedes establecer los datos en las vistas de tu ViewHolder
        // por ejemplo: holder.textView.setText(element.getText());
    }

    // Devolver el tamaño de la lista de datos
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // ViewHolder que contendrá las vistas de los elementos de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Aquí declaras las vistas de tu layout de elemento de lista (por ejemplo: TextView, ImageView, etc.)
        public TextView textoNoticia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar aqui las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
        }

        void bindData(final ListElement item){

        }
    }
}
