package com.example.estudiantes.graphsandtrees.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.classes.Cable;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Router;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class GraphListAdapter extends ArrayAdapter<Router> {

    Metodos met = Metodos.getInstance();

    public static Router router;
    private Context mContext;
    int mResource;

    public static CableListAdapter CablesAdapter;


    public GraphListAdapter(Context context, int resource, ArrayList<Router> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        String nombre = getItem(position).nombre;
        String descripcion = getItem(position).descripcion;
        LatLng ubicacion = getItem(position).ubicacion;
        ArrayList<Cable> cables = met.recorrerRouter(nombre);
        router = met.buscarNombre(nombre);

        final Router router = new Router(nombre, ubicacion, descripcion);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewNombre = convertView.findViewById(R.id.tv_adapter_nombre);
        TextView textViewDescripcion = convertView.findViewById(R.id.tv_adapter_descripcion);
        TextView textViewUbicacion = convertView.findViewById(R.id.tv_adapter_ubicacion);

        textViewNombre.setText(nombre);
        textViewDescripcion.setText(descripcion);
        textViewUbicacion.setText(String.valueOf(ubicacion));

        return convertView;
    }
}
