package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

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
    final int iCODIGO = 1234;
    TextView oTextView;
    private ConnectivityManager connectivityManager = null;

    EditText Texto;

    TextView consulta;
    CheckBox bizkaia,alava,gipuzkoa;
    String TextoConsulta="";
    String EscribirConsulta="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__favoritos);
        Imagen= (ImageView)findViewById(R.id.ImagenFavorito);
        Imagen.setImageResource(R.drawable.favoritos);
        oTextView= (TextView)findViewById(R.id.textView);

        Texto= (EditText)findViewById(R.id.textZona);
        Texto.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EscribirConsulta="";
                if(Texto.getText().toString().length()>0){
                    EscribirConsulta=" Nombre LIKE ‘"+Texto.getText().toString()+"%'";
                }
                Escribir();
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        consulta= (TextView)findViewById(R.id.textoConsulta);
        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia);
        alava= (CheckBox)findViewById(R.id.checkAlaba);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa);

    }

    public void Escribir(){
        String Inicio="";
        if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
                Inicio= "SELECT Nombre FROM municipio";
        }else if(TextoConsulta.length()==0){
            Inicio= "SELECT Nombre FROM municipio WHERE" +EscribirConsulta;
        }else if(EscribirConsulta.length()==0){
            Inicio="SELECT Nombre FROM municipio" +TextoConsulta;
        }else{
            Inicio="SELECT Nombre FROM municipio" +TextoConsulta+" or"+EscribirConsulta;
        }
        consulta.setText(Inicio);
    }

    public void Siguiente(View poView){
        Intent oIntent = new Intent(this, Listado_Espacios_Naturales.class);
        startActivityForResult(oIntent, iCODIGO);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Top_Ranking.class);
        startActivityForResult(oIntent, iCODIGO);
        finish();
    }
    public void conectarOnClick(View v) {
        try {
            if (isConnected()) {
                ArrayList<String> sRespuesta = conectar();
                if (null == sRespuesta) { // Si la respuesta es null, una excepción ha ocurrido.
                    Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION",
                            Toast.LENGTH_SHORT).show();
                } else {
                    oTextView.setText(sRespuesta.get(0).toString()); // Mostramos en el textView el nombre.
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
    String sql = "SELECT Nombre FROM municipio WHERE Nombre='Amurrio'";

    private ArrayList conectar() throws InterruptedException {
        ClientThread clientThread = new ClientThread(sql);
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
    public void AplicarFiltro(View v) {
        TextoConsulta="";

        if (bizkaia.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE Nombre='bizkaia'";
            }else{
                TextoConsulta+=" or Nombre='bizkaia'";
            }
        }
        if (alava.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE Nombre='alava'";
            }else{
                TextoConsulta+=" or Nombre='alava'";
            }
        }
        if (gipuzkoa.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE Nombre='gipuzkoa'";
            }else{
                TextoConsulta+=" or Nombre='gipuzkoa'";
            }
        }
        Escribir();
    }
}