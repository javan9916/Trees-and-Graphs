package com.example.estudiantes.graphsandtrees.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.classes.Register;

import java.util.ArrayList;

public class SearchRegisterAdapter extends ArrayAdapter<Register> {

    private Context mContext;
    int mResource;

    public SearchRegisterAdapter(Context context, int resource, ArrayList<Register> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int id = getItem(position).id;
        String fecha = getItem(position).fecha;
        String tipoRevision = getItem(position).tipoRevision;
        String estado = getItem(position).estado;

        final Register register = new Register(id, fecha, tipoRevision, estado);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewId = convertView.findViewById(R.id.tv_search_adapter_id);
        TextView textViewFecha = convertView.findViewById(R.id.tv_search_adapter_fecha);
        TextView textViewTipoRev = convertView.findViewById(R.id.tv_search_adapter_tipoRev);
        TextView textViewEstado = convertView.findViewById(R.id.tv_search_adapter_estado);

        textViewId.setText(String.valueOf(id));
        textViewFecha.setText(fecha);
        textViewTipoRev.setText(tipoRevision);
        textViewEstado.setText(estado);

        return convertView;
    }
}
