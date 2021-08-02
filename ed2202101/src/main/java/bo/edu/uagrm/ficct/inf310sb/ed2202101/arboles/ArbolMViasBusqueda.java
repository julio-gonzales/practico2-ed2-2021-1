package bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles;

import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionClaveNoExiste;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionOrdenInvalido;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ArbolMViasBusqueda <K extends Comparable<K>,V> implements IArbolBusqueda<K,V>{


    protected  NodoMVias<K,V> raiz;
    protected int orden;
    protected  int POSICION_INVALIDA = -1;

    public ArbolMViasBusqueda() {
        this.orden = 3;
    }

    public ArbolMViasBusqueda(int orden) throws ExcepcionOrdenInvalido {
        if (orden < 3) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
    }



    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permite insertar valores nulos");
        }

        if (claveAInsertar == null) {
            throw new RuntimeException("No se permiten insertar claves nulas");
        }

        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
        }

        NodoMVias<K,V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClave  = this.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClave, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {
                if (nodoActual.esHoja()) {
                    if (nodoActual.estanClavesLlenas()) {
                        int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                        NodoMVias<K,V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    } else {
                        this.insertarClaveYValorOrdenadaEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                        NodoMVias<K,V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                        nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                        nodoActual = NodoMVias.nodoVacio();
                    } else {
                        nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                    }

                }
            }
        }
    }

    protected void insertarClaveYValorOrdenadaEnNodo(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        K claveActual = nodoActual.getClave(nodoActual.cantidadDeClavesNoVacias() - 1);
        V valorActual = nodoActual.getValor(nodoActual.cantidadDeClavesNoVacias() - 1);
        int posicionDelUltimo = nodoActual.cantidadDeClavesNoVacias();
        boolean hayClavesAntes = true;

        while (claveActual.compareTo(claveAInsertar) > 0 && hayClavesAntes) {
            nodoActual.setClave(posicionDelUltimo, claveActual);
            nodoActual.setValor(posicionDelUltimo, valorActual);
            posicionDelUltimo--;
            if (posicionDelUltimo >0) {
                claveActual = nodoActual.getClave(posicionDelUltimo - 1);
                valorActual = nodoActual.getValor(posicionDelUltimo - 1);
            } else {
                hayClavesAntes = false;
            }
        }
        nodoActual.setClave(posicionDelUltimo, claveAInsertar);
        nodoActual.setValor(posicionDelUltimo, valorAInsertar);
    }

    protected int obtenerPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveAInsertar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAInsertar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return nodoActual.cantidadDeClavesNoVacias();
    }

    protected int obtenerPosicionDeClave(NodoMVias<K, V> nodoActual, K claveAInsertar) {
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveActual.compareTo(claveAInsertar) == 0) {
                return i;
            }
        }
        return POSICION_INVALIDA;
    }

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        V valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ExcepcionClaveNoExiste();
        }

        this.raiz = eliminar(this.raiz, claveAEliminar);


        return valorAEliminar;
    }

    private NodoMVias<K,V> eliminar(NodoMVias<K,V> nodoActual, K claveAEliminar) {
        for(int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                //ya lo encontre
                if (nodoActual.esHoja()) {
                    this.eliminarClaveYValorDelNodo(nodoActual, i);
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        return NodoMVias.nodoVacio();
                    }

                    return nodoActual;
                }
                // no es hoja el nodo actual

                K claveDeReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)) {
                    claveDeReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, claveAEliminar);
                } else {
                    claveDeReemplazo = this.buscarClavePredecesoraInOrdem(nodoActual, claveAEliminar);
                }

                V valorDeReemplazo = buscar(claveDeReemplazo);
                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;
            }

            // no esta en la posicion i del nodo

            if (claveAEliminar.compareTo(claveActual) < 0 ) {
                NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        //si llegue hasta aqui nunca baje por ningun lado, ni lo encontre

        NodoMVias<K,V> supuestoNuevoHijo = this.eliminar(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), claveAEliminar);
        nodoActual.setHijo(nodoActual.cantidadDeClavesNoVacias(), supuestoNuevoHijo);
        return nodoActual;
    }

    private K buscarClavePredecesoraInOrdem(NodoMVias<K,V> nodoActual, K claveAEliminar) {
        List<K> recorrido = new LinkedList<>();
        this.recorridoEnInOrden(nodoActual, recorrido);
        int posicion = recorrido.indexOf(claveAEliminar);
        return recorrido.get(posicion - 1);
    }

    private K buscarClaveSucesoraInOrden(NodoMVias<K,V> nodoActual, K claveAEliminar) {
        List<K> recorrido = new LinkedList<>();
        this.recorridoEnInOrden(nodoActual, recorrido);
        int posicion = recorrido.indexOf(claveAEliminar);
        return recorrido.get(posicion + 1);
    }

    private boolean hayHijosMasAdelante(NodoMVias<K,V> nodoActual, int posicion) {

        for (int i = posicion + 1; i < orden; i++) {
            if (!NodoMVias.esNodoVacio(nodoActual.getHijo(i))) {
                return true;
            }
        }
        return false;
    }

    protected void eliminarClaveYValorDelNodo(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            if (i < nodoActual.cantidadDeClavesNoVacias() - 1) {
                nodoActual.setClave(i, nodoActual.getClave(i + 1));
                nodoActual.setValor(i, nodoActual.getValor(i + 1));
            } else {
                nodoActual.setClave(i, (K)NodoMVias.datoVacio());
                nodoActual.setValor(i,(V) NodoMVias.datoVacio());
            }
        }
    }


    @Override
    public V buscar(K claveABuscar) {

        NodoMVias<K,V> nodoActual = this.raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioDeNodoActual = false;
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias() &&
                    !huboCambioDeNodoActual; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                }

                if (claveABuscar.compareTo(claveActual) < 0) {
                    nodoActual = nodoActual.getHijo(i);
                    huboCambioDeNodoActual = true;
                }
            }
            //fin del for

            //aqui verificamos si hubo cambio de nodo, si no hubo entonces revisamos por el ultimo hijo
            if (!huboCambioDeNodoActual) {
                nodoActual = nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias());
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return this.buscar(claveABuscar) != NodoMVias.datoVacio();
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(NodoMVias<K,V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + size(nodoActual.getHijo(i));
        }
        return cantidad + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }

        int alturaMayor = 0;
        for (int i = 0; i < this.orden; i++) {
            int alturaActual = altura(nodoActual.getHijo(i + 1));
            if (alturaActual > alturaMayor) {
                alturaMayor = alturaActual;
            }
        }

        return alturaMayor + 1;
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }

    private int nivel(NodoMVias<K,V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }

        int nivelMayor = -1;
        for (int i = 0; i < this.orden; i++) {
            int nivelActual = nivel(nodoActual.getHijo(i));
            if (nivelActual > nivelMayor) {
                nivelMayor = nivelActual;
            }
        }

        return nivelMayor + 1;

    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Queue<NodoMVias<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoMVias<K,V> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
                recorrido.add(nodoActual.getClave(i));
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }

            if (!nodoActual.esHijoVacio(nodoActual.cantidadDeClavesNoVacias())) {
                colaDeNodos.offer(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()));
            }
        }

        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPreOrden(this.raiz, recorrido);

        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<K,V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }

        recorridoEnPreOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    }


    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }

        recorridoEnInOrden(nodoActual.getHijo(nodoActual.cantidadDeClavesNoVacias()), recorrido);
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<K, V> nodoActual, List<K> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return ;
        }

        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);
        for (int i = 0; i < nodoActual.cantidadDeClavesNoVacias(); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            recorrido.add(nodoActual.getClave(i));
        }
    }


}
