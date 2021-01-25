package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Detalles_Municipios extends AppCompatActivity  {
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
    private ArrayList cargarLongitud(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "longitud";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarLatitud(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "latitud";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    public void cargarmapa(View poView) throws InterruptedException {
        Double Longitud;
        Double Latitud;

        String Consulta= "SELECT longitud FROM municipios WHERE nombre='" + NombreMun +"'";
        ArrayList<String> ListaPosicion = new ArrayList<String>();
        ListaPosicion = cargarLongitud(Consulta);
        Longitud=Double.parseDouble(ListaPosicion.get(0));

        ListaPosicion.clear();

        Consulta= "SELECT latitud FROM municipios WHERE nombre='" + NombreMun +"'";
        ListaPosicion = cargarLatitud(Consulta);
        Latitud=Double.parseDouble(ListaPosicion.get(0));

        Intent oIntent = new Intent(this, Mapa.class);
        oIntent.putExtra("longitud", Longitud);
        oIntent.putExtra("latitud", Latitud);
        oIntent.putExtra("Nombre", NombreMun);
        startActivity(oIntent);
    }
}