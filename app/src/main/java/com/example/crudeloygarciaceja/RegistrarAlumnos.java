package com.example.crudeloygarciaceja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistrarAlumnos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnRegistrar;
    private TextView edtControl, edtNombre, edtTelefono;
    private Spinner spinnerCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_alumnos);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        edtControl = findViewById(R.id.edtControl);
        edtNombre = findViewById(R.id.edtNombre);
        edtTelefono = findViewById(R.id.edtTelefono);
        //spinner personalizado
        spinnerCustom = findViewById(R.id.spinnerCustom);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.alumnoSexo,
                R.layout.color_spinner
        );
        adapter.setDropDownViewResource(R.layout.spinner_lista);
        spinnerCustom.setAdapter(adapter);
        spinnerCustom.setOnItemSelectedListener(this);

        //evento registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarAlumno();
            }
        });

    }

    private void RegistrarAlumno(){
        String control = edtControl.getText().toString();
        String nombre = edtNombre.getText().toString();
        String sexo = spinnerCustom.getSelectedItem().toString();
        String telefono = edtTelefono.getText().toString();

        if (!control.isEmpty() && !nombre.isEmpty() && !sexo.isEmpty() && !telefono.isEmpty()){
            ConexionSQLiteOpenHelper conexion = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase hobbies = conexion.getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put("NUMCONTROL",control);
            value.put("NOMBRE",nombre);
            value.put("SEXO",sexo);
            value.put("TELEFONO",telefono);
            long resultado = hobbies.insert("ALUMNOS",null,value);
            Toast.makeText(this, "Alumno registrado correctamente",Toast.LENGTH_LONG).show();
            conexion.close();
            edtControl.setText("");;
            edtNombre.setText("");
            edtTelefono.setText("");
        }else
            Toast.makeText(this, "Debes de llenar todos los campos",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}