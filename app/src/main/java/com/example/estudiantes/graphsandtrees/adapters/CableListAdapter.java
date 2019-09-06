package com.example.estudiantes.graphsandtrees.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.activities.CableActivity;
import com.example.estudiantes.graphsandtrees.classes.Cable;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Router;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class CableListAdapter extends ArrayAdapter<Cable> {

    Metodos met = Metodos.getInstance();

    private Context mContext;
    int mResource;

    public CableListAdapter(Context context, int resource, ArrayList<Cable> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        int dist = getItem(position).distancia;
        int vel = getItem(position).velocidadTransferencia;
        String tipo = getItem(position).tipo;
        Router destino = getItem(position).destino;
        Router origen = getItem(position).origen;
        String distancia = String.valueOf(dist);
        String velocidad = String.valueOf(vel);

        final Cable cable = new Cable(origen, destino);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewDistancia = convertView.findViewById(R.id.tv_adapter_distancia);
        TextView textViewVelocidad = convertView.findViewById(R.id.tv_adapter_velocidad);
        TextView textViewTipo = convertView.findViewById(R.id.tv_adapter_tipo);
        TextView textViewOrigen = convertView.findViewById(R.id.tv_adapter_origen);
        TextView textViewDestino = convertView.findViewById(R.id.tv_adapter_destino);

        textViewDistancia.setText(distancia);
        textViewVelocidad.setText(velocidad);
        textViewTipo.setText(tipo);
        textViewOrigen.setText(CableActivity.nombre);
        textViewDestino.setText(destino.nombre);

        return convertView;
}
}
