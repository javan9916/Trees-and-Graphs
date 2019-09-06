package com.example.estudiantes.graphsandtrees.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.adapters.RegisterListAdapter;
import com.example.estudiantes.graphsandtrees.adapters.SearchRegisterAdapter;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Register;
import com.example.estudiantes.graphsandtrees.classes.Router;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    Metodos met = Metodos.getInstance();

    private EditText editTextSearch;
    private Button buttonSearch;
    private Spinner spinnerFilter;

    public static ArrayList<Register> registerList;
    public static SearchRegisterAdapter RegistersAdapter;
    public static ListView listViewRegisters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        final Router router = MainActivity.currentRouter;

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        listViewRegisters = findViewById(R.id.listViewRegisters);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String key = spinnerFilter.getSelectedItem().toString();
                final String search = editTextSearch.getText().toString();

                if (!search.equals("")) {
                    registerList = met.buscarRegistros(key, router, search);

                    RegistersAdapter = new SearchRegisterAdapter
                            (SearchActivity.this, R.layout.search_adapter_view_layout, registerList);

                    listViewRegisters.setAdapter(RegistersAdapter);
                }
            }
        });
    }
}
