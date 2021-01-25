package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Scene;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listado_Favoritos extends AppCompatActivity {
    ImageView Imagen;
    String usuario;

    RecyclerView ListaFavoritos;
    ArrayList<String> NombreFavoritos = new ArrayList<String>();

    String sql;

    private ConnectivityManager connectivityManager = null;

    EditText Texto;
    CheckBox bizkaia,alava,gipuzkoa;
    String TextoConsulta="";
    String EscribirConsulta="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__favoritos);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        Imagen= (ImageView)findViewById(R.id.ImagenFavorito);
        Imagen.setImageResource(R.drawable.favoritos);

        ListaFavoritos= (RecyclerView)findViewById(R.id.recycler);

        ListaFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ListaFavoritos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Texto= (EditText)findViewById(R.id.textZona);
        Texto.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EscribirConsulta="";
                if(Texto.getText().toString().length()>0){
                    EscribirConsulta=" nombre LIKE '"+Texto.getText().toString().trim()+"%'";
                }
                Check();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia);
        alava= (CheckBox)findViewById(R.id.checkAlaba);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa);
        sql = "SELECT nombre FROM municipios WHERE nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
        Buscar();
    }

    public void relleno(){
        AdapterLista adapter = new AdapterLista(NombreFavoritos, usuario);
        ListaFavoritos.setAdapter(adapter);
    }

    public void Escribir(){
        String Inicio="";
        if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
            Inicio= "SELECT nombre FROM municipios WHERE nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
        }else if(TextoConsulta.length()==0){
            Inicio= "SELECT nombre FROM municipios WHERE" +EscribirConsulta + " AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
        } else{
            Inicio="SELECT nombre FROM municipios" +TextoConsulta;
        }
        sql=Inicio;
        Buscar();
    }
    public void Check(){
        TextoConsulta="";

        if (bizkaia.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE idProvincia=48 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }else{
                TextoConsulta+=" or idProvincia=48 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (alava.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE idProvincia=1 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }else{
                TextoConsulta+=" or idProvincia=1 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (gipuzkoa.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE idProvincia=20 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }else{
                TextoConsulta+=" or idProvincia=20 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        Escribir();
    }
    public void AplicarFiltro(View v) {
        Check();
    }
    //------------------------------------------------------------------------

    public void Buscar(){
        try {
            if (isConnected()) {
                NombreFavoritos.clear();
                NombreFavoritos = conectar(sql);
                if (NombreFavoritos.size()>0) {
                    relleno(); // Mostramos en el textView el nombre.
                } else {
                    NombreFavoritos.add("");
                    relleno();
                    // Si la respuesta es null, una excepción ha ocurrido.
                   /* Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION",
                            Toast.LENGTH_SHORT).show();*/
                    Toast.makeText(getApplicationContext(), "Resultado Vacio",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            // This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }
    private ArrayList conectar(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "nombre";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) &&
                    (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error_comunicación",
                    Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
    public void Siguiente(View poView){
        Intent oIntent = new Intent(this, Listado_Espacios_Naturales.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Top_Ranking.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
}