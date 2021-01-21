package com.elorrieta.euskalmet;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Pantalla_Registro_Usuario extends AppCompatActivity {
    EditText etNombre,etContraNuev,etRepContra,etRecordatorio;
    ImageView Imagen;
    CheckBox Recordar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_registrar_user);
        etNombre = findViewById(R.id.editUsuario);
        etContraNuev = findViewById(R.id.editContNue);
        etRepContra = findViewById(R.id.editRepCont);

        etRecordatorio = findViewById(R.id.editRecordatorio);
        etRecordatorio.setVisibility(View.GONE);

        Recordar = findViewById(R.id.checkRecordar);

        Imagen= (ImageView)findViewById(R.id.imageView2);
        Imagen.setImageResource(R.drawable.logotipo);
    }

    public void Cancelar(View poView){
        finish();
    }

    public void Aparecer(View poView){
        if (Recordar.isChecked()==true){
            etRecordatorio.setVisibility(View.VISIBLE);
        }else{
            etRecordatorio.setVisibility(View.GONE);
            etRecordatorio.setText("");
        }
    }

    public void Guardar(View poView) throws InterruptedException {
        String Usuario=etNombre.getText().toString();
        String Contraseña=etContraNuev.getText().toString();
        String RepContraseña=etRepContra.getText().toString();
        String Recordar=etRecordatorio.getText().toString();
        String consulta;

        if (Usuario.equals("")||Contraseña.equals("")||RepContraseña.equals("")){
            Toast.makeText(this,R.string.CamposVaciosRegis,Toast.LENGTH_LONG).show();
            return;
        }
        ArrayList<String> ListaUsuario = new ArrayList<String>();
        consulta = "SELECT idUser FROM usuarios WHERE idUser='" + Usuario.trim() +"'";
        ListaUsuario = conectarConsultarUsuario(consulta);
        if(!ListaUsuario.isEmpty()){
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_LONG).show();
            return;
        }

        if (Contraseña.equals(RepContraseña)){
            Toast.makeText(this,R.string.ContraBuena,Toast.LENGTH_SHORT).show();
            // "INSERT INTO usuarios(idUser, password, recuperacion) VALUES ('"Usuario"', '"Contraseña"', '')"
            String Sql="INSERT INTO usuarios(idUser, password, recuperacion) VALUES ('" + Usuario + "', '" + Contraseña +"', '"+Recordar+"')";
            conectarInsertarUsuario(Sql);

            finish();
        }else{
            Toast.makeText(this,R.string.ContraMala,Toast.LENGTH_LONG).show();
            return;
        }

    }
    private void conectarInsertarUsuario(String consulta) throws InterruptedException {
        Client_Insercion_Update clientThread = new Client_Insercion_Update(consulta);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
    }
    private ArrayList conectarConsultarUsuario(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "IdUser";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
}
