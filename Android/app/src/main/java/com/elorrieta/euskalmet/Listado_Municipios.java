package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listado_Municipios extends AppCompatActivity {
    ImageView Imagen;
    final int iCODIGO = 1234;

    private ConnectivityManager connectivityManager = null;
    String sql;
    ListView ListaMunicipios;
    ArrayList<String> NombreMunicipios = new ArrayList<String>();

    EditText Texto;

    CheckBox bizkaia,alava,gipuzkoa;
    String TextoConsulta="";
    String EscribirConsulta="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__municipios);
        Imagen= (ImageView)findViewById(R.id.imagenmunicipio);
        Imagen.setImageResource(R.drawable.municipios);

        ListaMunicipios= (ListView)findViewById(R.id.ListadoMUN);

        Texto= (EditText)findViewById(R.id.TextZona);
        Texto.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EscribirConsulta="";
                if(Texto.getText().toString().length()>0){
                    EscribirConsulta=" Nombre LIKE '"+Texto.getText().toString()+"%'";
                }
                Check();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        bizkaia= (CheckBox)findViewById(R.id.checkBizkaia2);
        alava= (CheckBox)findViewById(R.id.checkAlaba2);
        gipuzkoa= (CheckBox)findViewById(R.id.checkGipuzkoa2);
        sql = "SELECT Nombre FROM municipio";
        Buscar();
    }
    public void relleno(){
        ArrayAdapter<String> adapter = new ArrayAdapter <String>
                (this,android.R.layout.simple_list_item_1,NombreMunicipios);
        ListaMunicipios.setAdapter(adapter);
    }

    public void Escribir(){
        String Inicio="";
        if(TextoConsulta.length()==0 && EscribirConsulta.length()==0){
            Inicio= "SELECT Nombre FROM municipio";
        }else if(TextoConsulta.length()==0){
            Inicio= "SELECT Nombre FROM municipio WHERE" +EscribirConsulta;
        } else{
            Inicio="SELECT Nombre FROM municipio" +TextoConsulta;
        }
        sql=Inicio;
        Buscar();
    }
    public void Check(){
        TextoConsulta="";

        if (bizkaia.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE PaisOrigen='BIZKAIA'";
            }else{
                TextoConsulta+=" or PaisOrigen='BIZKAIA'";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (alava.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE PaisOrigen='ALABA'";
            }else{
                TextoConsulta+=" or PaisOrigen='ALABA'";
            }
            if(EscribirConsulta.length()>0){
                TextoConsulta+=" and"+EscribirConsulta;
            }
        }
        if (gipuzkoa.isChecked()==true){
            if(TextoConsulta.length()==0){
                TextoConsulta=" WHERE PaisOrigen='GIPUZKOA'";
            }else{
                TextoConsulta+=" or PaisOrigen='GIPUZKOA'";
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
                NombreMunicipios.clear();
                NombreMunicipios = conectar();
                if (NombreMunicipios.size()>0) {
                    relleno(); // Mostramos en el textView el nombre.
                } else {
                    NombreMunicipios.add("");
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
    //------------------------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seleccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.AcercaDe) {
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle("AcercaDe");
            msj.setMessage("R.string.MensajeAcerca");
            msj.setNeutralButton("R.string.Entendido", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }});
            AlertDialog mostrarDialogo =msj.create();
            mostrarDialogo.show();
        }
        if (id==R.id.cerrarSeseion) {
            //Mirar como cerrar ambas ventanas
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Siguiente(View poView){
        Intent oIntent = new Intent(this, Listado_Playas.class);
        startActivityForResult(oIntent, iCODIGO);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Listado_Espacios_Naturales.class);
        startActivityForResult(oIntent, iCODIGO);
        finish();
    }
}