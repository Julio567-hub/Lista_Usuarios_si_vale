package com.example.listausuarios;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class lista_usuarios extends AppCompatActivity {

    ListView listUsuarios;
    ArrayAdapter<String> adapter;
    ArrayList<String> lista;
    ArrayList<Usuarios> usuarios;
    DBHelper dbhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);
        listUsuarios = findViewById(R.id.listUsuarios);
        dbhelper = new DBHelper(this);
        cargarUsuarios();
        listUsuarios.setOnItemClickListener((parent, view, position, id) ->{
            Usuarios usuario = usuarios.get(position);
            mostrarDialogo(usuario);
        });

    }
    private void cargarUsuarios(){
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM usuarios",null);
        lista = new ArrayList<>();
        usuarios = new ArrayList<>();

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            String email = cursor.getString(2);
            usuarios.add(new Usuarios(id, nombre, email));
            lista.add(nombre+" - "+email);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listUsuarios.setAdapter(adapter);
    }

    private void mostrarDialogo(Usuarios usuario){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar o Eliminar");

        final EditText inputNombre = new EditText(this);
        inputNombre.setText(usuario.nombre);
        final EditText inputEmail = new EditText(this);
        inputEmail.setText(usuario.email);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputNombre);
        layout.addView(inputEmail);
        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, wich) ->{
            actualizarUsuario(usuario.id, inputNombre.getText().toString(), inputEmail.getText().toString());
        });

        builder.setNegativeButton("Eliminar", (dialog, wich) ->{
            eliminarUsuario(usuario.id);
        });

        builder.setNeutralButton("Cancelar", null);
        builder.show();
    }

    private void actualizarUsuario(int id, String nombre, String email){
        SQLiteDatabase db= dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        db.update("usuarios", values, "id=?", new String[]{String.valueOf(id)});
        cargarUsuarios();
    }

    private void eliminarUsuario(int id){
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete("usuarios","id=?", new String[]{String.valueOf(id)});
        cargarUsuarios();
    }
}