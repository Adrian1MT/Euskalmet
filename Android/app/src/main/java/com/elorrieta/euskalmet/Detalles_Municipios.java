package com.elorrieta.euskalmet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

public class Detalles_Municipios extends AppCompatActivity {
    static final int SOLICITUD_PERMISO_IMAGE_CAPTURE = 0;
    static final int SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE = 0;
    ImageView imagen;
    Button guardar;
    String NombreMun,Usuario;
    TextView TxtMun;
    EditText Descripcion;
    Spinner sEstaciones;
    ArrayList<String> listaEstaciones = new ArrayList<String>();
    ArrayList<String> listaComgm3 = new ArrayList<String>();
    ArrayList<String> listaCO8hmgm3 = new ArrayList<String>();
    ArrayList<String> listaNogm3 = new ArrayList<String>();
    ArrayList<String> listaNO2gm3 = new ArrayList<String>();
    ArrayList<String> listaNOXgm3 = new ArrayList<String>();
    ArrayList<String> listaPM10gm3 = new ArrayList<String>();
    ArrayList<String> listaPM25gm3 = new ArrayList<String>();
    ArrayList<String> listaSO2gm3 = new ArrayList<String>();
    String[] estaciones;
    String estacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles__municipios);

        sEstaciones = (Spinner) findViewById(R.id.spinner2);
        imagen = (ImageView) findViewById(R.id.ivFoto);
        guardar = (Button) findViewById(R.id.btnGuardar);
        TxtMun= (TextView) findViewById(R.id.textMunicipio);
        Descripcion= (EditText) findViewById(R.id.TextDescripcion);

        ArrayList<String> ListaDescripcion = new ArrayList<String>();
        Bundle oExtras = getIntent().getExtras();
        NombreMun = oExtras.getString("Municipio");
        Usuario= oExtras.getString("Usuario");
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

        Consulta = "SELECT nombre FROM estaciones_metereologicas WHERE nomMunicipio = '" + NombreMun + "'";
        try {
            listaEstaciones = cargarEstaciones(Consulta);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        estaciones = new String[listaEstaciones.size()];
        estaciones = listaEstaciones.toArray(estaciones);
        sEstaciones.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,estaciones));
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOLICITUD_PERMISO_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            String Foto=convertirImgeString(imageBitmap);
            String sql= "INSERT INTO fotos( nomMunicipio, idUser, foto) VALUES ('" + NombreMun + "', '" + Usuario +"', '"+Foto+"')";
            try {
                conectarInsertaFoto(sql);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            imagen.setImageBitmap(imageBitmap);

        }
    }
    private String convertirImgeString(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,arrayOutputStream);
        byte[] imagenByte=arrayOutputStream.toByteArray();
        String imagenString = android.util.Base64.encodeToString(imagenByte,android.util.Base64.DEFAULT);
        return imagenString;
    }
    private void conectarInsertaFoto(String consulta) throws InterruptedException {
        Client_Insercion_Update clientThread = new Client_Insercion_Update(consulta);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
    }
    public void hacerFoto(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent tomarFoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(tomarFoto, SOLICITUD_PERMISO_IMAGE_CAPTURE);

        } else {
            solicitarPermiso(Manifest.permission.CAMERA, "Sin el permiso" +
                    " para usar la cámara no puedo sacar la foto.", SOLICITUD_PERMISO_IMAGE_CAPTURE, this);
        }
        guardar.setVisibility(View.VISIBLE);
    }


    public static void solicitarPermiso(final String permiso, String justificacion, final int requestCode, final Activity actividad) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(actividad, permiso)){
            new AlertDialog.Builder(actividad)
                    .setTitle("Solicitud de permiso")
                    .setMessage(justificacion)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            ActivityCompat.requestPermissions(actividad, new String[]{permiso}, requestCode);
                        }}).show();
        } else {
            ActivityCompat.requestPermissions(actividad, new String[]{permiso},
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == SOLICITUD_PERMISO_IMAGE_CAPTURE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hacerFoto(this.getCurrentFocus());
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la acción", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                guardarFoto(this.getCurrentFocus());
            } else {
                Toast.makeText(this, "Sin el permiso, no puedo realizar la acción", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class Save {

        private Context TheThis;
        private String NameOfFolder = "/DCIM/Camera";
        private String NameOfFile = "";
//--------------------------------------------------
        public void SaveImage(Context context, Bitmap ImageToSave) {

            TheThis = context;
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
            String CurrentDateAndTime = getCurrentDateAndTime();
            File dir = new File(file_path);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");

            try {
                FileOutputStream fOut = new FileOutputStream(file);

                ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
                fOut.flush();
                fOut.close();
                MakeSureFileWasCreatedThenMakeAvabile(file);
                AbleToSave();
            }

            catch(FileNotFoundException e) {
                UnableToSave(e.toString());
            }
            catch(IOException e) {
                UnableToSave(e.toString());
            }

        }

        private void MakeSureFileWasCreatedThenMakeAvabile(File file){
            MediaScannerConnection.scanFile(TheThis,
                    new String[] { file.toString() } , null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }

        private String getCurrentDateAndTime() {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
            String formattedDate = df.format(c.getTime());
            return formattedDate;
        }

        private void UnableToSave(String e) {
            Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!" + e, Toast.LENGTH_LONG).show();
        }

        private void AbleToSave() {
            Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarFoto(View view) { // OJO!!En pruebas, sin asociar todavía al botón

        //convertir imagen a bitmap
        imagen.buildDrawingCache();
        Bitmap bmap = imagen.getDrawingCache();

        //Comprueba el permiso para grabar
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

//            guardar imagen
            Save savefile = new Save();
            savefile.SaveImage(this, bmap);
            
        } else {
            solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Sin el permiso"+
                    " para escribir, no puedo guardar la foto.", SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, this);
        }

    }


    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir("Android/data/com.example.android.pruebafoto/files/Pictures");

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //---------------------------------------------------------------
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
    private ArrayList cargarEstaciones(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "nombre";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarComgm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "Comgm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarCO8hmgm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "CO8hmgm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarNogm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "Nogm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarNO2gm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "NO2gm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarNOXgm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "NOXgm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarPM10gm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "PM10gm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarPM25gm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "PM25gm3";
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }
    private ArrayList cargarSO2gm3(String consulta) throws InterruptedException {
        ClientThread clientThread = new ClientThread(consulta);
        clientThread.columnaResultado = "SO2gm3";
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
    public void verCalidadAire(View v) throws InterruptedException {
        estacion = sEstaciones.getSelectedItem().toString();
        String consulta = "SELECT Comgm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaComgm3 = cargarComgm3(consulta);
        consulta = "SELECT CO8hmgm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaCO8hmgm3 = cargarCO8hmgm3(consulta);
        consulta = "SELECT Nogm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaNogm3 = cargarNogm3(consulta);
        consulta = "SELECT NO2gm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaNO2gm3 = cargarNO2gm3(consulta);
        consulta = "SELECT NOXgm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaNOXgm3 = cargarNOXgm3(consulta);
        consulta = "SELECT PM10gm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaPM10gm3 = cargarPM10gm3(consulta);
        consulta = "SELECT PM25gm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaPM25gm3 = cargarPM25gm3(consulta);
        consulta = "SELECT SO2gm3 FROM calidad_aire WHERE nomEstMet = '" + estacion + "'";
        listaSO2gm3 = cargarSO2gm3(consulta);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Calidad del aire de la estacion: " + estacion);
        builder.setMessage("Comgm3: " + listaComgm3.get(0) + "\n" +
                            "CO8hgm3: " + listaCO8hmgm3.get(0) + "\n" +
                            "Nogm3: " + listaNogm3.get(0) + "\n" +
                            "NO2gm3: " + listaNO2gm3.get(0) + "\n" +
                            "NOXgm3: " + listaNOXgm3.get(0) + "\n" +
                            "PM10gm3: " + listaPM10gm3.get(0) + "\n" +
                            "PM25gm3: " + listaPM25gm3.get(0) + "\n" +
                            "SO2gm3: " + listaSO2gm3.get(0)).setCancelable(true).show();
    }
}
