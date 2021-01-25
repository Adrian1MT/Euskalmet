package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Top_Ranking extends AppCompatActivity {
    ImageView Imagen;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top__ranking);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        Imagen= (ImageView)findViewById(R.id.imagenranking);
        Imagen.setImageResource(R.drawable.ranking);
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