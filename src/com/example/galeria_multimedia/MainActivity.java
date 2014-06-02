package com.example.galeria_multimedia;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 
 * @author sergioromero
 *
 */
public class MainActivity extends Activity {
    // Variable que utilizamos para indicar que hemos realizado la fotografia
    private static int TAKE_PICTURE = 1;
    // Definición del URI
    private Uri imageUri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		
		 // Capturamos el elemento de diseño de botón
        Button cameraButton = (Button)findViewById(R.id.button_camera);
        // Definimos el evento del botón.        
        cameraButton.setOnLongClickListener(cameraListener);
	}
	/**
	 * Implementación del evento OnLongClickListener
	 */
    private OnLongClickListener cameraListener = new OnLongClickListener() {		
		@Override
		public boolean onLongClick(View v) {
			// Llamamos al método que se encarga de hacer la fotografia y ponerla en la ImageView
            hacerFoto(v);
			return false;
		}
	};
    
	public void hacerFoto(View v) {
    	//Creamos el intent para poder utilizar la cámara. Utilizamos la definición definida por el media
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // Creamos un archivo llamado pic.jpg en el directorio estandard donde se almacenan las Imágenes
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic.jpg");
        //Guardamos en el fichero temporal pic.jpg los datos del intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        //Arrancamos la aplicación cámara
        startActivityForResult(intent, TAKE_PICTURE);
    }
	/**
	 * Implementación del menu de opciones de la apliación
	 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    /**
     * Implementamos los puntos de menú.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Salir:            	
            	finish();
                return true;
            case R.id.Help:
            	Toast.makeText(this, "Práctica Galería Multimedia realizada por Sergio Romero.", Toast.LENGTH_SHORT).show();
                return true;                
            default:            	
                return super.onOptionsItemSelected(item);
        }
    }
 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Evaluamos el valor que nos devuelve la actividad (pantalla cámara)
        switch(requestCode) {
        case 1:
            // Si se ha realizado la foto
            if(resultCode == Activity.RESULT_OK) {
                // Obtenemos la URI de la imagen
                Uri selectedImage = imageUri;
                //Notificamos al resto de las aplicaciones que se ha realizado la foto
                getContentResolver().notifyChange(selectedImage, null);
                // Capturamos el imageView donde irá la foto capturada
                ImageView imageView = (ImageView)findViewById(R.id.image_view_camera);
                // Creamos el content resolver para poder acceder al fichero de imagen definido por la uri
                ContentResolver cr = getContentResolver();
                // Creamos el objeto bitmap
                Bitmap bitmap;
                try {
                	// Cargamos el bitmap desde la uri definida del Content Resolver
                    bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, selectedImage);
                    // Asignamos el bitmap a la image view
                    imageView.setImageBitmap(bitmap);
                    // Notificamos al usuario
                    Toast.makeText(MainActivity.this, "Fotografía guardada correctamente", Toast.LENGTH_LONG).show();
                } catch(Exception e) {
                    // Noficamos el error al usuario.
                    Toast.makeText(MainActivity.this, "Error al recuperar la fotografía", Toast.LENGTH_LONG).show();                    
                }
            }
        }
    }
}

