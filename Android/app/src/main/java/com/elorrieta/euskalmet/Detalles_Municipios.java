package com.elorrieta.euskalmet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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
<<<<<<< HEAD
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Detalles_Municipios extends AppCompatActivity {
    static final int SOLICITUD_PERMISO_IMAGE_CAPTURE = 0;
    static final int SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE = 0;
    ImageView imagen;
    Button guardar;
=======
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
>>>>>>> 11f83c1c8fa11209f870603a13ff10d3e73bbc0e

public class Detalles_Municipios extends AppCompatActivity {
    String NombreMun;
    TextView TxtMun;
    EditText Descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalles__municipios);
<<<<<<< HEAD

        imagen = (ImageView) findViewById(R.id.ivFoto);
        guardar = (Button) findViewById(R.id.btnGuardar);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SOLICITUD_PERMISO_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imagen.setImageBitmap(imageBitmap);

            //En proceso
//            //Comprueba el permiso para grabar
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                // Create the File where the photo should go
//                File photoFile = null;
//                try {
//                    photoFile = createImageFile();
//                } catch (IOException ex) {
//                    // Error occurred while creating the File
//                    //...
//                }
//                // Continue only if the File was successfully created
//                if (photoFile != null) {
//                    Uri photoURI = FileProvider.getUriForFile(this,"com.example.android.euskalmet", photoFile);
//                    this.getIntent().putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                }
//            } else {
//                solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Sin el permiso"+
//                        " para escribir, no puedo guardar la foto.", SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, this);
//            }

        }
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

            //========================================================================GUARDAR A TAMAÑO REAL
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//                //...
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,"com.example.pruebafoto", photoFile);
//                tomarFoto.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(tomarFoto, SOLICITUD_PERMISO_IMAGE_CAPTURE);
//            }
            //===================================================================================
//            guardar imagen
            Save savefile = new Save();
            savefile.SaveImage(this, bmap);
        } else {
            solicitarPermiso(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Sin el permiso"+
                    " para escribir, no puedo guardar la foto.", SOLICITUD_PERMISO_WRITE_EXTERNAL_STORAGE, this);
        }
=======
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
>>>>>>> 11f83c1c8fa11209f870603a13ff10d3e73bbc0e
    }

    String currentPhotoPath;
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir("Android/data/com.example.android.pruebafoto/files/Pictures");
        //      File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}