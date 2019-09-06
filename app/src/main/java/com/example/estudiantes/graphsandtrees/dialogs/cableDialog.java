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

public class cableDialog extends AppCompatDialogFragment {

    private EditText editTextDistancia;
    private EditText editTextVelocidad;
    private EditText editTextTipo;
    private cableDialog.CableDialogListener cableListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_cable_layout, null);

        builder.setView(view)
                .setTitle("Cable Nuevo")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int distancia = ParseInt(editTextDistancia.getText().toString());
                        final int velocidad = ParseInt(editTextVelocidad.getText().toString());
                        final String tipo = editTextTipo.getText().toString();
                        cableListener.applyTexts(distancia, velocidad, tipo);
                    }
                });
            editTextDistancia = view.findViewById(R.id.editTextDistancia);
            editTextVelocidad = view.findViewById(R.id.editTextVelocidadT);
            editTextTipo = view.findViewById(R.id.editTextTipo);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            cableListener = (CableDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar CableDialog Listener");
        }
    }

    public interface CableDialogListener {
        void applyTexts(int distancia, int velocidad, String tipo);
    }

    int ParseInt(String strNumber) {
        if ((strNumber != null) && (strNumber.length() > 0)) {
            try {
                return Integer.parseInt(strNumber);
            } catch(Exception e) {
                return -1;
            }
        }
        else return -1;
    }
}
