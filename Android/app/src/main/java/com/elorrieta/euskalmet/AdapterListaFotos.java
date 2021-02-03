package com.elorrieta.euskalmet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterListaFotos  extends RecyclerView.Adapter<AdapterListaFotos.ViewHolderDatos>{
    ArrayList<String> FotosString;

    private View.OnClickListener listener;

    public AdapterListaFotos(ArrayList<String> ListaImagenes) {
        this.FotosString = ListaImagenes;
    }
    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listafoto,null,false);
        view.setOnClickListener(this.listener);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(FotosString.get(position));
    }

    @Override
    public int getItemCount() {
        return FotosString.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public void onClick(View view) {
        if(listener != null){
            listener.onClick(view);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView FOTOS;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            FOTOS = (ImageView) itemView.findViewById(R.id.fotos);
        }

        public void asignarDatos(String datos) {
            byte[] decodedstring = android.util.Base64.decode(datos,android.util.Base64.DEFAULT);
            Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
            FOTOS.setImageBitmap(decodeByte);
        }
    }
}
