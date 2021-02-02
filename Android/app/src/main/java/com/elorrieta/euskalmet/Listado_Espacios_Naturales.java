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

public class Listado_Espacios_Naturales extends AppCompatActivity {
    ImageView Imagen;
    final int iCODIGO = 1234;

    private ConnectivityManager connectivityManager = null;
    String sql;
    RecyclerView ListaEspaciosNaturales;
    ArrayList<String> NombreEspaciosNaturales = new ArrayList<String>();

    EditText Texto;

    CheckBox bizkaia,alava,gipuzkoa;
    String TextoConsulta="";
    String EscribirConsulta="";
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__espacios__naturales);
        Imagen= (ImageView)findViewById(R.id.imagennatural);
        Imagen.setImageResource(R.drawable.espaciosnaturales);

        ListaEspaciosNaturales = (RecyclerView)findViewById(R.id.ListadoESP);

        ListaEspaciosNaturales.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ListaEspaciosNaturales.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        Texto= (EditText)findViewById(R.id.TextZona);
        Texto.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EscribirConsulta="";
                if(Texto.getText().toString().length()>0){
                    EscribirConsulta=" nomEspNat LIKE '"+Texto.getText().toString().trim()+"%'";
                }
                Check();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia2);
        alava= (CheckBox)findViewById(R.id.checkAlaba2);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa2);
        sql = "SELECT DISTINCT nomEspNat FROM espacios_naturales EN JOIN existe EX ON EN.nombre = EX.nomEspNat JOIN municipios MU ON EX.nomMunicipio = MU.nombre ";
        Buscar();
    }
    public void relleno(){
        AdapterListaEspaciosNaturales adapter = new AdapterListaEspaciosNaturales(NombreEspaciosNaturales, usuario);
        ListaEspaciosNaturales.setAdapter(adapter);
    }

    public void Escribir(){
        String Inicio="";
        if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
            Inicio= "SELECT DISTINCT nomEspNat FROM espacios_naturales EN JOIN existe EX ON EN.nombre = EX.nomEspNat JOIN municipios MU ON EX.nomMunicipio = MU.nombre ";
        }else if(TextoConsulta.length()==0){
            Inicio= "SELECT DISTINCT nomEspNat FROM espacios_naturales EN JOIN existe EX ON EN.nombre = EX.nomEspNat JOIN municipios MU ON EX.nomMunicipio = MU.nombre WHERE" +EscribirConsulta;
        } else{
            Inicio="SELECT DISTINCT nomEspNat FROM espacios_naturales EN JOIN existe EX ON EN.nombre = EX.nomEspNat JOIN municipios MU ON EX.nomMunicipio = MU.nombre " +TextoConsulta;
        }
        sql=Inicio;
        Buscar();
    }
    public void Check(){
        TextoConsulta="";

        if (bizkaia.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE MU.idProvincia=48";
            }else{
                TextoConsulta+=" or MU.idProvincia=48";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (alava.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE MU.idProvincia=1";
            }else{
                TextoConsulta+=" or MU.idProvincia=1";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (gipuzkoa.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE MU.idProvincia=20";
            }else{
                TextoConsulta+=" or MU.idProvincia=20";
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
                NombreEspaciosNaturales.clear();
                NombreEspaciosNaturales = conectar(sql);
                if (NombreEspaciosNaturales.size()>0) {
                    relleno(); // Mostramos en el textView el nombre.
                    ListaEspaciosNaturales.setVisibility(View.VISIBLE);
                } else {
                    NombreEspaciosNaturales.add("");
                    relleno();
                    // Si la respuesta es null, una excepción ha ocurrido.
                   /* Toast.makeText(getApplicationContext(), "ERROR_COMUNICACION",
                            Toast.LENGTH_SHORT).show();*/
                    ListaEspaciosNaturales.setVisibility(View.INVISIBLE);
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
        clientThread.columnaResultado = "nomEspNat";
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
        Intent oIntent = new Intent(this, Listado_Municipios.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Listado_Favoritos.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
}