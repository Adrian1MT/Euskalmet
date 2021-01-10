package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class Listado_Municipios extends AppCompatActivity {
    ImageView Imagen;
    final int iCODIGO = 1234;

    ListView ListaMunicipios;
    ArrayList<String> NombreMunicipios = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__municipios);
        Imagen= (ImageView)findViewById(R.id.imagenmunicipio);
        Imagen.setImageResource(R.drawable.municipios);

        ListaMunicipios= (ListView)findViewById(R.id.ListadoMUN);
        relleno();
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
    public void relleno(){
        NombreMunicipios.add("Municipio 1");
        NombreMunicipios.add("Municipio 2");
        NombreMunicipios.add("Municipio 3");
        NombreMunicipios.add("Municipio 4");
        NombreMunicipios.add("Municipio 5");
        NombreMunicipios.add("Municipio 6");
        NombreMunicipios.add("Municipio 7");
        NombreMunicipios.add("Municipio 8");
        NombreMunicipios.add("Municipio 9");
        NombreMunicipios.add("Municipio 10");

        ArrayAdapter<String> adapter = new ArrayAdapter <String>
                (this,android.R.layout.simple_list_item_1,NombreMunicipios);
        ListaMunicipios.setAdapter(adapter);
    }
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
}