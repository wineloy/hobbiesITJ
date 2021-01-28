package com.example.crudeloygarciaceja;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReportesDatos extends AppCompatActivity implements View.OnClickListener {

    private EditText edtBusqueda;
    private Button btnBuscarAlumno;

    //Formulario Alumnos
    private EditText edtNumeroControl, edtNombre, edtSexo, edtTelefono;
    //Datos Tabla
    private TextView descripcionuno, dedicacionuno, DescripcionDos, dedicacionDos, DescripcionTres, dedicacionTres, DescripcionCuatro, dedicacionCuatro, ListaAlumnos;

    //botones adelante atras
    private ImageView anterior,siguiente;

    //Lista de alumnos y hobbies
    private String IdAlumno;
    private String[][] listAlumnos;
    private String[][] listHobbie;
    private int fila = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportes_datos);


       //Botones de accion
        edtBusqueda = findViewById(R.id.edtBusqueda);
        btnBuscarAlumno = findViewById(R.id.btnBuscarAlumno);
        //formulario
        edtNumeroControl = findViewById(R.id.edtNumeroControl);
        edtNombre = findViewById(R.id.edtNombre);
        edtSexo = findViewById(R.id.edtSexo);
        edtTelefono = findViewById(R.id.edtTelefono);
        //Tabla fila 1
        descripcionuno = findViewById(R.id.descripcionuno);
        dedicacionuno = findViewById(R.id.dedicacionuno);
        //fila 2
        DescripcionDos = findViewById(R.id.DescripcionDos);
        dedicacionDos = findViewById(R.id.dedicacionDos);
        //fila 3
        DescripcionTres = findViewById(R.id.DescripcionTres);
        dedicacionTres = findViewById(R.id.dedicacionTres);
        //fila 4
        DescripcionCuatro = findViewById(R.id.DescripcionCuatro);
        dedicacionCuatro = findViewById(R.id.dedicacionCuatro);
        // botones adelante atras
        anterior = findViewById(R.id.anterior);
        siguiente = findViewById(R.id.siguiente);
        //Asignacion de eventos
        btnBuscarAlumno.setOnClickListener(this);
        anterior.setOnClickListener(this);
        siguiente.setOnClickListener(this);

        ListaAlumnos = findViewById(R.id.ListaAlumnos);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuscarAlumno:
                BuscarAlumno();
                break;
            case R.id.anterior:
                if (fila <= 0){
                    fila = listAlumnos.length;
                }
                fila--;
                if (fila < listAlumnos.length || fila > 0){
                    edtNumeroControl.setText(listAlumnos[fila][1]);
                    edtNombre.setText(listAlumnos[fila][2]);
                    edtSexo.setText(listAlumnos[fila][3]);
                    edtTelefono.setText(listAlumnos[fila][4]);
                    MostrarHobbie();
                }
                break;
            case R.id.siguiente:
                if (fila==listAlumnos.length-1){
                    fila =-1;
                }
                fila++;
                if (fila < listAlumnos.length || fila > 0){
                    edtNumeroControl.setText(listAlumnos[fila][1]);
                    edtNombre.setText(listAlumnos[fila][2]);
                    edtSexo.setText(listAlumnos[fila][3]);
                    edtTelefono.setText(listAlumnos[fila][4]);
                    MostrarHobbie();
                }
                break;

        }
    }

    private void BuscarAlumno(){
        String nombre = "%"+edtBusqueda.getText().toString()+"%";
        String[] numControl = {edtBusqueda.getText().toString(), nombre};
        if (!numControl[0].isEmpty()){
            ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
            SQLiteDatabase db = conn.getReadableDatabase();
            //campos solicitados
            String[] campos = {"IDALUMNO","NUMCONTROL", "NOMBRE", "SEXO", "TELEFONO"};
            // solo se espera un resultado
            try {
                Cursor cursor = db.query("ALUMNOS",campos,"NUMCONTROL = ? OR NOMBRE LIKE ?",numControl,null,null,null );
                listAlumnos = new String[cursor.getCount()][cursor.getColumnCount()];
                if (cursor.getCount() <= 1){
                    cursor.moveToFirst();
                    listAlumnos[0][0]= cursor.getString(0);
                    listAlumnos[0][1]= cursor.getString(1);
                    listAlumnos[0][2]= cursor.getString(2);
                    listAlumnos[0][3]= cursor.getString(3);
                    listAlumnos[0][4]= cursor.getString(4);
                    //Muestra los datos del arreglo
                }else{
                    for (int filas =0 ; filas< cursor.getCount(); filas++ ) {
                        cursor.moveToNext();
                        for (int elemento = 0; elemento < cursor.getColumnCount(); elemento++) {
                            listAlumnos[filas][elemento]= cursor.getString(elemento);
                        }
                    }
                }
                ListaAlumnos.setText("Datos del Alumno ( se encontraron  "+cursor.getCount()+ " )");

                edtNumeroControl.setText(listAlumnos[0][1]);
                edtNombre.setText(listAlumnos[0][2]);
                edtSexo.setText(listAlumnos[0][3]);
                edtTelefono.setText(listAlumnos[0][4]);
                cursor.close();
               MostrarHobbie();

            }catch (Exception e){
                Toast.makeText(this,"Numero de control no registrado",Toast.LENGTH_SHORT).show();
            }
            finally {
                db.close();
                conn.close();
            }
        }else
            Toast.makeText(this,"Ingresa un numero de control",Toast.LENGTH_SHORT).show();
    }

    private void MostrarHobbie(){
        ConexionSQLiteOpenHelper conn = new ConexionSQLiteOpenHelper(this);
        SQLiteDatabase db = conn.getReadableDatabase();
        String[] campos = {"DESCRIPCION","DEDICACION"};
        String[] Alumno = {listAlumnos[fila][0]};
        Cursor cursor = db.query("HOBBIE",campos,"IDALUMNO = ?",Alumno,null,null,null );
        listHobbie = new String[cursor.getCount()][cursor.getColumnCount()];

         switch (cursor.getCount()){
             case 1:
                 cursor.moveToFirst();
                 descripcionuno.setText( cursor.getString(0));
                 dedicacionuno.setText( cursor.getString(1));
                 break;
             case 2:
                 cursor.moveToNext();
                 descripcionuno.setText( cursor.getString(0));
                 dedicacionuno.setText( cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionDos.setText( cursor.getString(0));
                 dedicacionDos.setText( cursor.getString(1));
                 break;
             case 3:
                 cursor.moveToNext();
                 descripcionuno.setText( cursor.getString(0));
                 dedicacionuno.setText( cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionDos.setText( cursor.getString(0));
                 dedicacionDos.setText( cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionTres.setText(cursor.getString(0));
                 dedicacionTres.setText(cursor.getString(1));
                 break;
             case 4:
                 cursor.moveToNext();
                 descripcionuno.setText( cursor.getString(0));
                 dedicacionuno.setText( cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionDos.setText( cursor.getString(0));
                 dedicacionDos.setText( cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionTres.setText(cursor.getString(0));
                 dedicacionTres.setText(cursor.getString(1));
                 cursor.moveToNext();
                 DescripcionCuatro.setText(cursor.getString(0));
                 dedicacionCuatro.setText(cursor.getString(1));
                 break;
             default:
                 clear();
                 Toast.makeText(this,"Este Alumno no tiene hobbies",Toast.LENGTH_SHORT).show();
                 break;

        }
        cursor.close();
         db.close();
         conn.close();

    }

    private void clear(){
        descripcionuno.setText( "");
        dedicacionuno.setText( "");
        DescripcionDos.setText( "");
        dedicacionDos.setText( "");
        DescripcionTres.setText("");
        dedicacionTres.setText("");
        DescripcionCuatro.setText("");
        dedicacionCuatro.setText("");

    }
}
