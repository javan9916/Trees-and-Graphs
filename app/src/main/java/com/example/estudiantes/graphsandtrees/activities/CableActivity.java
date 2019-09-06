package com.example.estudiantes.graphsandtrees.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.adapters.CableListAdapter;
import com.example.estudiantes.graphsandtrees.adapters.GraphListAdapter;
import com.example.estudiantes.graphsandtrees.classes.Cable;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Router;

import java.util.ArrayList;

public class CableActivity extends AppCompatActivity {

    Metodos met = Metodos.getInstance();

    private TextView textViewDistancia;
    private TextView textViewVelocidad;
    private TextView textViewTipo;
    private TextView textViewOrigen;
    private TextView textViewDestino;

    public static String nombre;

    public static ArrayList<Cable> CableList;
    public static CableListAdapter CablesAdapter;
    public static ListView listViewCables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cable);

        Bundle info = getIntent().getExtras();
        nombre = info.getString("nombre");

        textViewDistancia = findViewById(R.id.tv_adapter_distancia);
        textViewVelocidad = findViewById(R.id.tv_adapter_velocidad);
        textViewTipo = findViewById(R.id.tv_adapter_tipo);
        textViewOrigen = findViewById(R.id.tv_adapter_origen);
        textViewDestino = findViewById(R.id.tv_adapter_destino);
        listViewCables = findViewById(R.id.listViewCables);

        CableList = met.recorrerRouter(nombre);

        CablesAdapter = new CableListAdapter(CableActivity.this, R.layout.cables_adapter_view_layout, CableList);
        listViewCables.setAdapter(CablesAdapter);
    }
}
