package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button BtnRegistrar,BtnLoguer;
    TextView Usuario,pass;
    final int iCODIGO = 1234;
    EditText etNombre,etContra;
    ImageView Imagen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BtnRegistrar = findViewById(R.id.btnRegistrar);
        BtnLoguer = findViewById(R.id.btnLoguer);

        Usuario = findViewById(R.id.textUser);
        pass = findViewById(R.id.textPass);

        etNombre = (EditText)findViewById(R.id.EditUsuario);
        etContra = (EditText)findViewById(R.id.EditContraseña);

        Invisible();

        Imagen= (ImageView)findViewById(R.id.imageView);
        Imagen.setImageResource(R.drawable.logotipo);
        Imagen.setOnClickListener(this::mover_ObjectAnimator);
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
    public void mover_ObjectAnimator(View v) {
        ObjectAnimator oObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                Imagen,
                PropertyValuesHolder.ofFloat("translationY", -700),
                PropertyValuesHolder.ofFloat("scaleX", 0.9f),
                PropertyValuesHolder.ofFloat("scaleY", 0.9f)
        );
        oObjectAnimator.setDuration(2000L);
        oObjectAnimator.setStartDelay(500L);
        oObjectAnimator.start();

        visible();
    }
    public void Invisible(){
        BtnLoguer.setEnabled(false);
        BtnRegistrar.setEnabled(false);

        ViewPropertyAnimator oAnimation = BtnLoguer.animate();
        oAnimation.alpha(0f);
        oAnimation.start();

        ViewPropertyAnimator oAnimation2 = BtnRegistrar.animate();
        oAnimation2.alpha(0f);
        oAnimation2.start();

        ViewPropertyAnimator oAnimation3 = Usuario.animate();
        oAnimation3.alpha(0f);
        oAnimation3.start();

         ViewPropertyAnimator oAnimation4 = pass.animate();
         oAnimation4.alpha(0f);
         oAnimation4.start();

         ViewPropertyAnimator oAnimation5 = etNombre.animate();
         oAnimation5.alpha(0f);
         oAnimation5.start();

        ViewPropertyAnimator oAnimation6 = etContra.animate();
        oAnimation6.alpha(0f);
        oAnimation6.start();
    }
    public void visible(){
        BtnLoguer.setEnabled(true);
        BtnRegistrar.setEnabled(true);

        ViewPropertyAnimator oAnimation = BtnLoguer.animate();
        oAnimation.alpha(1f);
        oAnimation.setDuration(2000);
        oAnimation.setStartDelay(2500L);
        oAnimation.start();

        ViewPropertyAnimator oAnimation2 = BtnRegistrar.animate();
        oAnimation2.alpha(1f);
        oAnimation2.setDuration(2000);
        oAnimation2.setStartDelay(2500L);
        oAnimation2.start();

       ViewPropertyAnimator oAnimation3 = Usuario.animate();
        oAnimation3.alpha(1f);
        oAnimation3.setDuration(2000);
        oAnimation3.setStartDelay(2500L);
        oAnimation3.start();

        ViewPropertyAnimator oAnimation4 = pass.animate();
        oAnimation4.alpha(1f);
        oAnimation4.setDuration(2000);
        oAnimation4.setStartDelay(2500L);
        oAnimation4.start();

        ViewPropertyAnimator oAnimation5 = etNombre.animate();
        oAnimation5.alpha(1f);
        oAnimation5.setDuration(2000);
        oAnimation5.setStartDelay(2500L);
        oAnimation5.start();

        ViewPropertyAnimator oAnimation6 = etContra.animate();
        oAnimation6.alpha(1f);
        oAnimation6.setDuration(2000);
        oAnimation6.setStartDelay(2500L);
        oAnimation6.start();

    }
}