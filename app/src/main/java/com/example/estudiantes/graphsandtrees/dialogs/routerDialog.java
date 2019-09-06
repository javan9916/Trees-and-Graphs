package com.example.estudiantes.graphsandtrees.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.estudiantes.graphsandtrees.R;

public class routerDialog extends AppCompatDialogFragment {

    private EditText editTextNombre;
    private EditText editTextDescripcion;
    private RouterDialogListener routerListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_router_layout, null);

        builder.setView(view)
                .setTitle("Router Nuevo")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String nombre = editTextNombre.getText().toString();
                        final String descripcion = editTextDescripcion.getText().toString();
                        routerListener.applyTexts(nombre, descripcion);
                    }
                });
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            routerListener = (RouterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar RouterDialog Listener");
        }
    }

    public interface RouterDialogListener {
        void applyTexts(String nombre, String descripcion);
    }
}
