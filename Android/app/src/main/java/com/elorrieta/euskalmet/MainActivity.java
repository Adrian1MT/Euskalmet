package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button BtnRegistrar,BtnLoguer;
    TextView Usuario,pass, inicio;
    final int iCODIGO = 1234;
    EditText etNombre,etContra;
    ImageView Imagen;

    Boolean Terminar=false;
    ObjectAnimator AparecertAnimator;
    ObjectAnimator DesaparecertAnimator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BtnRegistrar = findViewById(R.id.btnRegistrar);
        BtnLoguer = findViewById(R.id.btnLoguer);

        Usuario = findViewById(R.id.textUser);
        pass = findViewById(R.id.textPass);
        inicio= findViewById(R.id.textTocar);

        etNombre = (EditText)findViewById(R.id.EditUsuario);
        etContra = (EditText)findViewById(R.id.EditContraseña);

        Invisible();

        Imagen= (ImageView)findViewById(R.id.imageView);
        String idioma;
        idioma=(getString(R.string.IDIOMA));
        if (idioma.equals("Castellano")){
            Imagen.setImageResource(R.drawable.logotipo);
        }else{
            Imagen.setImageResource(R.drawable.logotipoeus);
        }

        Imagen.setOnClickListener(this::mover_ObjectAnimator);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_inicio_sesion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.Opcion1Acerca) {
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle(R.string.Acerca);
            msj.setMessage(R.string.MensajeAcerca);
            msj.setNeutralButton(R.string.Entendido, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }});
            AlertDialog mostrarDialogo =msj.create();
            mostrarDialogo.show();
        }
        if (id==R.id.Opcion2Recordar) {
            Intent oIntent = new Intent(this, EnviarCorreoRecordatorio.class);
            startActivityForResult(oIntent, iCODIGO);
        }
        return super.onOptionsItemSelected(item);
    }
    public void REGISTRAR(View poView){
        Intent oIntent = new Intent(this, Pantalla_Registro_Usuario.class);
        startActivityForResult(oIntent, iCODIGO);
    }
    public void LOGUEAR(View poView) throws InterruptedException {
        String consulta;
        String Usuario=etNombre.getText().toString();
        String Contraseña=etContra.getText().toString();
        if (Usuario.trim().equals("")||Contraseña.trim().equals("")){
            Toast.makeText(this,R.string.CamposVacios,Toast.LENGTH_LONG).show();
            return;
        }else{
            ArrayList<String> listaContrasenia = new ArrayList<String>();
            consulta = "SELECT PASSWORD FROM usuarios WHERE idUser='" + Usuario.trim() +"'";
            listaContrasenia = conectar(consulta);
            if(listaContrasenia.isEmpty()){
                Toast.makeText(this, R.string.NoExisteUser, Toast.LENGTH_LONG).show();
                return;
            }else{
                if(listaContrasenia.get(0).equals(Contraseña.trim())){
                    Toast.makeText(this,R.string.Welcome,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, Menu_principal.class);
                    i.putExtra("usuario", Usuario.trim());
                    startActivity(i);
                    etNombre.setText("");
                    etContra.setText("");
                }else{
                    Toast.makeText(this,R.string.UserPassMalEscrito,Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
    private ArrayList conectar(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "password";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
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

        BtnLoguer.setVisibility(View.INVISIBLE);
        BtnRegistrar.setVisibility(View.INVISIBLE);

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

        Cartel();
    }
    public void Cartel(){
        if (Terminar==false){

        AparecertAnimator = ObjectAnimator.ofFloat(inicio,View.ALPHA,1f,0f);
        AparecertAnimator.setDuration(500);
        AnimatorSet Animacioncontinua =new AnimatorSet();
        Animacioncontinua.playTogether(AparecertAnimator);
        Animacioncontinua.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                DesaparecertAnimator=ObjectAnimator.ofFloat(inicio,View.ALPHA,0f,1f);
                DesaparecertAnimator.setDuration(500);

                AnimatorSet AnimacionRepetir =new AnimatorSet();
                AnimacionRepetir.playTogether(DesaparecertAnimator);
                AnimacionRepetir.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Cartel();
                    }
                });
                AnimacionRepetir.start();
            }
        });
        Animacioncontinua.start();
        }if (Terminar==true){
            ViewPropertyAnimator oAnimation7 = inicio.animate();
            oAnimation7.alpha(0f);
            oAnimation7.setDuration(500L);
            oAnimation7.start();
        }
    }

    public void visible(){
        Terminar=true;
        BtnLoguer.setEnabled(true);
        BtnRegistrar.setEnabled(true);

        BtnLoguer.setVisibility(View.VISIBLE);
        BtnRegistrar.setVisibility(View.VISIBLE);

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