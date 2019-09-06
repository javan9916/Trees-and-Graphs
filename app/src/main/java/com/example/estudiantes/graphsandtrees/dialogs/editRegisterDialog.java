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
import android.widget.Spinner;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.activities.RegisterActivity;
import com.example.estudiantes.graphsandtrees.adapters.RegisterListAdapter;
import com.example.estudiantes.graphsandtrees.classes.Register;

public class editRegisterDialog extends AppCompatDialogFragment {

    private EditText editTextID;
    private EditText editTextFecha;
    private EditText editTextTipo;
    private Spinner spinnerEstado;
    private editRegisterDialog.EditRegisterDialogListener editRegisterListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_register_layout, null);
        Register register = RegisterListAdapter.currentRegister;


        builder.setView(view)
                .setTitle("Registro Nuevo")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int ID = ParseInt(editTextID.getText().toString());
                        final String fecha = editTextFecha.getText().toString();
                        final String tipo = editTextTipo.getText().toString();
                        final String estado = spinnerEstado.getSelectedItem().toString();
                        editRegisterListener.applyTexts(ID, fecha, tipo, estado);
                    }
                });
        editTextID = view.findViewById(R.id.editTextId);
        editTextFecha = view.findViewById(R.id.editTextFecha);
        editTextTipo = view.findViewById(R.id.editTextTipo);
        spinnerEstado = view.findViewById(R.id.spinnerEstado);
        editTextID.setText(register.id);
        editTextFecha.setText(register.fecha);
        editTextTipo.setText(register.tipoRevision);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            editRegisterListener = (EditRegisterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar CableDialog Listener");
        }
    }

    public interface EditRegisterDialogListener {
        void applyTexts(int id, String fecha, String tipo, String estado);
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
