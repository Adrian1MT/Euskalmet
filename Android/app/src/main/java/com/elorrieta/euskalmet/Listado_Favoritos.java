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
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listado_Favoritos extends AppCompatActivity {
    ImageView Imagen;
    String usuario;

    String nomColumna = "nombre";

    RecyclerView ListaFavoritos;
    ArrayList<String> NombreFavoritos = new ArrayList<String>();

    String sql;

    private ConnectivityManager connectivityManager = null;

    EditText Texto;
    CheckBox bizkaia,alava,gipuzkoa, espacios, municipios;
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
                    EscribirConsulta=" LIKE '"+Texto.getText().toString().trim()+"%'";
                }
                Check();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia);
        alava= (CheckBox)findViewById(R.id.checkAlaba);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa);
        espacios= (CheckBox)findViewById(R.id.espacios);
        municipios= (CheckBox)findViewById(R.id.municipios);
        sql = "SELECT DISTINCT nombre FROM espacios_naturales WHERE nombre IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser = '" + usuario +"')";
        nomColumna = "nombre";
        Buscar();
    }

    public void relleno(){
        if(municipios.isChecked()) {
            AdapterListaMunicipios adapter = new AdapterListaMunicipios(NombreFavoritos, usuario);
            ListaFavoritos.setAdapter(adapter);
        }else{
            AdapterListaEspaciosNaturales adapter = new AdapterListaEspaciosNaturales(NombreFavoritos, usuario);
            ListaFavoritos.setAdapter(adapter);
        }
    }

    public void Escribir(){
        String Inicio="";
        if(municipios.isChecked()){
            if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
                Inicio= "SELECT nombre FROM municipios WHERE nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                nomColumna = "nombre";
            }else if(TextoConsulta.length()==0){
                Inicio= "SELECT nombre FROM municipios WHERE" +EscribirConsulta + " AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                nomColumna = "nombre";
            } else{
                Inicio="SELECT nombre FROM municipios" +TextoConsulta;
                nomColumna = "nombre";
            }

            sql=Inicio;
        }else{
            if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
                Inicio= "SELECT nombre FROM espacios_naturales WHERE nombre IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser = '" + usuario + "')";
                nomColumna = "nombre";
            }else if(TextoConsulta.length()==0){
                Inicio= "SELECT nombre FROM espacios_naturales WHERE nombre " +EscribirConsulta + " AND nombre IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser = '" + usuario + "')";
                nomColumna = "nombre";
            } else{
                Inicio="SELECT nomEspNat FROM espacios_naturales EN JOIN existe EX ON EN.nombre = EX.nomEspNat JOIN municipios MU ON EX.nomMunicipio = MU.nombre " +TextoConsulta;
                nomColumna = "nomEspNat";
            }
            sql=Inicio;
        }

        Buscar();
    }
    public void Check(){
        TextoConsulta="";
        if(municipios.isChecked()) {
            if (bizkaia.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=48 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=48 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nombre " + EscribirConsulta;
                }
            }
            if (alava.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=1 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=1 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nombre " + EscribirConsulta;
                }
            }
            if (gipuzkoa.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=20 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=20 AND nombre IN (SELECT nomMunicipio FROM es_favorito_mun WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nombre " + EscribirConsulta;
                }
            }
        }else{
            if (bizkaia.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=48 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=48 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nomEspNat " + EscribirConsulta;
                }
            }
            if (alava.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=1 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=1 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nomEspNat " + EscribirConsulta;
                }
            }
            if (gipuzkoa.isChecked() == true) {
                if (TextoConsulta.length() == 0) {
                    TextoConsulta = " WHERE idProvincia=20 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                } else {
                    TextoConsulta += " or idProvincia=20 AND nomEspNat IN (SELECT nomEspNat FROM es_favorito_esp WHERE idUser ='" + usuario + "')";
                }
                if (EscribirConsulta.length() > 0) {
                    TextoConsulta += " and nomEspNat " + EscribirConsulta;
                }
            }
        }
        Escribir();
    }
    public void AplicarFiltro(View v) {
        Check();
    }

    public void cambiarMunicipios(View v){
        espacios.setChecked(false);
        if(!municipios.isChecked()){
            municipios.setChecked(true);
        }
        Check();
    }

    public void cambiarEspaciosNaturales(View v){
        municipios.setChecked(false);
        if(!espacios.isChecked()){
            espacios.setChecked(true);
        }
        Check();
    }
    //------------------------------------------------------------------------

    public void Buscar(){
        ListaFavoritos.setVisibility(View.VISIBLE);
        try {
            if (isConnected()) {
                NombreFavoritos.clear();
                NombreFavoritos = conectarNombre(sql);
                if (NombreFavoritos.size()>0) {
                    ListaFavoritos.setVisibility(View.VISIBLE);
                    relleno(); // Mostramos en el textView el nombre.
                } else {
                    NombreFavoritos.add("");
                    relleno();
                    // Si la respuesta es null, una excepción ha ocurrido.
                    ListaFavoritos.setVisibility(View.INVISIBLE);
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
    private ArrayList conectarNombre(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = nomColumna;
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