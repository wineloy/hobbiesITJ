package com.example.crudeloygarciaceja;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ModificacionesDatos extends AppCompatActivity implements View.OnClickListener {
    private TextView tvCantidadHobbies;
    private EditText edtBusqueda;
    private Button btnBuscarAlumno, btnActualizarDatos, btnBorrarHobbie,btnEliminarAlumno;
    private ImageView anterior,siguiente;
    //informacion
    private EditText edtNumeroControl, edtNombre, edtSexo, edtTelefono, edtDescripcionHobbie, edtTiempoHobbie;
    //informacion
    private int IdAlumno;
    String[][] lista;
    int fila = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificaciones_datos);
        //mapeo
        tvCantidadHobbies = findViewById(R.id.tvCantidadHobbies);
        edtBusqueda = findViewById(R.id.edtBusqueda);
        //Botones
        btnBuscarAlumno = findViewById(R.id.btnBuscarAlumno);
        btnActualizarDatos = findViewById(R.id.btnActualizarDatos);
        btnBorrarHobbie = findViewById(R.id.btnBorrarHobbie);
        btnEliminarAlumno = findViewById(R.id.btnEliminarAlumno);
        siguiente = findViewById(R.id.siguiente);
        anterior = findViewById(R.id.anterior);
        // formulario
        edtNumeroControl = findViewById(R.id.edtNumeroControl);
        edtNombre = findViewById(R.id.edtNombreprimer);
        edtSexo = findViewById(R.id.edtSexo);
        edtTelefono = findViewById(R.id.edtTelefonoprimero);
        edtDescripcionHobbie = findViewById(R.id.edtDescripcionHobbie);
        edtTiempoHobbie = findViewById(R.id.edtTiempoHobbie);
        //Eventos
        btnBuscarAlumno.setOnClickListener(this);
        btnActualizarDatos.setOnClickListener(this);
        btnEliminarAlumno.setOnClickListener(this);
        btnBorrarHobbie.setOnClickListener(this);
        siguiente.setOnClickListener(this);
        anterior.setOnClickListener(this);

        siguiente.setEnabled(false);
        anterior.setEnabled(false);
    }

    @Override
    public void onClick(View v) {

            switch (v.getId()){
                case R.id.btnBuscarAlumno:
                    BuscarAlumno();
                    break;
                case R.id.btnActualizarDatos:
                    updateDatos();
                    break;
                case R.id.btnEliminarAlumno:
                    DeleteAlumno();
                    break;
                case R.id.btnBorrarHobbie:
                    DeleteHobbie();
                    break;
                case R.id.siguiente:
                    if (fila==lista.length-1){
                      fila =-1;
                    }
                    fila++;
                    if (fila < lista.length || fila > 0){
                        edtDescripcionHobbie.setText(lista[fila][1]);
                        edtTiempoHobbie.setText(lista[fila][2]);
                    }
                    break;
                case R.id.anterior:

                    if (fila <= 0){
                        fila = lista.length;
                    }
                        fila--;
                    if (fila < lista.length || fila > 0){
                        edtDescripcionHobbie.setText(lista[fila][1]);
                        edtTiempoHobbie.setText(lista[fila][2]);
                    }
                    break;
            }
    }

    private void BuscarAlumno(){
        String[] numControl = {edtBusqueda.getText().toString()};
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
                cursor.close();
                //La funcion era muy grande asi hice otra
                ListarHobbies();
            }catch (Exception e){
                Toast.makeText(this,"Numero de control no registrado",Toast.LENGTH_SHORT).show();
            }
            finally {
                db.close();
                conn.close();
            }
        }else
            Toast.makeText(this,"Ingresa un numero de control",Toast.LENGTH_LONG).show();
    }

    private void ListarHobbies(){

        siguiente.setEnabled(true);
        anterior.setEnabled(true);

        edtDescripcionHobbie.setText("");
        edtTiempoHobbie.setText("");

        String[] numControl = {edtBusqueda.getText().toString()};
        if (!numControl[0].isEmpty()){
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getReadableDatabase();
            //campos solicitados
            String[] campos = {"DESCRIPCION","DEDICACION"};
            // solo se espera un resultado
            String Alumno[] = {Integer.toString(IdAlumno)};
            try {
                Cursor cursor = db.rawQuery("select IDHOBBIES ,DESCRIPCION, DEDICACION from HOBBIE WHERE IDALUMNO =  "+Alumno[0],null);
                tvCantidadHobbies.setText("Hobbies Alumno "+cursor.getCount());
                lista = new String[cursor.getCount()][cursor.getColumnCount()];
                if (cursor.getCount() <= 1){
                    cursor.moveToFirst();
                    lista[0][0]=cursor.getString(0);
                    lista[0][1]=cursor.getString(1);
                    lista[0][2]=cursor.getString(2);
                }
                else {
                    for (int filas =0 ; filas< cursor.getCount(); filas++ ) {
                        cursor.moveToNext();
                        for (int elemento = 0; elemento < cursor.getColumnCount(); elemento++) {
                            lista[filas][elemento]= cursor.getString(elemento);
                        }
                    }
                    cursor.close();
                    for (int filas = 0; filas<lista.length; filas++)
                        for (int col = 0; col<lista[filas].length; col++){
                            System.out.println(lista[filas][col]+"\n");
                        }
                }
                edtDescripcionHobbie.setText(lista[0][1]);
                edtTiempoHobbie.setText(lista[0][2]);
            }catch (Exception e){
                Toast.makeText(this,"Este Alumno no tiene hobbies registradas",Toast.LENGTH_LONG).show();
                siguiente.setEnabled(false);
                anterior.setEnabled(false);
            }
            finally {
                db.close();
                conn.close();
            }
        }else
            Toast.makeText(this,"Ingresa un numero de control",Toast.LENGTH_LONG).show();
    }

    private void updateDatos(){
        //Actualiza el Hobbie
        try {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = {lista[fila][0]};
            ContentValues values = new ContentValues();
            values.put("DESCRIPCION",edtDescripcionHobbie.getText().toString());
            values.put("DEDICACION",edtTiempoHobbie.getText().toString());
            db.update("HOBBIE", values,"IDHOBBIES = ?",parametros);
            conn.close();
            db.close();
            updateAlumnos();
            Toast.makeText(this,"Informacion Actualizada",Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Toast.makeText(this,"No has realizado cambios",Toast.LENGTH_LONG).show();
        }
    }

    private void updateAlumnos(){
        try {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = { Integer.toString(IdAlumno) };
            ContentValues values = new ContentValues();
            values.put("NOMBRE", edtNombre.getText().toString());
            values.put("TELEFONO", edtTelefono.getText().toString());

            db.update("ALUMNOS", values, "IDALUMNO = ?", parametros);
            conn.close();
            db.close();
            BuscarAlumno();
            ListarHobbies();
        }catch (Exception e){
        }
    }

    private void DeleteHobbie(){
        try {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(getApplicationContext());
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = {lista[fila][0]};

            AlertDialog.Builder Diaboton = new AlertDialog.Builder(ModificacionesDatos.this);
            Diaboton.setTitle("Estas seguro de eliminar esta hobbie");
            Diaboton.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.delete("HOBBIE", "IDHOBBIES = ?", parametros);
                    fila=0;
                    ListarHobbies();
                    conn.close();
                    db.close();
                    ListarHobbies();
                    siguiente.setEnabled(false);
                    anterior.setEnabled(false);
                }
            });
            Diaboton.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), "No se elimino Hobbie",Toast.LENGTH_SHORT).show();
                }
            });
            Diaboton.show();

        }catch (Exception e){
            Toast.makeText(this,"Error al eliminar probablemente no tiene hobbie",Toast.LENGTH_LONG).show();
        }
    }

    private void DeleteAlumno(){
        try {
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(getApplicationContext());
            SQLiteDatabase db = conn.getWritableDatabase();
            String[] parametros = {Integer.toString(IdAlumno)};

            AlertDialog.Builder Diaboton = new AlertDialog.Builder(ModificacionesDatos.this);
            Diaboton.setTitle("¿ Seguro que desea eliminar este alumno ?");
            Diaboton.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.delete("ALUMNOS", "IDALUMNO = ?", parametros);
                    fila=0;
                    BuscarAlumno();
                    conn.close();
                    db.close();
                    edtNumeroControl.setText("");
                    edtNombre.setText("");
                    edtSexo.setText("");
                    edtTelefono.setText("");
                    edtDescripcionHobbie.setText("");
                    edtTiempoHobbie.setText("");
                    Toast.makeText(getApplicationContext(), "Alumno Eliminado",Toast.LENGTH_SHORT).show();
                    edtBusqueda.setText("");
                    ListarHobbies();
                    siguiente.setEnabled(false);
                    anterior.setEnabled(false);
                }
            });
            Diaboton.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), "No se elimino Alumno",Toast.LENGTH_SHORT).show();
                }
            });
            Diaboton.show();

        }catch (Exception e){
            Toast.makeText(this,"Error al tratar de eliminar Alumnos",Toast.LENGTH_LONG).show();
        }
    }
}