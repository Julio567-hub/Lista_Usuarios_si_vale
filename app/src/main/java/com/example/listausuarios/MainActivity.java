package com.example.listausuarios;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etEmail;
    Button btnGuarda1, btnVistaUsuarios;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNombre = findViewById(R.id.etNombre);
        etEmail = findViewById(R.id.etEmail);
        btnGuarda1 = findViewById(R.id.btnGuardado1);
        btnVistaUsuarios = findViewById(R.id.btnVerUsuarios);
        dbHelper = new DBHelper(this);

        btnGuarda1.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String email = etEmail.getText().toString();
            guardarUsuario(nombre, email);
        });

        btnVistaUsuarios.setOnClickListener(v ->{
            startActivity(new Intent(this, lista_usuarios.class));
        });
    }
    private void guardarUsuario(String nombre, String email){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("email", email);
        db.insert("usuarios",null, values);
        Toast.makeText(this, "Usuario guardado", Toast.LENGTH_SHORT).show();
        etNombre.setText("");
        etEmail.setText("");
    }


}
