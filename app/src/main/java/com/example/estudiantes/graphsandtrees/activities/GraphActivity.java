package com.example.estudiantes.graphsandtrees.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.adapters.GraphListAdapter;
import com.example.estudiantes.graphsandtrees.adapters.RegisterListAdapter;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Register;
import com.example.estudiantes.graphsandtrees.classes.Router;

import java.util.ArrayList;

import static com.example.estudiantes.graphsandtrees.activities.RegisterActivity.registerList;

public class GraphActivity extends AppCompatActivity {

    Metodos met = Metodos.getInstance();

    private TextView textViewNombre;
    private TextView textViewDescripcion;

    public static ArrayList<Router> routerList;
    public static GraphListAdapter RoutersAdapter;
    public static ListView listViewRouters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        textViewNombre = findViewById(R.id.tv_adapter_nombre);
        textViewDescripcion = findViewById(R.id.tv_adapter_descripcion);
        listViewRouters = findViewById(R.id.listViewRouters);

        routerList = met.recorrerGraph();

        RoutersAdapter = new GraphListAdapter(GraphActivity.this, R.layout.routers_adapter_view_layout, routerList);
        listViewRouters.setAdapter(RoutersAdapter);

        listViewRouters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String nombre = getNombre(position);
                Intent intent = new Intent(GraphActivity.this, CableActivity.class);
                intent.putExtra("nombre", nombre);
                startActivity(intent);
            }
        });
    }

    public String getNombre(int position) {
        Router item = (Router) listViewRouters.getItemAtPosition(position);
        return item.nombre;
    }
}
