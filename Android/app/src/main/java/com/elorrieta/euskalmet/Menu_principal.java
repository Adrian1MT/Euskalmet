package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Menu_principal extends AppCompatActivity {
    ListView ListaOpciones;
    ArrayList<String> NombreTarea = new ArrayList<String>();
    ImageView Imagen;
    TextView time;

    final int iCODIGO = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        Imagen= (ImageView)findViewById(R.id.imageView3);
        Imagen.setImageResource(R.drawable.logotipo);
        ListaOpciones= (ListView)findViewById(R.id.Actividades);
        TodoElListado();

        time= (TextView)findViewById(R.id.tiempo);

        ListaOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Opciones(i);
            }
        });
        hora();
    }
    public void hora(){

            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
            time.setText(currentDateTimeString);

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
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void Opciones(int i){
        Intent oIntent;
        switch(i) {
            case 0:
                oIntent = new Intent(this, Listado_Municipios.class);
                startActivityForResult(oIntent, iCODIGO);
                break;
            case 1:
                oIntent = new Intent(this, Listado_Espacios_Naturales.class);
                startActivityForResult(oIntent, iCODIGO);
                break;
            case 2:
                oIntent = new Intent(this, Listado_Favoritos.class);
                startActivityForResult(oIntent, iCODIGO);
                break;
            case 3:
                oIntent = new Intent(this, Top_Ranking.class);
                startActivityForResult(oIntent, iCODIGO);
                break;
            case 4:
                oIntent = new Intent(this, Listado_Playas.class);
                startActivityForResult(oIntent, iCODIGO);
                break;
        }
    }
    public void TodoElListado(){
        NombreTarea.add("Listado de Municipios");
        NombreTarea.add("Listado de Espacios Naturales");
        NombreTarea.add("Favoritos");
        NombreTarea.add("Top Ranking");
        NombreTarea.add("Las Playas");
        ArrayAdapter<String> adapter = new ArrayAdapter <String>
                (this,android.R.layout.simple_list_item_1,NombreTarea);
        ListaOpciones.setAdapter(adapter);
    }
}