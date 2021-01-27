package com.example.crudeloygarciaceja;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLiteOpenHelper extends SQLiteOpenHelper {
    //nombre base de datos y sus tablas
    final static String DB_NAME = "HOBBIES.db";
    final static String ALUMNOS = "ALUMNOS";
    final static  String HOBBIE = "HOBBIE";
    final static int VERSION = 1;
    public ConexionSQLiteOpenHelper(@Nullable Context context) {

        super(context, DB_NAME, null, VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase Hobbies) {
        //para habilitar el borrado en cascada
        Hobbies.execSQL(String.format("PRAGMA foreign_keys = ON"));
        //Creacion de tablas con escape de inyeccion SQL
        Hobbies.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(IDALUMNO integer primary key autoincrement, NUMCONTROL text, NOMBRE text, SEXO text, TELEFONO TEXT)", ALUMNOS));
        Hobbies.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(IDHOBBIES integer primary key autoincrement, IDALUMNO integer, DESCRIPCION text, DEDICACION text, FOREIGN KEY (IDALUMNO) REFERENCES ALUMNOS (IDALUMNO) ON DELETE CASCADE)", HOBBIE));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
