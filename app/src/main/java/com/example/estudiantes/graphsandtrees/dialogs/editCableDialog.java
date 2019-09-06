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
import com.example.estudiantes.graphsandtrees.activities.MainActivity;
import com.example.estudiantes.graphsandtrees.classes.Cable;

public class editCableDialog extends AppCompatDialogFragment {

    private EditText editTextDistancia;
    private EditText editTextVelocidad;
    private EditText editTextTipo;
    private EditCableDialogListener editCableListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_cable_layout, null);
        Cable cable = MainActivity.currentCable;

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
                        editCableListener.applyTexts(distancia, velocidad, tipo, true);

                    }
                });
        editTextDistancia = view.findViewById(R.id.editTextDistancia);
        editTextVelocidad = view.findViewById(R.id.editTextVelocidadT);
        editTextTipo = view.findViewById(R.id.editTextTipo);
        editTextDistancia.setText(String.valueOf(cable.distancia));
        editTextVelocidad.setText(String.valueOf(cable.velocidadTransferencia));
        editTextTipo.setText(cable.tipo);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            editCableListener = (EditCableDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar CableDialog Listener");
        }
    }

    public interface EditCableDialogListener {
        void applyTexts(int distancia, int velocidad, String tipo, boolean edit);
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
