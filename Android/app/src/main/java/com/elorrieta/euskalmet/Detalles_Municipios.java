package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class Detalles_Municipios extends AppCompatActivity {
    String NombreMun;
    TextView TxtMun;
    EditText Descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles__municipios);
        TxtMun= (TextView) findViewById(R.id.textMunicipio);
        Descripcion= (EditText) findViewById(R.id.TextDescripcion);

        ArrayList<String> ListaDescripcion = new ArrayList<String>();
        Bundle oExtras = getIntent().getExtras();
        NombreMun = oExtras.getString("Municipio");

        TxtMun.setText(NombreMun);
        String Consulta= "SELECT descripcion FROM municipios WHERE nombre='" + NombreMun +"'";
        try {
            ListaDescripcion = cargarDescripcion(Consulta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(ListaDescripcion.isEmpty()){
            Descripcion.setText("Lo sentimos no existe registros en nuestra base de datos");
        }else {
        Descripcion.setText(ListaDescripcion.get(0));
        }
    }
    private ArrayList cargarDescripcion(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "descripcion";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
}