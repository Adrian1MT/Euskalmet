package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class ListadoFotos extends AppCompatActivity {
    RecyclerView recycler;
    ArrayList<ImageView> ImagenList = new ArrayList<ImageView>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_fotos);

        recycler= (RecyclerView)findViewById(R.id.ReciclerFoto);
        AdapterListaFotos adapter = new AdapterListaFotos(ImagenList);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = recycler.getChildAdapterPosition(view);
            }
        });

        recycler.setAdapter(adapter);
    }
    public void TodoElListado(){
       
    }
}