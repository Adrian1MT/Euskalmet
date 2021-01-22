package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Listado_Playas extends AppCompatActivity {
    ImageView Imagen;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado__playas);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        Imagen= (ImageView)findViewById(R.id.imagenplaya);
        Imagen.setImageResource(R.drawable.playas);


    }
    public void Siguiente(View poView){
        Intent oIntent = new Intent(this, Top_Ranking.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
    public void anterior(View poView){
        Intent oIntent = new Intent(this, Listado_Municipios.class);
        oIntent.putExtra("usuario", usuario);
        startActivity(oIntent);
        finish();
    }
}