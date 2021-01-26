package com.elorrieta.euskalmet;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class Mapa extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener
{
    private GoogleMap mapa;
    private LatLng LUGAR;
    Button IR,Animado;
    Double longitud;
    Double latitud;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String nombre;
        Bundle oExtras = getIntent().getExtras();
        //longitud = oExtras.getDouble("longitud");

        //latitud = oExtras.getDouble("latitud");

        nombre= oExtras.getString("Nombre");

        try {
            List<Address> result = new Geocoder(this).getFromLocationName(nombre, 1000);
                latitud = result.get(0).getLatitude();
                longitud = result.get(0).getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }

        //IR=(Button) findViewById(R.id.button1);
        //IR.setTag("Ir a"+nombre);
        //Animado=(Button) findViewById(R.id.button2);
        //Animado.setTag("Animado a"+nombre);

        mapa();
        setContentView(R.layout.mapa);
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    public void mapa(){
        LUGAR = new LatLng(latitud, longitud);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        mapa.getUiSettings().setZoomControlsEnabled(false);
        // Oculta los controles de zoom.
                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(LUGAR, 15));
        //Nivel de zoom con el que queremos que entre.
                mapa.addMarker(new MarkerOptions()
                .position(LUGAR)
                .title("Elorrieta")
                .snippet("CIFP Elorrieta-Erreka Mari LHII")
                .icon(BitmapDescriptorFactory
                        .fromResource(android.R.drawable.ic_menu_compass))
                .anchor(0.5f, 0.5f));
        mapa.setOnMapClickListener(this);
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            mapa.setMyLocationEnabled(true);
            mapa.getUiSettings().setCompassEnabled(true);
        }
    }
    public void moveCamera(View view) {
        mapa.moveCamera(CameraUpdateFactory.newLatLng(LUGAR));
    }
    public void animateCamera(View view) {
        mapa.animateCamera(CameraUpdateFactory.newLatLng(LUGAR));
    }
    public void addMarker(View view) {
        mapa.addMarker(new MarkerOptions().position(
                mapa.getCameraPosition().target));
    }
    @Override
    public void onMapClick(LatLng puntoPulsado) {
        mapa.addMarker(new MarkerOptions().position(puntoPulsado)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
}
}
