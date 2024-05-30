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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Esta clase comunica la parte back de las cards con la parte front
public class ListAdapterMasGuardadas extends RecyclerView.Adapter<ListAdapterMasGuardadas.ViewHolderMasGuardadas> {
    private List<ListElement> mDataMasGuardadas;
    private LayoutInflater mInflater;
    private Context mContext;


    public ListAdapterMasGuardadas(Context context, List<ListElement> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.mDataMasGuardadas = itemList;
        this.mContext = context;
    }


    @NonNull
    @Override
    public ViewHolderMasGuardadas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_element_mas_guardadas, parent, false);
        return new ViewHolderMasGuardadas(view);
    }

    //Sirve para atar, sujetar, vincular etc.. las cosas que vamos implementando al recyclerView
    @Override
    public void onBindViewHolder(@NonNull ViewHolderMasGuardadas holder, int position) {
        ListElement element = mDataMasGuardadas.get(position);

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
        holder.urlVer(element);
    }

    //Devolver el tamaño de la lista de datos
    @Override
    public int getItemCount() {
        return mDataMasGuardadas.size();
    }

    //Esta clase static lo que hace es que si o si ambas clases están relacionadas,
    // lo que es bueno para simplificar el codigo y hacer que sea mas fácil de entender.
    // Tambien simplifica el codigo ya que no necesito crear codigo innecesario (getter and setter)
    // porque la clase static puede acceder a las cosas privadas de la clase en la que esta implementada
    // El ViewHolder contendrá las vistas(las cosas) que irán dentro de las cards
    public class ViewHolderMasGuardadas extends RecyclerView.ViewHolder {
        public TextView textoNoticia, namePeriodico, fechaPublicacion, tituloNoticia;
        public ImageView imagenCard,urlVer;

        public ViewHolderMasGuardadas(@NonNull View itemView) {
            super(itemView);
            //Inicializar las vistas
            textoNoticia = itemView.findViewById(R.id.textoNoticia);
            namePeriodico = itemView.findViewById(R.id.namePeriodico);
            fechaPublicacion = itemView.findViewById(R.id.fecha);
            tituloNoticia = itemView.findViewById(R.id.tituloNoticia);
            imagenCard = itemView.findViewById(R.id.imagenCard);
            urlVer = itemView.findViewById(R.id.urlVer);
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