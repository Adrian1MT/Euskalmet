package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Listado_Espacios_Naturales extends AppCompatActivity {
    ImageView Imagen;
    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");

        setContentView(R.layout.listado__espacios__naturales);
        Imagen= (ImageView)findViewById(R.id.imagennatural);
        Imagen.setImageResource(R.drawable.espaciosnaturales);
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