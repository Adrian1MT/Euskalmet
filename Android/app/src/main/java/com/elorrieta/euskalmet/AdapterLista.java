package com.elorrieta.euskalmet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class AdapterLista extends RecyclerView.Adapter<AdapterLista.ViewHolderDatos>{

    ArrayList<String> listaDatos;
    String Usuario;

    public AdapterLista(ArrayList<String> listaDatos, String usuario) {
        this.listaDatos = listaDatos;
        this.Usuario = usuario;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista,null,false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaDatos.get(position));
        holder.setOnClickListeners();
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dato;
        ImageView favorito;
        Context context;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            String consulta = "SELECT";
            dato = (TextView) itemView.findViewById(R.id.idDato);
            favorito = (ImageView) itemView.findViewById(R.id.favorito);

            favorito.setImageResource(R.drawable.estrella_off);
            context = itemView.getContext();
        }

        public void asignarDatos(String datos) {
            dato.setText(datos);
        }

        public void setOnClickListeners() {
            favorito.setOnClickListener(this);
            dato.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.favorito:
                    favorito.setImageResource(R.drawable.estrella_on);
                    break;
                case R.id.idDato:
                    break;
            }
        }
    }

    private ArrayList conectar(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "password";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
}