package com.elorrieta.euskalmet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Pantalla_Registro_Usuario extends AppCompatActivity {
    EditText etNombre,etContraNuev,etRepContra;
    ImageView Imagen;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registrar_user);
        etNombre = findViewById(R.id.editUsuario);
        etContraNuev = findViewById(R.id.editContNue);
        etRepContra = findViewById(R.id.editRepCont);
        Imagen= (ImageView)findViewById(R.id.imageView2);
        Imagen.setImageResource(R.drawable.logotipo);
    }

    public void Cancelar(View poView){
        finish();
    }

    public void Guardar(View poView){
        String Usuario=etNombre.getText().toString();
        String Contraseña=etContraNuev.getText().toString();
        String RepContraseña=etRepContra.getText().toString();
        SharedPreferences prefe = getSharedPreferences("Usuarios", Context.MODE_PRIVATE);

        if (Usuario.equals("")||Contraseña.equals("")||RepContraseña.equals("")){
            Toast.makeText(this,R.string.CamposVaciosRegis,Toast.LENGTH_LONG).show();
            return;
        }

        if (Contraseña.equals(RepContraseña)){
            Toast.makeText(this,R.string.ContraBuena,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,R.string.ContraMala,Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences.Editor editor =prefe.edit();
        editor.putString(Usuario,Contraseña);
        editor.commit();

        finish();
    }
}
