package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Listado_Favoritos extends AppCompatActivity {
    ImageView Imagen;
    final int iCODIGO = 1234;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__favoritos);
        Imagen= (ImageView)findViewById(R.id.ImagenFavorito);
        Imagen.setImageResource(R.drawable.favoritos);
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
}