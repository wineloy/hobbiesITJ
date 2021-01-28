package com.example.crudeloygarciaceja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrarHobbies extends AppCompatActivity implements View.OnClickListener{

    private Button btnBuscarAlumno, btnRegistroHobbie;
    private EditText edtBusquedaHobbie;
    //formulario
    private EditText edtNumeroControl, edtNombre, edtSexo, edtTelefono,edtDescripcionHobbie, edtTiempoHobbie;
    private int IdAlumno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_hobbies);
        // Hobbie
        btnBuscarAlumno = findViewById(R.id.btnBuscarAlumno);
        btnRegistroHobbie = findViewById(R.id.btnRegistroHobbie);
        btnBuscarAlumno.setOnClickListener(this);
        btnRegistroHobbie.setOnClickListener(this);
        //Vistas
        edtNumeroControl = findViewById(R.id.edtNumeroControl);
        edtNombre = findViewById(R.id.edtNombreprimer);
        edtSexo = findViewById(R.id.edtSexo);
        edtTelefono = findViewById(R.id.edtTelefonoprimero);
        edtDescripcionHobbie = findViewById(R.id.edtDescripcionHobbie);
        edtTiempoHobbie = findViewById(R.id.edtTiempoHobbie);

        edtBusquedaHobbie = findViewById(R.id.edtBusquedaAlumno);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscarAlumno:
                BuscarAlumno();
                break;

            case R.id.btnRegistroHobbie:
                RegistrarHobbie();
                break;
        }
    }

    private void BuscarAlumno(){
        String[] numControl = {edtBusquedaHobbie.getText().toString()};
        if (!numControl[0].isEmpty()){
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getReadableDatabase();
            //campos solicitados
            String[] campos = {"IDALUMNO","NUMCONTROL", "NOMBRE", "SEXO", "TELEFONO"};
            // solo se espera un resultado
            try {
                Cursor cursor = db.query("ALUMNOS",campos,"NUMCONTROL = ?",numControl,null,null,null );
                cursor.moveToFirst();
                IdAlumno = Integer.parseInt(cursor.getString(0));
                edtNumeroControl.setText(cursor.getString(1));
                edtNombre.setText(cursor.getString(2));
                edtSexo.setText(cursor.getString(3));
                edtTelefono.setText(cursor.getString(4));
                db.close();
                conn.close();
                cursor.close();
            }catch (Exception e){
                Toast.makeText(this,"Numero de control no registrado",Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(this,"Ingresa un numero de control",Toast.LENGTH_LONG).show();

    }

    private void RegistrarHobbie(){
        String descripcion = edtDescripcionHobbie.getText().toString();
        String tiempo = edtTiempoHobbie.getText().toString();
        String numControl = edtBusquedaHobbie.getText().toString();
        if (!descripcion.isEmpty() && !numControl.isEmpty()){
            try {
                ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
                SQLiteDatabase db = conn.getWritableDatabase();
                ContentValues value = new ContentValues();
                value.put("IDALUMNO",IdAlumno);
                value.put("DESCRIPCION",descripcion);
                value.put("DEDICACION",tiempo);
                long resultado = db.insert("HOBBIE",null,value);
                Toast.makeText(this, "Hobbie agregada Correctamente",Toast.LENGTH_LONG).show();
                conn.close();
                edtDescripcionHobbie.setText("");;
                edtTiempoHobbie.setText("");
            } catch (Exception e){
                Toast.makeText(this,"Error al registrar, intenta reiniciar app",Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this,"Ingrese la informacion completa de hobbie",Toast.LENGTH_LONG).show();
        }
    }

}