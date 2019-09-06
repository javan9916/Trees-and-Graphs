package com.example.estudiantes.graphsandtrees.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.activities.MainActivity;
import com.example.estudiantes.graphsandtrees.activities.RegisterActivity;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.classes.Register;
import com.example.estudiantes.graphsandtrees.classes.Router;

import java.util.ArrayList;

public class RegisterListAdapter extends ArrayAdapter<Register> {

    Metodos met = Metodos.getInstance();
    Router router = MainActivity.currentRouter;
    public static Register currentRegister;

    private Context mContext;
    int mResource;

    private Button buttonDelete;
    private ListView lv = RegisterActivity.listViewRegisters;
    private ArrayList<Register> list = RegisterActivity.registerList;

    public RegisterListAdapter(Context context, int resource, ArrayList<Register> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;

    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Obtiene la información del registro
        int id = getItem(position).id;
        String fecha = getItem(position).fecha;
        String tipoRevision = getItem(position).tipoRevision;
        String estado = getItem(position).estado;


        // Crea el registro con la información dada
        final Register register = new Register(id, fecha, tipoRevision, estado);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView textViewId = convertView.findViewById(R.id.tv_adapter_id);
        TextView textViewFecha = convertView.findViewById(R.id.tv_adapter_fecha);
        TextView textViewTipoRev = convertView.findViewById(R.id.tv_adapter_tipoRev);
        TextView textViewEstado = convertView.findViewById(R.id.tv_adapter_estado);
        buttonDelete = convertView.findViewById(R.id.adapterButtonDelete);

        textViewId.setText(String.valueOf(id));
        textViewFecha.setText(fecha);
        textViewTipoRev.setText(tipoRevision);
        textViewEstado.setText(estado);

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = getId(position);
                boolean eliminado = met.eliminarRegister(router.raiz, id, router.raiz, router);
                if (eliminado) {
                    Toast.makeText(mContext, "Eliminado correctamente", Toast.LENGTH_SHORT).show();
                    list.remove(position);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "Hubo un problema al eliminar el registro", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            }
        });

        return convertView;
    }

    public int getId(int position) {
        Register item = (Register) lv.getItemAtPosition(position);
        return item.id;
    }



}
