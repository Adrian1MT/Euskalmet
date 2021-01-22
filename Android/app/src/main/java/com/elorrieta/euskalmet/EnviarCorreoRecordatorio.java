package com.elorrieta.euskalmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EnviarCorreoRecordatorio extends AppCompatActivity {
    ImageView Imagen;
    Button button;
    EditText correo, Usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enviar_correo_recordatorio);

        Imagen= (ImageView)findViewById(R.id.Logotipo);
        Imagen.setImageResource(R.drawable.logotipo);

        correo = findViewById(R.id.TxtCorreo);
        Usuario = findViewById(R.id.TxtUsuario);
        button = findViewById(R.id.BtnEnviar);

    }
    public void Enviar(View v) {
        String enviarcorreo = correo.getText().toString();
        String enviarasunto = Usuario.getText().toString();
        String enviarmensaje = "Prueba0";

        // Defino mi Intent y hago uso del objeto ACTION_SEND
        Intent intent = new Intent(Intent.ACTION_SEND);

        // Defino los Strings Email, Asunto y Mensaje con la función putExtra
        intent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { enviarcorreo });
        intent.putExtra(Intent.EXTRA_SUBJECT, enviarasunto);
        intent.putExtra(Intent.EXTRA_TEXT, enviarmensaje);

        // Establezco el tipo de Intent
        intent.setType("message/rfc822");

        // Lanzo el selector de cliente de Correo
        startActivity( Intent.createChooser(intent,"Elije un cliente de Correo:"));
    }
}
