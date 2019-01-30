package edu.itsur.contactosapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import static android.provider.ContactsContract.*;

public class MainActivity extends AppCompatActivity {

    private final int PICK_CONTACT_REQUEST = 1;
    private Uri contactoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void initSeleccionarContacto(View v) {
        Intent i = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
        startActivityForResult(i, PICK_CONTACT_REQUEST);
    }

    private void recibirConctacto(Uri uri) {
        TextView nombre = (TextView) findViewById(R.id.nombreContacto);
        TextView telefono = (TextView) findViewById(R.id.telefonoContacto);
        ImageView foto = (ImageView) findViewById(R.id.fotoContacto);

        nombre.setText(getNombre(uri));
        telefono.setText(getTelefono(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                contactoUri = data.getData();

                recibirConctacto(contactoUri);
            }
        }
    }

    private String getNombre(Uri uri) {
        String nombre = null;

        ContentResolver contentResolver = getContentResolver();

        Cursor c = contentResolver.query(
                uri,
                new String[]{Contacts.DISPLAY_NAME},
                null,
                null,
                null
        );

        if (c.moveToFirst()) {
            nombre = c.getString(0);
        }
        c.close();

        return nombre;
    }

    private String getTelefono(Uri uri) {
        String id = null;
        String telefono = null;

        Cursor contactoCursor = getContentResolver().query(
                uri,
                new String[]{Contacts._ID},
                null,
                null,
                null
        );

        if (contactoCursor.moveToFirst()){
            id = contactoCursor.getString(0);
        }

        contactoCursor.close();

        String selectionArgs =
                CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        CommonDataKinds.Phone.TYPE + " = " +
                        CommonDataKinds.Phone.TYPE_MOBILE;

        Cursor telefonoCursor = getContentResolver().query(
                CommonDataKinds.Phone.CONTENT_URI,
                new String[] {CommonDataKinds.Phone.NUMBER},
                selectionArgs,
                new String[]{id},
                null
        );
        if (telefonoCursor.moveToFirst()){
            telefono = telefonoCursor.getString(0);
        }

        return telefono;
    }


}




