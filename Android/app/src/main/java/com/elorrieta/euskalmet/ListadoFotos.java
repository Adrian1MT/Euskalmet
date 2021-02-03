package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoFotos extends AppCompatActivity {
    String sql;
    private ConnectivityManager connectivityManager = null;
    RecyclerView recycler;
    ArrayList<String> StringList = new ArrayList<String>();
    ImageView fotito;
    String usuario, Lugar;
    TextView mensaje;
    int Opcion,otro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_fotos);
        mensaje=(TextView)findViewById(R.id.Mensaje);

        Bundle oExtras = getIntent().getExtras();
        Opcion= oExtras.getInt("Opcion");
        otro= oExtras.getInt("otro");
        if (Opcion==1){
            usuario= oExtras.getString("Usuario");
            mensaje.setText(getString(R.string.mio)+usuario);
            sql= "SELECT foto FROM fotos where idUser='"+usuario+"'";
        }else{
            Lugar= oExtras.getString("Lugar");
            mensaje.setText(getString(R.string.soyo)+Lugar);
            if(otro==3){
                sql= "SELECT foto FROM fotos where nomMunicipio=(SELECT nomMunicipio FROM existe where nomEspNat='"+Lugar+"')";
            }else{
            sql= "SELECT foto FROM fotos where nomMunicipio='"+Lugar+"'";
            }
        }

        fotito= (ImageView)findViewById(R.id.FOTO);
        recycler= (RecyclerView)findViewById(R.id.ReciclerFoto);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        String idioma;
        idioma=(getString(R.string.IDIOMA));
        if (idioma.equals("Castellano")){
            fotito.setImageResource(R.drawable.logotipo);
        }else{
            fotito.setImageResource(R.drawable.logotipoeus);
        }
        Buscar();
    }

    public void relleno(){
        AdapterListaFotos adapter = new AdapterListaFotos(StringList);
        adapter.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               String foto="";
                int id = recycler.getChildAdapterPosition(view);
               foto=StringList.get(id).toString();
               byte[] decodedstring = android.util.Base64.decode(foto,android.util.Base64.DEFAULT);
               Bitmap decodeByte = BitmapFactory.decodeByteArray(decodedstring,0,decodedstring.length);
               fotito.setImageBitmap(decodeByte);
            }
        });
        recycler.setAdapter(adapter);
    }

    public void Buscar(){
        try {
            if (isConnected()) {
                StringList.clear();
                StringList = conectar(sql);
                if (StringList.size()>0) {
                    relleno(); // Mostramos en el textView el nombre.
                    recycler.setVisibility(View.VISIBLE);
                } else {
                    StringList.add("");
                    relleno();
                    // Si la respuesta es null, una excepción ha ocurrido.
                    recycler.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), R.string.nada,
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            // This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) &&
                    (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error_comunicación",
                    Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
    private ArrayList conectar(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "foto";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
}