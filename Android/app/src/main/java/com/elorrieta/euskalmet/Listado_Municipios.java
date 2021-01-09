package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

public class Listado_Municipios extends AppCompatActivity {
    ImageView Imagen;
    final int iCODIGO = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__municipios);
        Imagen= (ImageView)findViewById(R.id.imagenmunicipio);
        Imagen.setImageResource(R.drawable.municipios);
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