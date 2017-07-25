package com.example.alumno.ejemplobasededatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Asignatura> asignaturas;
    private ArrayAdapter<Asignatura> adapter;
    private AdminSQLiteOpenHelper admin;
    private EditText etCodigoAsignatura, etAsignatura, etCantidadEstudiantes;
    private ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCodigoAsignatura = (EditText) findViewById(R.id.etCodigo);
        etAsignatura = (EditText) findViewById(R.id.etAsignatura);
        etCantidadEstudiantes = (EditText) findViewById(R.id.etCantidadEstudiantes);
        lv1=(ListView) findViewById(R.id.lv1);
        asignaturas = new ArrayList<>();
        adapter = new ArrayAdapter<Asignatura>(this, android.R.layout.simple_expandable_list_item_1, asignaturas);
        lv1.setAdapter(adapter);
        admin = new AdminSQLiteOpenHelper(this, "administracion", null, 1);
        showAll();
    }

    public void agregar(View view) {
        SQLiteDatabase bd = admin.getWritableDatabase();
        int codigo = Integer.parseInt(etCodigoAsignatura.getText().toString());
        int cantidadEstudiantes = Integer.parseInt(etCantidadEstudiantes.getText().toString());
        String nombre = etAsignatura.getText().toString();
        ContentValues registro = new ContentValues();
        registro.put("codigo", codigo);
        registro.put("nombre", nombre);
        registro.put("cantidadEstudiantes", cantidadEstudiantes);
        bd.insert("asignatura", null, registro);
        bd.close();
        Toast.makeText(this, "Asignatura ingresada", Toast.LENGTH_LONG).show();
        showAll();
    }

    public void del(View view) {
        SQLiteDatabase bd= admin.getReadableDatabase();
        String codigo=etCodigoAsignatura.getText().toString();
        int cant=bd.delete("asignatura","codigo = " + codigo, null);
        bd.close();
        if(cant==1){
            Toast.makeText(this, "Asignatura eliminada", Toast.LENGTH_SHORT).show();

        }else{
            Toast.makeText(this, "No existe la asignatura con el codigo "+codigo, Toast.LENGTH_SHORT).show();
        }
        showAll();

    }

    public void showAll() {
        String query = "select * from asignatura";
        SQLiteDatabase bd = admin.getReadableDatabase();
        asignaturas.clear();
        Cursor c = bd.rawQuery(query, null);
        while(c.moveToNext()){
            Asignatura a= new Asignatura();
            a.setCodigo(c.getInt(c.getColumnIndex("codigo")));
            a.setNombre(c.getString(c.getColumnIndex("nombre")));
            a.setCantidadEstudiantes(c.getInt(c.getColumnIndex("cantidadEstudiantes")));
            asignaturas.add(a);
        }
        adapter.notifyDataSetChanged();
    }
}
