package com.elorrieta.euskalmet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdapterListaMunicipios extends RecyclerView.Adapter<AdapterListaMunicipios.ViewHolderDatos>{
    ArrayList<String> listaDatos;
    ArrayList<String> listaFavoritos;
    String Usuario;
    public AdapterListaMunicipios(ArrayList<String> listaDatos, String usuario) {
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
        String consulta = "SELECT nomMunicipio FROM es_favorito_mun WHERE idUser = '" + Usuario + "'";
        try {
            listaFavoritos = conectar_select(consulta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(listaFavoritos.contains(holder.dato.getText().toString())){
            holder.favorito.setImageResource(R.drawable.estrella_on);
        }else {
            holder.favorito.setImageResource(R.drawable.estrella_off);
        }
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

            dato = (TextView) itemView.findViewById(R.id.idDato);
            favorito = (ImageView) itemView.findViewById(R.id.foto);


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
                case R.id.foto:
                    if(listaFavoritos.contains(dato.getText().toString())){
                        String consulta = "DELETE FROM es_favorito_mun WHERE idUser = '" + Usuario + "' AND nomMunicipio = '" + dato.getText().toString() + "'";
                        try {
                            conectar_insert(consulta);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        favorito.setImageResource(R.drawable.estrella_off);
                        listaFavoritos.remove(listaFavoritos.indexOf(dato.getText().toString()));
                    }else{
                        String consulta = "INSERT INTO es_favorito_mun(idUser, nomMunicipio) VALUES('" + Usuario + "', '" + dato.getText().toString() + "')";
                        try {
                            conectar_insert(consulta);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        favorito.setImageResource(R.drawable.estrella_on);
                        listaFavoritos.add(dato.getText().toString());
                    }
                    break;
                case R.id.idDato:
                    Intent oIntent = new Intent(context, Detalles_Municipios.class);

                    oIntent.putExtra("Municipio",dato.getText().toString());
                    oIntent.putExtra("Usuario",Usuario);
                    context.startActivity(oIntent);
                    break;
            }
        }
    }

    private ArrayList conectar_select(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "nomMunicipio";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }

    private void conectar_insert(String consulta) throws InterruptedException {
        Client_Insercion_Update clientThread = new Client_Insercion_Update(consulta);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
    }

}