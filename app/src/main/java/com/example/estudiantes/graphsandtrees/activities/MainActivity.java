package com.example.estudiantes.graphsandtrees.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.estudiantes.graphsandtrees.classes.Cable;
import com.example.estudiantes.graphsandtrees.classes.Metodos;
import com.example.estudiantes.graphsandtrees.R;
import com.example.estudiantes.graphsandtrees.classes.Router;
import com.example.estudiantes.graphsandtrees.dialogs.cableDialog;
import com.example.estudiantes.graphsandtrees.dialogs.dijkstraDialog;
import com.example.estudiantes.graphsandtrees.dialogs.editCableDialog;
import com.example.estudiantes.graphsandtrees.dialogs.editRouterDialog;
import com.example.estudiantes.graphsandtrees.dialogs.registerDialog;
import com.example.estudiantes.graphsandtrees.dialogs.routerDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity implements
        OnMapReadyCallback,
        routerDialog.RouterDialogListener,
        cableDialog.CableDialogListener,
        registerDialog.RegisterDialogListener,
        editRouterDialog.EditRouterDialogListener,
        editCableDialog.EditCableDialogListener,
        dijkstraDialog.DijkstraDialogListener,
        GoogleMap.OnPolylineClickListener {

    Metodos met = Metodos.getInstance();

    private GoogleMap mMap;
    private LatLng currentUbicacion;
    public static Router currentRouter;
    public static Cable currentCable;
    private Router currentDest;
    private Marker currentMarker;
    private Polyline currentPolyline;
    private String currentNombre;
    private String currentDescripcion;
    private int currentDistancia;
    private int currentVelocidad;
    private String currentTipo;
    private int currentId;
    private String currentFecha;
    private String currentTipoRevision;
    private String currentEstado;
    private Button buttonRoutes;
    private String menuContextType;
    private boolean selected = false;
    private boolean onDijkstra = false;
    private ArrayList<Polyline> polylines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        registerForContextMenu(findViewById(R.id.map));
        buttonRoutes = findViewById(R.id.buttonRoutes);
    }

    // Configuraciones y eventos del mapa
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnPolylineClickListener(this);
        mMap.setMaxZoomPreference(20);

        buttonRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuContextType = "Rutas";
                openContextMenu(findViewById(R.id.map));
            }
        });


        // Ajusta la posición de la cámara en Costa Rica
        LatLng cr = new LatLng(9.951896844238645, -84.10552504999998);
        //mMap.addMarker(new MarkerOptions().position(cr).title("Marker in Costa Rica"));
        CameraPosition camera = new CameraPosition.Builder()
                .target(cr)
                .zoom(16)           // limit -> 21
                .bearing(0)         // 0 - 365
                .tilt(10)           // limit -> 90
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

        /**
         * Evento del mapa cuando es pulsado
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (selected) {
                    currentPolyline.setColor(Color.GRAY);
                    selected = false;
                } else if (onDijkstra) {
                    for (Polyline p : polylines) {
                        p.setColor(Color.GRAY);
                    }
                    polylines = new ArrayList<>();
                    onDijkstra = false;
                }
                else {
                    currentUbicacion = latLng;
                    currentRouter = met.buscarUbicacion(latLng);
                    openRouterDialog();
                }
            }
        });

        /**
         * Evento de los marker cuando son pulsados
         */
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                currentRouter = met.buscarArea(latLng);
                if (currentRouter != null) {
                    menuContextType = "OpcionesRouter";
                    openContextMenu(findViewById(R.id.map));
                } else if (currentPolyline != null && currentPolyline.getColor() == Color.BLACK) {
                    menuContextType = "OpcionesCable";
                    openContextMenu(findViewById(R.id.map));
                } else {
                    menuContextType = "OpcionesGrafo";
                    openContextMenu(findViewById(R.id.map));
                }
            }
        });

        /**
         * Evento de los marker cuando son pulsados
         */
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Retrieve the data from the marker.
                Integer clickCount = (Integer) marker.getTag();
                currentRouter = met.buscarUbicacion(marker.getPosition());
                currentMarker = marker;

                // Check if a click count was set, then display the click count.
                if (!marker.isDraggable() && (clickCount <= 2)) {

                    clickCount++;
                    marker.setDraggable(true);
                    Toast.makeText(MainActivity.this, "Draggable", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            currentMarker.setDraggable(false);
                            currentMarker.setTag(0);
                        }
                    }, 2000);
                }

                return false;
            }
        });

        /**
         * Evento del arrastre de los marker
         */
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                currentDest = met.buscarArea(marker.getPosition());
                if (currentDest != null && currentRouter != currentDest) {
                    Cable cable = met.buscarCable(currentRouter, currentDest);
                    if (cable == null) {
                        openCableDialog();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Alerta").setMessage("El cable no se puede repetir").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                }
                marker.setPosition(currentRouter.ubicacion);
            }
        });
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        try {
            if (!selected) {
                currentPolyline = polyline;
                currentPolyline.setColor(Color.BLACK);
                selected = true;
            }
            currentCable = met.buscarPolyline(polyline);
            Toast.makeText(MainActivity.this, "Distancia: "+String.valueOf(currentCable.distancia), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error encontrando el cable", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Abre el dialog para crear un router
     */
    public void openRouterDialog() {
        routerDialog routerDialog = new routerDialog();
        routerDialog.show(getSupportFragmentManager(), "router dialog");
    }

    /**
     * Abre el dialog para crear un cable
     */
    public void openCableDialog() {
        cableDialog cableDialog = new cableDialog();
        cableDialog.show(getSupportFragmentManager(), "cable dialog");
    }

    /**
     * Abre el dialog para crear un registro
     */
    public void openRegisterDialog() {
        registerDialog registerDialog = new registerDialog();
        registerDialog.show(getSupportFragmentManager(), "register dialog");
    }

    /**
     * Abre el dialog para editar un router
     */
    public void openEditRouterDialog() {
        editRouterDialog editRouterDialog = new editRouterDialog();
        editRouterDialog.show(getSupportFragmentManager(), "edit router dialog");
    }

    /**
     * Abre el dialog para editar un cable
     */
    public void openEditCableDialog() {
        editCableDialog editCableDialog = new editCableDialog();
        editCableDialog.show(getSupportFragmentManager(), "edit cable dialog");
    }

    /**
     * Abre el Dijkstra dialog
     */
    public void openDijkstraDialog() {
        dijkstraDialog dijkstraDialog = new dijkstraDialog();
        dijkstraDialog.show(getSupportFragmentManager(), "dijkstra dialog");
    }

    /**
     * Obtiene los datos que se ingresaron en el router dialog y los guarda
     * @param nombre
     * @param descripcion
     */
    @Override
    public void applyTexts(String nombre, String descripcion) {
        currentNombre = nombre;
        currentDescripcion = descripcion;

        if ((!currentNombre.equals("")) && (!currentDescripcion.equals(""))) {
            String msj = met.insertarRouter(currentNombre, currentUbicacion, currentDescripcion);
            if (msj.equals("Insertado")) {
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .position(currentUbicacion)
                        .title(currentNombre)
                        .snippet("Descripción: " + currentDescripcion));
                newMarker.setTag(0);
                Circle area = mMap.addCircle(new CircleOptions()
                        .center(currentUbicacion)
                        .radius(25)
                        .strokeColor(Color.WHITE)
                        .fillColor(Color.LTGRAY));
                Router router = met.buscarNombre(currentNombre);
                router.setArea(area);
                router.setMarker(newMarker);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alerta").setMessage(msj).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
            currentNombre = "";
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerta").setMessage("Rellene todos los campos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Obtiene los datos que se ingresaron en el cable dialog y los guarda
     * @param distancia
     * @param velocidad
     * @param tipo
     */
    @Override
    public void applyTexts(int distancia, int velocidad, String tipo) {
        currentDistancia = distancia;
        currentVelocidad = velocidad;
        currentTipo = tipo;

        if ((currentDistancia != -1) && (currentVelocidad != -1) && (!currentTipo.equals(""))) {
            met.insertarCable(currentRouter, currentDest);
            met.insertarCable(currentDest, currentRouter);
            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .add(currentRouter.ubicacion, currentDest.ubicacion)
                    .width(15)
                    .color(Color.GRAY));
            polyline.setClickable(true);
            polyline.setTag(currentRouter.nombre+"/"+currentDest.nombre);
            Cable cable1 = met.buscarCable(currentRouter, currentDest);
            cable1.setDistancia(currentDistancia);
            cable1.setVelocidadTransferencia(currentVelocidad);
            cable1.setTipo(currentTipo);
            cable1.setPolyline(polyline);
            cable1.tag1 = currentRouter.nombre+"/"+currentDest.nombre;
            cable1.tag2 = currentDest.nombre+"/"+currentRouter.nombre;
            Cable cable2 = met.buscarCable(currentDest, currentRouter);
            cable2.setDistancia(currentDistancia);
            cable2.setVelocidadTransferencia(currentVelocidad);
            cable2.setTipo(currentTipo);
            cable2.setPolyline(polyline);
            cable2.tag1 = currentRouter.nombre+"/"+currentDest.nombre;
            cable2.tag2 = currentDest.nombre+"/"+currentRouter.nombre;

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerta").setMessage("Rellene todos los campos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Obtiene los datos que se ingresaron en el register dialog y los guarda
     * @param id
     * @param fecha
     * @param tipo
     * @param estado
     */
    @Override
    public void applyTexts(int id, String fecha, String tipo, String estado) {
        currentId = id;
        currentFecha = fecha;
        currentTipoRevision = tipo;
        currentEstado = estado;

        if ((currentId != -1) && (!currentFecha.equals("")) && (!currentTipoRevision.equals("")) && (!currentEstado.equals(""))) {
            String msj = met.insertarRegistro(currentRouter, id, fecha, tipo, estado);
            Toast.makeText(MainActivity.this, msj, Toast.LENGTH_LONG).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerta").setMessage("Rellene todos los campos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Obtiene los datos del edit router dialog y los guarda
     * @param nombre
     * @param descripcion
     * @param edit
     */
    @Override
    public void applyTexts(String nombre, String descripcion, boolean edit) {
        if (met.buscarNombre(nombre) == null) {
            currentNombre = nombre;
            currentDescripcion = descripcion;
            currentRouter.nombre = nombre;
            currentRouter.descripcion = descripcion;
            currentRouter.marker.setTitle(nombre);
            currentRouter.marker.setSnippet("Descripción: " + descripcion);
        }
    }

    /**
     * Obtiene los datos del edit cable dialog y los guarda
     * @param distancia
     * @param velocidad
     * @param tipo
     * @param edit
     */
    @Override
    public void applyTexts(int distancia, int velocidad, String tipo, boolean edit) {
        currentDistancia = distancia;
        currentVelocidad = velocidad;
        currentTipo = tipo;
        Router origen = currentCable.origen;
        Router destino = currentCable.destino;

        Cable cable1 = met.buscarCable(origen, destino);
        cable1.distancia = distancia;
        cable1.velocidadTransferencia = velocidad;
        cable1.tipo = tipo;
        Cable cable2 = met.buscarCable(destino, origen);
        cable2.distancia = distancia;
        cable2.velocidadTransferencia = velocidad;
        cable2.tipo = tipo;
    }

    /**
     * Obtiene los datos del dijkstra dialog
     * @param o -> origen
     * @param d -> destino
     * @param num
     */
    @Override
    public void applyTexts(String o, String d, int num) {
        if (num == 1) {
            Router origen = null;
            Router destino = null;
            origen = met.buscarNombre(o);
            destino = met.buscarNombre(d);

            if (origen != null && destino != null) {
                met.existe = false;
                met.camino(origen, destino);
                if (met.existe) {
                    met.reinicio();
                    ArrayList<String> camino = new ArrayList<>();
                    polylines = new ArrayList<>();
                    String rutaCorta = met.Dijkstra(origen, origen, destino, 0, camino);
                    met.restaurar();
                    ArrayList<String> parejas = traducirStringPolyline(rutaCorta);
                    for (String p: parejas) {
                        if (met.buscarTag(p) != null)
                            polylines.add(met.buscarTag(p));
                    }
                    for (Polyline p: polylines) {
                        p.setColor(Color.BLUE);
                    }
                    onDijkstra = true;
                } else {
                    Toast.makeText(MainActivity.this, "La ruta especificada no existe", Toast.LENGTH_SHORT).show();
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Alerta").setMessage("Tanto el origen como el destino deben existir").setPositiveButton
                        ("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }

        } else if (num == 2) {
            Router origen = null;
            Router destino = null;
            origen = met.buscarNombre(o);
            destino = met.buscarNombre(d);


        }
    }

    public ArrayList traducirStringPolyline(String rutaCorta) {
        if (!rutaCorta.equals("")) {
            String[] ruta = rutaCorta.split(",");
            ArrayList<String> parejas = new ArrayList<>();
            for (int i = 0; i < ruta.length; i++) {
                if ((!ruta[i].equals("null")) && (!ruta[i].equals("")) && ((parejas.size()) < (ruta.length-2))) {
                    ruta[i] += "/";
                    ruta[i] += ruta[i+1];
                    parejas.add(ruta[i]);
                }
            }
            return parejas;
        }
        return null;
    }

    /**
     * Método para crear menu de contexto
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (menuContextType.equals("OpcionesRouter")) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.router_options_menu, menu);
        }
        if (menuContextType.equals("OpcionesCable")) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.cable_options_menu, menu);
        }
        if (menuContextType.equals("Rutas")) {
            menu.setHeaderTitle(menuContextType);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.routes_options_menu, menu);
        }
        if (menuContextType.equals("OpcionesGrafo")) {
            menu.setHeaderTitle("Opciones");
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.graph_option_menu, menu);
        }
    }

    /**
     * Toma la selección del menu de contexto
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (menuContextType == "OpcionesRouter") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.delete_item:
                    deleteRouter();
                    return true;

                case R.id.edit_item:
                    editRouter();
                    return true;

                case R.id.inspect_item:
                    inspect();
                    return true;

                case R.id.register:
                    createRegister();
                    return true;

                case R.id.search_item:
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }
        if (menuContextType == "OpcionesCable") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.delete_item:
                    deleteCable();
                    return true;

                case R.id.edit_item:
                    editCable();
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }

        if (menuContextType == "Rutas") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.dijkstra_item:
                    openDijkstraDialog();
                    return true;

                case R.id.floyd_item:
                    met.cargarVertices();
                    int[][] matriz = met.cargarMatriz();
                    String ruta = met.floyd(matriz);
                    Toast.makeText(MainActivity.this, ruta, Toast.LENGTH_LONG).show();
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }

        if (menuContextType == "OpcionesGrafo") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.inspect_graph_item:
                    Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                    startActivity(intent);
                    return true;

                default:
                    return super.onContextItemSelected(item);
            }
        }
        return super.onContextItemSelected(item);
    }

    /**
     * Elimina el vertice seleccionado del grafo
     */
    public void deleteRouter() {
        ArrayList<Polyline> polylines = met.eliminarVertice(currentRouter);
        if (currentRouter.marker != null)
            currentRouter.marker.remove();
        if (currentRouter.area != null)
            currentRouter.area.remove();
        currentRouter = null;
        for (Polyline p : polylines) {
            p.remove();
        }
    }

    /**
     * Edita un vertice seleccionado del grafo
     */
    public void editRouter() {
        openEditRouterDialog();
    }

    /**
     * Habilita el dialog de inspeccionar para ver la info del vertice
     */
    public void inspect() {
        int e = met.gradoExterno(currentRouter);
        int i = met.gradoInterno(currentRouter);
        String gradoE = String.valueOf(e);
        String gradoI = String.valueOf(i);
        String valor;
        valor = met.conexo();
        if (valor.equals("conexo")) {
            valor = met.completo();
        }

        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        intent.putExtra("nombre", currentRouter.nombre);
        intent.putExtra("descripcion", currentRouter.descripcion);
        intent.putExtra("gradoI", gradoI);
        intent.putExtra("gradoE", gradoE);
        intent.putExtra("valor", valor);
        startActivity(intent);
    }

    /**
     * Crea un registro nuevo
     */
    public void createRegister() {
        openRegisterDialog();
    }

    /**
     * Elimina un cable seleccionado del grafo
     */
    public void deleteCable() {
        try {
            String tag = (String) currentPolyline.getTag();
            String[] routers = tag.split("/");
            Router origen = met.buscarNombre(routers[0]);
            Router destino = met.buscarNombre(routers[1]);
            met.eliminarCable(origen, destino);
            met.eliminarCable(destino, origen);
            currentPolyline.remove();
            selected = false;
            currentPolyline = null;
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Seleccione un cable", Toast.LENGTH_SHORT);
        }

    }

    /**
     * Edita un cable seleccionado del grafo
     */
    public void editCable() {
        openEditCableDialog();
    }

}
