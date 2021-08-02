package bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles;

import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionClaveNoExiste;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionOrdenInvalido;

import java.util.Stack;

public class ArbolB <K extends Comparable<K>,V> extends  ArbolMViasBusqueda<K,V>{
    private int nroMinimoDeHijos;
    private int nroMinimoDeDatos;
    private int nroMaximoDeDatos;


    public ArbolB() {
        super();
        this.nroMaximoDeDatos = 2;
        this.nroMinimoDeDatos = 1;
        this.nroMinimoDeHijos = 1;
    }

    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDeDatos = super.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos = this.nroMinimoDeDatos + 1;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (valorAInsertar == null) {
            throw new RuntimeException("No se permiten valores nulos");
        }

        if (claveAInsertar == null) {
            throw  new RuntimeException("No se permiten claves nulas");
        }

        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden + 1, claveAInsertar, valorAInsertar);
            return;
        }

        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClave = super.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClave != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClave, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else {
                if (nodoActual.esHoja()) {
                    this.insertarClaveYValorOrdenadaEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                    if (nodoActual.cantidadDeClavesNoVacias() > this.nroMaximoDeDatos) {
                        this.dividir(nodoActual, pilaDeAncestros);
                    }
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    pilaDeAncestros.push(nodoActual);
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }
    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {


        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (nodoActual.cantidadDeClavesNoVacias() > nroMaximoDeDatos) {
                K claveDelMedio = nodoActual.getClave(this.nroMaximoDeDatos/2);
                V valorDelMedio = nodoActual.getValor(this.nroMaximoDeDatos /2);



                //si la pila de nodos viene vacia, creamos un nuevo nodo
                NodoMVias<K,V> nodoPadre;
                if(!pilaDeAncestros.isEmpty()) {
                    nodoPadre = pilaDeAncestros.pop();
                    super.insertarClaveYValorOrdenadaEnNodo(nodoPadre, claveDelMedio, valorDelMedio);
                } else {
                    nodoPadre = new NodoMVias<>(orden +1, claveDelMedio, valorDelMedio);
                }


                int posicionDeClaveEnNodoPadre = super.obtenerPosicionDeClave(nodoPadre, claveDelMedio);

                NodoMVias<K,V> nuevoHijoDerecho = new NodoMVias<>(orden + 1);
                int contadorDelNuevoNodoDerecho = 0;
                for (int i = (this.nroMaximoDeDatos /2) + 1; i < orden; i++) {
                    K claveDelNodo = nodoActual.getClave(i );
                    V valorDelNodo = nodoActual.getValor(i );
                    nuevoHijoDerecho.setClave(contadorDelNuevoNodoDerecho, claveDelNodo);
                    nuevoHijoDerecho.setValor(contadorDelNuevoNodoDerecho, valorDelNodo);
                    nuevoHijoDerecho.setHijo(contadorDelNuevoNodoDerecho, nodoActual.getHijo(i));
                    //vaciar del nodo padre
                    nodoActual.setClave(i, (K) NodoMVias.datoVacio());
                    nodoActual.setValor(i, (V)NodoMVias.datoVacio());
                    nodoActual.setHijo(i,NodoMVias.nodoVacio());
                    contadorDelNuevoNodoDerecho++;
                }
                nodoActual.setClave(this.nroMaximoDeDatos/2, (K)NodoMVias.datoVacio());
                nodoActual.setValor(this.nroMaximoDeDatos/2, (V)NodoMVias.datoVacio());
                nuevoHijoDerecho.setHijo(contadorDelNuevoNodoDerecho,nodoActual.getHijo(orden));
                nodoActual.setHijo(orden,NodoMVias.nodoVacio());



                nodoPadre.setHijo(posicionDeClaveEnNodoPadre, nodoActual);
                //aqui vamos a empezar a setear a los hijos
                for (int i = posicionDeClaveEnNodoPadre; i < orden; i++) {
                    NodoMVias<K,V> nodoAuxiliar = nodoPadre.getHijo(i + 1);
                    nodoPadre.setHijo(posicionDeClaveEnNodoPadre + 1, nuevoHijoDerecho);
                    nuevoHijoDerecho = nodoAuxiliar;
                }

            } else {
                nodoActual = NodoMVias.nodoVacio();
            }
        }

    }

    @Override
    public V eliminar(K claveAEliminar) throws ExcepcionClaveNoExiste {
        V valorAEliminar = this.buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new IllegalArgumentException("clave a eliminar no puede ser nula");
        }

        Stack<NodoMVias<K,V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K,V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDeAncestros);
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new ExcepcionClaveNoExiste();
        }

        int posicionDeClaveAEliminarEnElNodo = super.obtenerPosicionPorDondeBajar(nodoActual,claveAEliminar) - 1;
        V valorARetornar = nodoActual.getValor(posicionDeClaveAEliminarEnElNodo);
        if (nodoActual.esHoja()) {
            super.eliminarClaveYValorDelNodo(nodoActual, posicionDeClaveAEliminarEnElNodo);
            if (nodoActual.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) {
                if (pilaDeAncestros.isEmpty()) {
                    if (nodoActual.cantidadDeClavesNoVacias() == 0) {
                        super.vaciar();
                    }
                } else {
                    this.prestarOFucionar(nodoActual, pilaDeAncestros);
                }
            }
        } else {
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K,V> nodoDelPredecesor = this.buscarNodoDeClavePredecesora(pilaDeAncestros, nodoActual.getHijo(posicionDeClaveAEliminarEnElNodo));
            int posicionDelPredecesor = nodoDelPredecesor.cantidadDeClavesNoVacias() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            super.eliminarClaveYValorDelNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeClaveAEliminarEnElNodo,clavePredecesora);
            nodoActual.setValor(posicionDeClaveAEliminarEnElNodo, valorPredecesor);
            if (nodoDelPredecesor.cantidadDeClavesNoVacias() < this.nroMinimoDeDatos) {
                this.prestarOFusionar(nodoDelPredecesor, pilaDeAncestros);
            }
        }

        return valorARetornar;
    }

    private void prestarOFusionar(NodoMVias<K,V> nodoDelPredecesor, Stack<NodoMVias<K,V>> pilaDeAncestros) {
    }

    private void prestarOFucionar(NodoMVias<K,V> nodoActual, Stack<NodoMVias<K,V>> pilaDeAncestros) {
    }

    private NodoMVias<K,V> buscarNodoDeClavePredecesora(Stack<NodoMVias<K,V>> pilaDeAncestros, NodoMVias<K,V> hijo) {
        return null;
    }

    private NodoMVias<K,V> buscarNodoDeLaClave(K claveAEliminar, Stack<NodoMVias<K,V>> pilaDeAncestros) {
        return null;
    }


}
