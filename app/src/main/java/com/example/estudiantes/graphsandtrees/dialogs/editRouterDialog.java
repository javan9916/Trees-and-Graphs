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
import com.example.estudiantes.graphsandtrees.classes.Router;

public class editRouterDialog extends AppCompatDialogFragment {

    private EditText editTextNombre;
    private EditText editTextDescripcion;
    EditRouterDialogListener editRouterListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_router_layout, null);
        Router router = MainActivity.currentRouter;

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
                        editRouterListener.applyTexts(nombre, descripcion, true);

                    }
                });
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextDescripcion = view.findViewById(R.id.editTextDescripcion);
        editTextNombre.setText(router.nombre);
        editTextDescripcion.setText(router.descripcion);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            editRouterListener = (EditRouterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar EditRouterDialog Listener");
        }
    }

    public interface EditRouterDialogListener {
        void applyTexts(String nombre, String descripcion, boolean edit);
    }
}
