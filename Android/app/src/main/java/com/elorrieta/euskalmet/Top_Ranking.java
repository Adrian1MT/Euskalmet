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
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class Top_Ranking extends AppCompatActivity {
    private ConnectivityManager connectivityManager = null;
    ImageView Imagen;
    String usuario;
    RecyclerView ListaMunicipios;
    ArrayList<String> NombreMunicipios = new ArrayList<String>();
    String sql;
    CheckBox bizkaia,alava,gipuzkoa, ascendente,descendnte;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top__ranking);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        ListaMunicipios= (RecyclerView)findViewById(R.id.Ranking);
        ListaMunicipios.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ListaMunicipios.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Imagen= (ImageView)findViewById(R.id.imagenranking);
        Imagen.setImageResource(R.drawable.ranking);
        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia3);
        alava= (CheckBox)findViewById(R.id.checkAlaba3);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa3);

        ascendente= (CheckBox)findViewById(R.id.asc);
        descendnte= (CheckBox)findViewById(R.id.des);
        Check();
    }
    public void Check(){
        sql="";
        if (bizkaia.isChecked()==true){
            if (ascendente.isChecked()==true){
                sql="SELECT nombre FROM municipios where idProvincia = 48 ORDER BY nombre ASC LIMIT 0, 5";
            }else if(descendnte.isChecked()==true){
                sql="SELECT nombre FROM municipios where idProvincia = 48 ORDER BY nombre DESC LIMIT 0, 5";
            }
        }
        if (alava.isChecked()==true){
            if (ascendente.isChecked()==true){
                sql="SELECT nombre FROM municipios where idProvincia = 1  ORDER BY nombre ASC LIMIT 0, 5";
            }else if(descendnte.isChecked()==true){
                sql= "SELECT nombre FROM municipios where idProvincia = 1 ORDER BY nombre DESC LIMIT 0, 5";
            }
        }
        if (gipuzkoa.isChecked()==true){
            if (ascendente.isChecked()==true){
                sql="SELECT nombre FROM municipios where idProvincia = 20  ORDER BY nombre ASC LIMIT 0, 5";
            }else if(descendnte.isChecked()==true){
                sql="SELECT nombre FROM municipios where idProvincia = 20 ORDER BY nombre DESC LIMIT 0, 5";
            }
        }
        Buscar();
    }

    public void cambiarBizkaia(View v){
        alava.setChecked(false);
        gipuzkoa.setChecked(false);
        if(!bizkaia.isChecked()){
            bizkaia.setChecked(true);
        }
        Check();
    }

    public void cambiaralava(View v){
        bizkaia.setChecked(false);
        gipuzkoa.setChecked(false);
        if(!alava.isChecked()){
            alava.setChecked(true);
        }
        Check();
    }
    public void cambiargipuzkoa(View v){
        bizkaia.setChecked(false);
        alava.setChecked(false);
        if(!gipuzkoa.isChecked()){
            gipuzkoa.setChecked(true);
        }
        Check();
    }
    public void cambiarasc(View v){
        descendnte.setChecked(false);
        if(!ascendente.isChecked()){
            ascendente.setChecked(true);
        }
        Check();
    }
    public void cambiardes(View v){
        ascendente.setChecked(false);
        if(!descendnte.isChecked()){
            descendnte.setChecked(true);
        }
        Check();
    }
    public void Buscar(){
        try {
            if (isConnected()) {
                NombreMunicipios.clear();
                NombreMunicipios = conectar(sql);
                if (NombreMunicipios.size()>0) {
                    relleno(); // Mostramos en el textView el nombre.
                    ListaMunicipios.setVisibility(View.VISIBLE);
                } else {
                    NombreMunicipios.add("");
                    relleno();
                    // Si la respuesta es null, una excepción ha ocurrido.
                   /* Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION",
                            Toast.LENGTH_SHORT).show();*/
                    ListaMunicipios.setVisibility(View.INVISIBLE);
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
    public void relleno(){
        AdapterListaMunicipios adapter = new AdapterListaMunicipios(NombreMunicipios, usuario);
        ListaMunicipios.setAdapter(adapter);
    }
    private ArrayList conectar(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "nombre";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    public void Siguiente(View poView){
        Intent oIntent = new Intent(this, Listado_Favoritos.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Listado_Playas.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
}