package com.example.estudiantes.graphsandtrees.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.adapters.RegisterListAdapter;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Register;
import com.example.estudiantes.graphsandtrees.classes.Router;
import com.example.estudiantes.graphsandtrees.dialogs.editRegisterDialog;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    Metodos met = Metodos.getInstance();

    private TextView textViewNombre;
    private TextView textViewDescripcion;
    private TextView textViewGradoI;
    private TextView textViewGradoE;
    private TextView textViewTipoGrafo;

    private Button buttonInorden;
    private Button buttonPreorden;

    private int id;
    final Router router = MainActivity.currentRouter;
    Register currentRegister = RegisterListAdapter.currentRegister;
    private String orden;

    public static ArrayList<Register> registerList;
    public static RegisterListAdapter RegistersAdapter;
    public static ListView listViewRegisters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        textViewNombre = findViewById(R.id.textViewTitle);
        textViewDescripcion = findViewById(R.id.textViewSnippet);
        textViewGradoI = findViewById(R.id.textViewGradoI);
        textViewGradoE = findViewById(R.id.textViewGradoE);
        listViewRegisters = findViewById(R.id.register_list);
        buttonInorden = findViewById(R.id.buttonInorden);
        buttonPreorden = findViewById(R.id.buttonPreorden);
        textViewTipoGrafo = findViewById(R.id.textViewTipoGrafo);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String nombre = bundle.getString("nombre");
            String descripcion = bundle.getString("descripcion");
            String gradoI = bundle.getString("gradoI");
            String gradoE = bundle.getString("gradoE");
            String valor = bundle.getString("valor");

            textViewNombre.setText("Nombre: "+nombre);
            textViewDescripcion.setText("Descripción: "+descripcion);
            textViewGradoI.setText(gradoI);
            textViewGradoE.setText(gradoE);
            if (valor.equals("completo")) {
                textViewTipoGrafo.setText("Completo");
            } else if (valor.equals("conexo")) {
                textViewTipoGrafo.setText("Fuertemente Conexo");
            } else if (valor.equals("noconexo")) {
                textViewTipoGrafo.setText("No Conexo");
            }
        }

        inorden();
        orden = "inorden";

        buttonInorden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inorden();
                orden = "inorden";
            }
        });

        buttonPreorden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preorden();
                orden = "preorden";
            }
        });

    }

    public void inorden() {
        registerList = met.registersOrden(router, "inorden");

        RegistersAdapter = new RegisterListAdapter
                (RegisterActivity.this, R.layout.registers_adapter_view_layout, registerList);
        listViewRegisters.setAdapter(RegistersAdapter);
    }

    public void preorden() {
        registerList = met.registersOrden(router, "preorden");

        RegistersAdapter = new RegisterListAdapter
                (RegisterActivity.this, R.layout.registers_adapter_view_layout, registerList);
        listViewRegisters.setAdapter(RegistersAdapter);
    }


}
