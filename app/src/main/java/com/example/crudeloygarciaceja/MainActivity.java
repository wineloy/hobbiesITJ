package com.example.crudeloygarciaceja;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button btnActividadAlumno, btnActividadHobbie, btnActividadModificaciones, btnActividadReportes;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_home);
        //Mapeo botones
        btnActividadAlumno  = findViewById(R.id.btnActividadAlumno);
        btnActividadHobbie = findViewById(R.id.btnActividadHobbie);
        btnActividadModificaciones = findViewById(R.id.btnActividadModificaciones);
        btnActividadReportes = findViewById(R.id.btnActividadReportes);

        btnActividadAlumno.setOnClickListener(this);
        btnActividadHobbie.setOnClickListener(this);
        btnActividadModificaciones.setOnClickListener(this);
        btnActividadReportes.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnActividadAlumno:

                Intent intent = new Intent (v.getContext(), RegistrarAlumnos.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.btnActividadHobbie:
                Intent intentHobbies = new Intent (v.getContext(), RegistrarHobbies.class);
                startActivityForResult(intentHobbies, 0);
                break;

            case R.id.btnActividadModificaciones:
                Intent intentModificaciones = new Intent (v.getContext(), ModificacionesDatos.class);
                startActivityForResult(intentModificaciones, 0);
                break;

            case R.id.btnActividadReportes:
                Intent intentReportes = new Intent (v.getContext(), ReportesDatos.class);
                startActivityForResult(intentReportes, 0);
                break;
        }
    }


}