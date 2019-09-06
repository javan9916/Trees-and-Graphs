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

public class dijkstraDialog extends AppCompatDialogFragment {

    private EditText editTextOrigen;
    private EditText editTextDestino;
    private dijkstraDialog.DijkstraDialogListener dijkstraListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dijkstra_layout, null);

        builder.setView(view)
                .setTitle("Dijkstra")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String origen = editTextOrigen.getText().toString();
                        final String destino = editTextDestino.getText().toString();
                        final int num = 1;
                        dijkstraListener.applyTexts(origen, destino, num);

                    }
                });
        editTextOrigen = view.findViewById(R.id.editTextOrigen);
        editTextDestino = view.findViewById(R.id.editTextDestino);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            dijkstraListener = (DijkstraDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + "Se tiene que implementar DijkstraDialog Listener");
        }
    }

    public interface DijkstraDialogListener {
        void applyTexts(String origen, String destino, int num);
    }
}
