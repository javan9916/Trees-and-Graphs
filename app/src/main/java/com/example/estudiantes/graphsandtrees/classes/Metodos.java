package com.example.estudiantes.graphsandtrees.classes;

import android.app.AlertDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ArrayAdapter;

import com.directions.route.RouteException;
import com.example.estudiantes.graphsandtrees.activities.MainActivity;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.androidmapsextensions.utils.LatLngUtils;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Metodos {

    Router inicio;
    String msj;
    int extensionMinima = Integer.MAX_VALUE;
    boolean encontrado = false;
    ArrayList<Register> registersList = new ArrayList<>();
    ArrayList<Router> graph = new ArrayList<>();
    ArrayList<Cable> cables = new ArrayList<>();
    public boolean eliminado = false;
    public boolean existe = false;
    ArrayList<String> Vertices;
    String lista;

    public String rutaCorta;
    String ruta;


    public static Metodos instance = null;

    public static Metodos getInstance() {
        if (instance == null) {
            instance = new Metodos();
        }
        return instance;
    }

    /**
     * Inserta un router en el grafo con los parámetros dados.
     *
     * @param nombre
     * @param descripcion
     * @return String "Insertado"
     */
    public String insertarRouter(String nombre, LatLng ubicacion, String descripcion) {
        Router nuevo = new Router(nombre, ubicacion, descripcion);
        if (buscarNombre(nombre) == null) {
            if (buscarArea(ubicacion) != nuevo) {
                if (inicio == null) {
                    inicio = nuevo;
                    return "Insertado";
                }
                nuevo.sigRo = inicio;
                inicio = nuevo;
                return "Insertado";
            }
        }
        return "El Router no se puede repetir";
    }

    /**
     * Limpia la lista graph y la carga de nuevo
     *
     * @return
     */
    public ArrayList<Router> recorrerGraph() {
        graph.clear();
        recorrerRouters();
        return graph;
    }

    /**
     * Devuelve la lista de routers llena
     */
    public void recorrerRouters() {
        Router aux = inicio;
        while (aux != null) {
            graph.add(aux);
            aux = aux.sigRo;
        }
    }

    /**
     * Devuelve el router
     *
     * @param router
     * @return
     */
    public Router buscarRouter(Router router) {
        Router aux = inicio;
        while (aux != null) {
            if (aux == router) {
                return aux;
            }
            aux = aux.sigRo;
        }
        return null;
    }

    /**
     * Busca un router con el nombre dado.
     *
     * @param nombre
     * @return Router que tiene el nombre dado
     */
    public Router buscarNombre(String nombre) {
        Router aux = inicio;
        while (aux != null) {
            if (aux.nombre.equals(nombre))
                return aux;
            aux = aux.sigRo;
        }
        return null;
    }

    /**
     * Busca un router con el nombre dado.
     *
     * @param ubicacion
     * @return Router que tiene la ubicación dada
     */
    public Router buscarUbicacion(LatLng ubicacion) {
        Router aux = inicio;
        while (aux != null) {
            if (aux.ubicacion.equals(ubicacion))
                return aux;
            aux = aux.sigRo;
        }
        return null;
    }

    /**
     * Busca el Router que contiene la ubicación dada en el área
     *
     * @param ubicacion
     * @return Router
     */
    public Router buscarArea(LatLng ubicacion) {
        Router aux = inicio;
        while (aux != null) {
            if (contains(ubicacion, aux.area))
                return aux;
            aux = aux.sigRo;
        }
        return null;
    }

    /**
     * Inserta un nuevo cable en el router dado
     *
     * @param router
     * @param destino
     * @return
     */
    public String insertarCable(Router router, Router destino) {
        if (buscarCable(router, destino) == null) {
            Cable nuevo = new Cable(router, destino);
            if (router.sigC == null) {
                router.sigC = nuevo;
            } else {
                nuevo.sigC = router.sigC;
                router.sigC.antC = nuevo;
                router.sigC = nuevo;
            }
            return "Insertado";
        }
        return "El cable no se puede repetir";
    }

    /**
     * Limpia la lista cables y la carga de nuevo
     *
     * @return
     */
    public ArrayList<Cable> recorrerRouter(String nombre) {
        Router router = buscarNombre(nombre);
        cables.clear();
        recorrerCables(router);
        return cables;
    }

    /**
     * Devuelve la lista de cables de un router llena
     */
    public void recorrerCables(Router router) {
        Cable aux = router.sigC;
        while (aux != null) {
            cables.add(aux);
            aux = aux.sigC;
        }
    }

    /**
     * Cuenta la cantidad de cables de un router dado
     *
     * @param router
     * @return
     */
    public int contarCables(Router router) {
        int cont = 0;
        Cable aux = router.sigC;
        while (aux != null) {
            cont++;
            aux = aux.sigC;
        }
        return cont;
    }

    /**
     * Busca un Cable que esté entre dos routers dados
     *
     * @param router
     * @param destino
     * @return
     */
    public Cable buscarCable(Router router, Router destino) {
        if (router.sigC != null) {
            Cable aux = router.sigC;
            while (aux != null) {
                if (aux.destino == destino) {
                    return aux;
                }
                aux = aux.sigC;
            }
        }
        return null;
    }

    /**
     * Busca un cable con respecto a otro enviado
     *
     * @param cable
     * @return
     */
    public Cable buscarCable(Cable cable) {
        Router aux = inicio;
        while (aux != null) {
            Cable temp = aux.sigC;
            while (temp != null) {
                if (temp.destino == cable.destino || temp.equals(cable))
                    return temp;
                temp = temp.sigC;
            }
            aux = aux.sigRo;
        }
        return null;
    }

    /**
     * Buscar un cable que tenga el polyline dado por medio del Tag
     *
     * @param polyline
     * @return
     */
    public Cable buscarPolyline(Polyline polyline) {
        if (inicio != null) {
            Router aux = inicio;
            while (aux != null) {
                Cable temp = aux.sigC;
                while (temp != null) {
                    if (temp.polyline.getTag().equals(polyline.getTag()))
                        return temp;
                    temp = temp.sigC;
                }
                aux = aux.sigRo;
            }
        }
        return null;
    }

    /**
     * Busca el polyline del tag dado
     *
     * @param tag
     * @return
     */
    public Polyline buscarTag(String tag) {
        if (inicio != null) {
            Router aux = inicio;
            while (aux != null) {
                Cable temp = aux.sigC;
                while (temp != null) {
                    if ((temp.tag1.equals(tag)) || (temp.tag2.equals(tag)))
                        return temp.polyline;
                    temp = temp.sigC;
                }
                aux = aux.sigRo;
            }
        }
        return null;
    }

    /**
     * Calcula el grado interno de un router dado
     *
     * @param destino
     * @return grado interno
     */
    public int gradoInterno(Router destino) {
        if (inicio != null) {
            Router aux = inicio;
            int cont = 0;
            while (aux != null) {
                if ((aux != destino) && (buscarCable(aux, destino) != null)) {
                    cont++;
                }
                aux = aux.sigRo;
            }
            return cont;
        }
        return 0;
    }

    /**
     * Calcula el grado externo de un router dado
     *
     * @param router
     * @return grado interno
     */
    public int gradoExterno(Router router) {
        int cont = 0;
        Cable aux = router.sigC;
        if (inicio != null) {
            while (aux != null) {
                cont++;
                aux = aux.sigC;
            }
        }
        return cont;
    }

    /**
     * Calcula si una ubicación dada se encuentra en una area dada
     *
     * @param ubicacion
     * @param area
     * @return true si lo contiene
     */
    public boolean contains(LatLng ubicacion, Circle area) {
        LatLng areaCenter = area.getCenter();
        double radius = area.getRadius();
        float distance = LatLngUtils.distanceBetween(ubicacion, areaCenter);
        return distance < radius;
    }

    /**
     * Elimina un cable dado del grafo
     *
     * @param router
     * @param destino
     * @return true si fue eliminado
     */
    public boolean eliminarCable(Router router, Router destino) {
        Cable aux = buscarCable(router, destino);
        if (aux != null) {
            if (router.sigC == aux) {
                router.sigC = aux.sigC;
                if (aux.sigC != null) {
                    aux.sigC.antC = null;
                }
                return true;
            }
            aux.antC.sigC = aux.sigC;
            if (aux.sigC != null) {
                aux.sigC.antC = aux.antC;
            }
            return true;
        }
        return false;
    }

    /**
     * Elimina un router dado del grafo
     *
     * @param router
     * @return true si fue eliminado
     */
    public ArrayList<Polyline> eliminarVertice(Router router) {
        ArrayList<Polyline> polylines = new ArrayList<>();
        eliminarCables(router, polylines);
        Cable aux = router.sigC;
        while (aux != null) {
            Polyline polyline = aux.polyline;
            polylines.add(polyline);
            eliminarCable(router, aux.destino);
            aux = aux.sigC;
        }
        if (router == inicio) {
            inicio = router.sigRo;
        }
        Router temp = inicio;
        Router ant = null;
        while (temp != null) {
            if (temp == router) {
                ant.sigRo = temp.sigRo;
            }
            ant = temp;
            temp = temp.sigRo;
        }
        return polylines;
    }

    /**
     * Elimina los cables en el grafo de un router dado
     *
     * @param router
     * @return
     */
    public ArrayList<Polyline> eliminarCables(Router router, ArrayList<Polyline> polylines) {
        Router aux = inicio;
        if (contarVertices() > 1) {
            while (aux != null) {
                if (aux != router) {
                    Cable temp = aux.sigC;
                    while (temp != null) {
                        if (temp.destino == router) {
                            Polyline polyline = temp.polyline;
                            polylines.add(polyline);
                            eliminarCable(aux, router);
                        }
                        temp = temp.sigC;
                    }
                }
                aux = aux.sigRo;
            }
        } else {
            Cable temp = aux.sigC;
            if (temp != null) {
                Polyline polyline = temp.polyline;
                polylines.add(polyline);
                eliminarCable(router, aux);
            }
            router = null;
            inicio = null;
        }
        return polylines;
    }

    /**
     * Cuenta los vértices del grafo
     */
    public int contarVertices() {
        int cont = 0;
        Router aux = inicio;
        while (aux != null) {
            cont++;
            aux = aux.sigRo;
        }
        return cont;
    }

    /**
     * Inserta un registro en el árbol binario de un router dado
     *
     * @param router
     * @param id
     * @param fecha
     * @param tipo
     * @param estado
     * @return
     */
    public String insertarRegistro(Router router, int id, String fecha, String tipo, String estado) {
        crearRegistro(router, router.getRaiz(), router.getRaiz(), id, fecha, tipo, estado);
        return msj;
    }

    /**
     * Método utilizado para crear el registro
     *
     * @param router
     * @param raiz
     * @param aux
     * @param id
     * @param fecha
     * @param tipo
     * @param estado
     * @return
     */
    public String crearRegistro(Router router, Register raiz, Register aux, int id, String fecha, String tipo, String estado) {
        Register nuevo = new Register(id, fecha, tipo, estado);

        if (raiz == null) {
            router.raiz = nuevo;
            msj = "Insertado";
            return msj;
        }
        if (id < aux.id) {
            if (aux.izq == null) {
                aux.izq = nuevo;
                msj = "Insertado";
                return msj;
            }
            crearRegistro(router, raiz, aux.izq, id, fecha, tipo, estado);
        } else if (id > aux.id) {
            if (aux.der == null) {
                aux.der = nuevo;
                msj = "Insertado";
                return msj;
            }
            crearRegistro(router, raiz, aux.der, id, fecha, tipo, estado);
        } else {
            msj = "No se puede repetir";
            return msj;
        }
        return msj;
    }

    /**
     * Busca un registro por medio del id dado
     *
     * @param aux
     * @param id
     * @param router
     * @return
     */
    public Register buscarRegistro(Register aux, int id, Router router) {
        if (router.raiz == null) {
            return null;
        }
        if (id < aux.id) {
            if (aux.izq != null) {
                buscarRegistro(aux.izq, id, router);
            }
        } else if (id > aux.id) {
            if (aux.der != null) {
                buscarRegistro(aux.der, id, router);
            }
        } else {
            return aux;
        }

        return null;
    }

    /**
     * Limpia la lista de registros y agarra los valores nuevos
     *
     * @return
     */
    public ArrayList<Register> registersOrden(Router router, String orden) {
        if (orden.equals("inorden")) {
            registersList.clear();
            recorrerInorden(router.raiz);
        }
        if (orden.equals("preorden")) {
            registersList.clear();
            recorrerPreorden(router.raiz);
        }
        return registersList;
    }

    /**
     * Recorre el arbol preorden
     *
     * @param register
     */
    public void recorrerPreorden(Register register) {
        if (register == null)
            return;

        registersList.add(register);
        recorrerPreorden(register.izq);
        recorrerPreorden(register.der);
    }

    /**
     * Recorre el arbol inorden
     *
     * @param register
     */
    public void recorrerInorden(Register register) {
        if (register == null)
            return;

        recorrerPreorden(register.izq);
        registersList.add(register);
        recorrerPreorden(register.der);
    }

    /**
     * Reinicia eliminado y llama al método para eliminar
     *
     * @param aux
     * @param id
     * @param padre
     * @return
     */
    public boolean eliminarRegister(Register aux, int id, Register padre, Router router) {
        eliminado = false;
        eliminar(aux, id, padre, router);
        return eliminado;
    }

    /**
     * Elimina un registro del árbol
     *
     * @param aux
     * @param id
     * @param padre
     */
    public void eliminar(Register aux, int id, Register padre, Router router) {
        if (router.raiz.id == id) {
            if ((router.raiz.izq == null) && (router.raiz.der == null)) {
                router.raiz = null;
                eliminado = true;
            } else {
                if ((router.raiz.izq != null) && (router.raiz.der != null)) {
                    padre = aux;
                    aux = router.raiz.izq;
                    while (aux.der != null) {
                        padre = aux;
                        aux = aux.der;
                    }
                    padre.der = aux.izq;
                    aux.izq = router.raiz.izq;
                    aux.der = router.raiz.der;
                    router.raiz = aux;
                } else {
                    if (aux.izq != null) {
                        router.raiz = router.raiz.izq;
                    } else {
                        router.raiz = router.raiz.der;
                    }
                }
            }
            eliminado = true;
        } else {
            if (aux.id > id) {
                if (aux.izq != null) {
                    eliminarRegister(aux.izq, id, aux, router);
                } else {
                    eliminado = false;
                }
            }
            if (aux.id < id) {
                if (aux.der != null) {
                    eliminarRegister(aux.der, id, aux, router);
                } else {
                    eliminado = false;
                }
            } else {
                if ((aux.izq == null) && (aux.der == null)) {
                    if (aux.id > padre.id)
                        padre.der = null;
                    else
                        padre.izq = null;
                } else if ((aux.izq != null) && (aux.der != null)) {
                    Register padre2 = aux;
                    Register aux2 = aux.izq;
                    while (aux2.der != null) {
                        padre2 = aux;
                        aux2 = aux2.der;
                    }
                    padre2.der = aux2.izq;
                    aux2.izq = aux.izq;
                    aux2.der = aux.der;
                    if (aux.id > padre.id) {
                        padre.der = aux2;
                    } else {
                        padre.izq = aux2;
                    }
                } else {
                    if (aux.izq != null) {
                        if (aux.id > padre.id)
                            padre.der = aux.izq;
                        else
                            padre.izq = aux.izq;
                    } else {
                        if (aux.id > padre.id)
                            padre.der = aux.der;
                        else
                            padre.izq = aux.der;
                    }
                    eliminado = true;
                }
            }
        }
    }

    /**
     * Algoritmo de ruta corta Dijkstra
     *
     * @param origen
     * @param actual
     * @param destino
     * @param extension
     * @param camino
     * @return
     */
    public String Dijkstra(Router origen, Router actual, Router destino, int extension, ArrayList<String> camino) {

        if (origen == destino) {
            lista = "";
            lista += "Si se puede llegar de " + origen.nombre + " a " + destino.nombre + "\n";
            lista += "Ya que el origen y el destino son el mismo. Extencion: " + extension + "\n";
            encontrado = true;
            return lista;
        }
        if ((actual == null) | (actual.marca == true)) {
            return null;
        } else {
            actual.marca = true;
            Cable aux = actual.sigC;
            while (aux != null) {
                if (actual == destino) {
                    if (extension < extensionMinima) {
                        ruta = "";
                        extensionMinima = extension;
                        lista = "";
                        lista += "Si se puede llegar de " + origen.nombre + " a " + destino.nombre + "\n";
                        String mensaje = ("Siguiendo los siguientes Vertices: ");
                        for (int i = 0; i < camino.size(); i++) {
                            mensaje += "-" + camino.get(i);
                            ruta += "," + camino.get(i);
                        }
                        mensaje += " - " + actual.nombre;
                        ruta += "," + actual.nombre;
                        lista += mensaje + "\n";
                        lista += "Extension total: " + extensionMinima;
                        encontrado = true;
                        actual.marca = false;
                        return lista;
                    }
                }
                int extencionActual = extension;
                ArrayList<String> caminoActual = new ArrayList<>();
                for (int i = 0; i < camino.size(); i++) {
                    caminoActual.add(camino.get(i));
                }

                camino.add(actual.nombre);
                Dijkstra(origen, aux.destino, destino, extension += aux.distancia, camino);
                extension = extencionActual;

                camino.clear();
                for (int i = 0; i < caminoActual.size(); i++) {
                    camino.add(caminoActual.get(i));
                }
                aux = aux.sigC;
            }
        }
        if (encontrado == false) {
            return null;
        } else {
            actual.marca = false;
            return ruta;
        }
    }

    public void restaurar() {
        extensionMinima = Integer.MAX_VALUE;
        ruta = "";
        encontrado = false;
        Router aux = inicio;
        while (aux != null) {
            aux.marca = false;
            aux = aux.sigRo;
        }
    }

    /**
     * Le quita las marcas a todos los Routers
     */
    public void reinicio() {
        Router aux = inicio;
        while (aux != null) {
            aux.marca = false;
            aux = aux.sigRo;
        }
    }

    /**
     * Verifica si existe un camino entre el origen y el destino dados
     *
     * @param origen
     * @param destino
     */
    public void camino(Router origen, Router destino) {
        if ((origen == null) || (origen.marca == true)) {
            return;
        } else {
            origen.marca = true;
            Cable aux = origen.sigC;
            while (aux != null) {
                if (aux.destino == destino) {
                    existe = true;
                }
                camino(aux.destino, destino);
                aux = aux.sigC;
            }
        }
    }

    /**
     * Crea un array con los nombres de los routers que existen
     */
    public void cargarVertices() {
        Router aux = inicio;
        int tamano = contarVertices();
        Vertices = new ArrayList<>();
        int contador = 0;
        while (aux != null || contador < tamano) {
            System.out.println(contador);
            Vertices.add(aux.nombre);
            contador++;
            aux = aux.sigRo;
        }
    }

    /**
     * Toma los cables y escribe sus pesos en una matriz
     *
     * @return
     */
    public int[][] cargarMatriz() {
        int tamano = contarVertices();
        int matriz[][] = new int[tamano][tamano];
        if (tamano == 0) {
            System.out.println("Sin routers");
        } else {
            for (int i = 0; i < tamano; i++) {
                Router v1 = buscarNombre(Vertices.get(i));
                for (int j = 0; j < tamano; j++) {
                    Router v2 = buscarNombre(Vertices.get(j));
                    if (v1.equals(v2)) {
                        matriz[i][j] = 0;
                    } else {
                        Cable arco = buscarCable(v1, v2);
                        if (arco != null) {
                            matriz[i][j] = arco.distancia;
                        } else {
                            matriz[i][j] = 9999;
                        }
                    }
                }
            }

        }
        return matriz;
    }

    /**
     * Algoritmo Floyd
     *
     * @param adyacencia
     * @return
     */
    public String floyd(int[][] adyacencia) {
        int n = adyacencia.length;
        int D[][] = adyacencia;

        String enlaces[][] = new String[n][n];
        String[][] aux_enlaces = new String[adyacencia.length][adyacencia.length];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                enlaces[i][j] = "";
                aux_enlaces[i][j] = "";
            }
        }
        String enl_rec = "";
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    float aux = D[i][j];
                    float aux2 = D[i][k];
                    float aux3 = D[k][j];
                    float aux4 = aux2 + aux3;
                    float res = Math.min(aux, aux4);
                    if (aux != aux4) {
                        if (res == aux4) {
                            enl_rec = "";
                            aux_enlaces[i][j] = k + "";
                            enlaces[i][j] = enlaces(i, k, aux_enlaces, enl_rec) + (k + 1);
                        }
                    }
                    D[i][j] = (int) res;
                }
            }
        }

        String cadena = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cadena += D[i][j] + " ";
            }
            cadena += "\n";
        }

        String enlacesres = "";
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    if (enlaces[i][j].equals("") == true) {
                        enlacesres += " De ( " + (i + 1) + " a " + (j + 1) + " ) = " + "( " + (i + 1) + " , " + (j + 1) + " )" + "\n";
                    } else
                        enlacesres += " De ( " + (i + 1) + " a " + (j + 1) + " ) = ( " + (i + 1) + " , " + enlaces[i][j] + " , " + (j + 1) + ")\n";
                }
            }
        }

        return "las distancias minimas entre nodos son: \n" + cadena + "\nlos caminos minimos entre nodos son:\n" + enlacesres;
    }

    public String enlaces(int i, int k, String[][] aux_enlaces, String enl_rec) {
        if (aux_enlaces[i][k].equals("") == true) {
            return "";
        } else {
            enl_rec += enlaces(i, Integer.parseInt(aux_enlaces[i][k].toString()), aux_enlaces, enl_rec) + (Integer.parseInt(aux_enlaces[i][k].toString()) + 1) + " , ";
            return enl_rec;
        }
    }

    /**
     * Verifica si un grafo es conexo o no
     *
     * @return
     */
    public String conexo() {
        Router aux = inicio;
        while (aux != null) {
            if (contarCables(aux) == 0)
                return "noconexo";
            aux = aux.sigRo;
        }
        return "conexo";
    }

    /**
     * Verifica si un grafo es completo
     *
     * @return
     */
    public String completo() {
        reinicioConexos();
        Router aux = inicio;
        while (aux != null) {
            if (contarCables(aux) == (contarVertices() - 1)) {
                aux.conexo = true;
            } else {
                return "conexo";
            }
            aux = aux.sigRo;
        }
        return "completo";
    }

    /**
     * Reinicia el valor boolean conexo de los routers
     */
    public void reinicioConexos() {
        Router aux = inicio;
        while (aux != null) {
            aux.conexo = false;
            aux = aux.sigRo;
        }
    }

    /**
     * Crea un lista de registros y dependiendo del tipo de busqueda llama a los debidos métodos para llenarla
     *
     * @param key
     * @param router
     * @param search
     * @return
     */
    public ArrayList<Register> buscarRegistros(String key, Router router, String search) {
        ArrayList<Register> registers = new ArrayList<>();
        if (key.equals("Fecha"))
            buscarFechas(router.raiz, search, registers);
        else if (key.equals("Tipo de Revisión")) {
            buscarTipoRevs(router.raiz, search, registers);
        }

        return registers;
    }

    /**
     * Inserta en la lista registros a los registros con misma fecha
     *
     * @param aux
     * @param fecha
     * @param registers
     */
    public void buscarFechas(Register aux, String fecha, ArrayList<Register> registers) {
        if (aux == null)
            return;

        if (aux.fecha.equals(fecha)) {
            registers.add(aux);
        }

        buscarFechas(aux.izq, fecha, registers);
        buscarFechas(aux.der, fecha, registers);
    }

    /**
     * Inserta en la lista registros a los registros con mismo tipo de revisión
     *
     * @param aux
     * @param tipoRev
     * @param registers
     */
    public void buscarTipoRevs(Register aux, String tipoRev, ArrayList<Register> registers) {
        if (aux == null)
            return;

        if (aux.tipoRevision.equals(tipoRev)) {
            registers.add(aux);
        }

        buscarTipoRevs(aux.izq, tipoRev, registers);
        buscarTipoRevs(aux.der, tipoRev, registers);
    }


    public int[][] AlgKruskal(int[][] Matriz) {
        boolean[] marcados = new boolean[contarVertices()];
        cargarVertices();
        String vertice = Vertices.get(0);
        return AlgKruskal(Matriz, marcados, vertice, new int[Matriz.length][Matriz.length]);
    }

    private int[][] AlgKruskal(int[][] Matriz, boolean[] marcados, String vertice, int[][] Final) {
        marcados[Vertices.indexOf(vertice)] = true;//marcamos el primer nodo
        int aux = -1;
        if (!TodosMarcados(marcados)) {
            for (int i = 0; i < marcados.length; i++) {
                if (marcados[i]) {
                    for (int j = 0; j < Matriz.length; j++) {
                        if (Matriz[i][j] != 0) {
                            if (!marcados[j]) {
                                if (aux == -1) {
                                    aux = Matriz[i][j];
                                } else {
                                    aux = Math.min(aux, Matriz[i][j]);
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < marcados.length; i++) {
                if (marcados[i]) {
                    for (int j = 0; j < Matriz.length; j++) {
                        if (Matriz[i][j] == aux) {
                            if (!marcados[j]) {
                                Final[i][j] = aux;
                                Final[j][i] = aux;
                                return AlgKruskal(Matriz, marcados, Vertices.get(j), Final);
                            }
                        }
                    }
                }
            }
        }
        return Final;
    }

    public boolean TodosMarcados(boolean[] vertice) {
        for (boolean b : vertice) {
            if (!b) {
                return b;
            }
        }
        return true;
    }


}
