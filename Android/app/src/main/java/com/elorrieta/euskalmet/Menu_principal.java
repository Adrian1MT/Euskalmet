package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Menu_principal extends AppCompatActivity {
    ListView ListaOpciones;
    ArrayList<String> NombreTarea = new ArrayList<String>();
    ImageView Imagen;
    final int iCODIGO = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        Imagen= (ImageView)findViewById(R.id.imageView3);
        Imagen.setImageResource(R.drawable.logotipo);
        ListaOpciones= (ListView)findViewById(R.id.Actividades);
        TodoElListado();
        ListaOpciones.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                Opciones(i);
            }
        });
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