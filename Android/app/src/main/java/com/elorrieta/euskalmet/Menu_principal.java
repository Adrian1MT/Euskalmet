package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Menu_principal extends AppCompatActivity {
    RecyclerView recycler;
    ArrayList<String> NombreTarea = new ArrayList<String>();
    ImageView Imagen;
    TextView time;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_principal);
        Bundle extras = getIntent().getExtras();
        usuario = extras.getString("usuario");


        Imagen= (ImageView)findViewById(R.id.imageView3);
        Imagen.setImageResource(R.drawable.logotipo);
        recycler= (RecyclerView)findViewById(R.id.recycler);
        time= (TextView)findViewById(R.id.tiempo);

        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        TodoElListado();

        AdapterMenu adapter = new AdapterMenu(NombreTarea);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = recycler.getChildAdapterPosition(view);
                Opciones(id);
            }
        });

        recycler.setAdapter(adapter);

        hora();
    }
    public void hora(){
            String currentDateTimeString = DateFormat.getTimeInstance().format(new Date());
            time.setText(currentDateTimeString);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_seleccion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.AcercaDe) {
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle("AcercaDe");
            msj.setMessage("R.string.MensajeAcerca");
            msj.setNeutralButton("R.string.Entendido", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }});
            AlertDialog mostrarDialogo =msj.create();
            mostrarDialogo.show();
        }
        if (id==R.id.cerrarSeseion) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void Opciones(int i){
        Intent oIntent;
        switch(i) {
            case 0:
                oIntent = new Intent(this, Listado_Municipios.class);
                oIntent.putExtra("usuario", usuario);
                startActivity(oIntent);
                break;
            case 1:
                oIntent = new Intent(this, Listado_Espacios_Naturales.class);
                oIntent.putExtra("usuario", usuario);
                startActivity(oIntent);
                break;
            case 2:
                oIntent = new Intent(this, Listado_Favoritos.class);
                oIntent.putExtra("usuario", usuario);
                startActivity(oIntent);
                break;
            case 3:
                oIntent = new Intent(this, Top_Ranking.class);
                oIntent.putExtra("usuario", usuario);
                startActivity(oIntent);
                break;
        }
    }
    public void TodoElListado(){
        NombreTarea.add(getString(R.string.ListMunicipios));
        NombreTarea.add(getString(R.string.ListEspNat));
        NombreTarea.add(getString(R.string.Favoritos));
        NombreTarea.add(getString(R.string.TRanking));
    }
}