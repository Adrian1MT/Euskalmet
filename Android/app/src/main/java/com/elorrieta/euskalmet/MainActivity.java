package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button BtnRegistrar,BtnLoguer;
    final int iCODIGO = 1234;
    EditText etNombre,etContra;
    ImageView Imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BtnRegistrar = findViewById(R.id.btnRegistrar);
        BtnLoguer = findViewById(R.id.btnLoguer);
        etNombre = (EditText)findViewById(R.id.EditUsuario);
        etContra = (EditText)findViewById(R.id.EditContraseña);
        Imagen= (ImageView)findViewById(R.id.imageView);
        Imagen.setImageResource(R.drawable.logotipo);
    }
    public void REGISTRAR(View poView){
        Intent oIntent = new Intent(this, Pantalla_Registro_Usuario.class);
        startActivityForResult(oIntent, iCODIGO);
    }
    public void LOGUEAR(View poView){
        String Usuario=etNombre.getText().toString();
        String Contraseña=etContra.getText().toString();
        if (Usuario.equals("")||Contraseña.equals("")){
            Toast.makeText(this,"CamposVacios",Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences prefe = getSharedPreferences("Usuarios", Context.MODE_PRIVATE);
        String User = prefe.getString(Usuario,"");
        if(User.length()==0){
            Toast.makeText(this,"R.string.NoExisteUser",Toast.LENGTH_LONG).show();
            return;
        }
        if (User.equals(Contraseña)){
            Toast.makeText(this,"Bienvenido",Toast.LENGTH_SHORT).show();
            Intent oIntent = new Intent(this, Menu_principal.class);
            startActivityForResult(oIntent, iCODIGO);
        }else{
            Toast.makeText(this,"UserPassMalEscrito",Toast.LENGTH_LONG).show();
            return;
        }

    }
}