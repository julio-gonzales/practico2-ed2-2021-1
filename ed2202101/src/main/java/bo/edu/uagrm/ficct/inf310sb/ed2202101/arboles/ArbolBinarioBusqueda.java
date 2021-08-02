/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles;

import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionClaveNoExiste;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author Hp 240
 * @param <K>
 * @param <V>
 */
public class ArbolBinarioBusqueda<K extends Comparable<K>,V> implements IArbolBusqueda<K,V>{
    
    protected NodoBinario<K,V> raiz;
    
    public ArbolBinarioBusqueda(){
        
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (valorAInsertar == null){
            throw new RuntimeException("no se permiten valores nulos");
        }
        
        if (this.esArbolVacio()){
            this.raiz = new NodoBinario<>(claveAInsertar, valorAInsertar);
        }
        NodoBinario<K,V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K,V> nodoActual = this.raiz;
        
        while (!NodoBinario.esNodoVacio(nodoActual)){
            K claveActual = nodoActual.getClave();
            if (claveAInsertar.compareTo(claveActual) < 0){
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveAInsertar.compareTo(claveActual) > 0) {
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                nodoActual.setValor(valorAInsertar);
                return;
            }
        }
        
        //si se llega a este punto, hallamos el lugar donde insertar la clave
        // y el valor
        NodoBinario<K,V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAInsertar);
        K claveActual = nodoAnterior.getClave();
        if (claveAInsertar.compareTo(claveActual) < 0){
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        } else {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
        
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
    
    private NodoBinario<K,V> eliminar(NodoBinario<K,V> nodoActual, K claveAEliminar) {
        K claveActual = nodoActual.getClave();
        
        if (claveAEliminar.compareTo(claveActual) < 0) {
            NodoBinario<K,V> supuestoNuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return nodoActual;
        }
        
        if (claveAEliminar.compareTo(claveActual) > 0) {
            NodoBinario<K,V> supuestoNuevoHijoDerecho = eliminar(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return nodoActual;
        }
        
        //si llegamos hasta aqui, quiere decir que encontramos la clave a eliminar
        //revisamos que caso es
        //caso 1
        //Es hoja
        if (nodoActual.esHoja()){
            return NodoBinario.nodoVacio();
        }
        
        //caso 2
        //2.1 solo tiene hijo izquierdo
        if (!nodoActual.esVacioHijoIzquierdo() && nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoIzquierdo();
        }
        
        //2.2 solo tiene hijo derecho
        if (nodoActual.esVacioHijoIzquierdo() && !nodoActual.esVacioHijoDerecho()) {
            return nodoActual.getHijoDerecho(); 
        }
        
        //caso 3
        NodoBinario<K,V> nodoDelSucesor = buscarSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K,V> supuestoNuevoHijo = eliminar(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijo);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return nodoActual;
        
    }
    
    protected NodoBinario<K,V> buscarSucesor(NodoBinario<K,V> nodoActual) {
        NodoBinario<K,V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        
        return nodoAnterior;
    }

    @Override
    public V buscar(K claveABuscar) {

        NodoBinario<K,V> nodoActual = this.raiz;
        
        while (!NodoBinario.esNodoVacio(nodoActual)){
            K claveActual = nodoActual.getClave();
            if (claveABuscar.compareTo(claveActual) < 0){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveABuscar.compareTo(claveActual) > 0){
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                return nodoActual.getValor();
            }
        }
        
        //si se llega a este punto la clave a buscar no se encuentra 
        //en el arbol, entonces retornamos null 
        return null;
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return this.buscar(claveABuscar) != null;
    }

    @Override
    public int size() {
        int cantidad = 0;
        if (this.esArbolVacio()){
            return cantidad;
        }
        
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        
        while (!colaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            cantidad++;
            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            
            if (!nodoActual.esVacioHijoDerecho()){
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
            
        }
        return cantidad;
    }

    public int sizeRec(){
        return sizeRec(this.raiz);
    }
    
    private int sizeRec(NodoBinario<K,V> nodoActual){
        if (NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidadPorIzquierda = sizeRec(nodoActual.getHijoIzquierdo()); 
        int cantidadPorDerecha = sizeRec(nodoActual.getHijoDerecho());
        
        return cantidadPorIzquierda + cantidadPorDerecha + 1;
    }
    
    @Override
    public int altura() {
        return altura(this.raiz);
    }
    
    protected int altura(NodoBinario<K,V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        return alturaPorIzquierda > alturaPorDerecha ? alturaPorIzquierda + 1 : 
                alturaPorDerecha + 1;
    }
    
    public int alturaIte(){
        int alturaDelArbol = 0;
        if (this.esArbolVacio()){
            return alturaDelArbol;
        }
        
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        
        while (!colaDeNodos.isEmpty()){
            int numeroDeNodosEnNivel = colaDeNodos.size();
            int posicion = 0;
            while (posicion < numeroDeNodosEnNivel){
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
         
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }

                if (!nodoActual.esVacioHijoDerecho()){
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
            }
            alturaDelArbol++;
            
        }
        return alturaDelArbol;
    }

    @Override
    public int nivel() {
        int nivelDelArbol = - 1;
        if (this.esArbolVacio()) {
            return nivelDelArbol;
        }
        
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);
        while (!colaDeNodos.isEmpty()) {
            int numeroDeNodosEnNivel = colaDeNodos.size();
            int posicion = 0;
            while (posicion < numeroDeNodosEnNivel){
                NodoBinario<K,V> nodoActual = colaDeNodos.poll();
                if (!nodoActual.esVacioHijoIzquierdo()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                posicion++;
            }
            nivelDelArbol++;
        }
        return nivelDelArbol;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()){
            return recorrido;
        }
        
        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);
        
        while (!colaDeNodos.isEmpty()){
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoIzquierdo()){
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()){
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()){
            return recorrido;
        }
        
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<K,V> nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if( !nodoActual.esVacioHijoDerecho() ) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
        }
        
        return recorrido;
    }
    
    public List<K> recorridoPreOrdenRec(){
        List<K> recorrido = new LinkedList<>();
        recorridoPreOrdenRec(this.raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoPreOrdenRec(NodoBinario<K, V> nodoActual, List<K> recorrido) {
        if (NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        
        recorrido.add(nodoActual.getClave());
        recorridoPreOrdenRec(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoPreOrdenRec(nodoActual.getHijoDerecho(), recorrido);
    } 
    

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoEnInOrden(NodoBinario<K,V> nodoActual, List<K> recorrido){
        if (NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        
        recorridoEnInOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorrido.add(nodoActual.getClave());
        recorridoEnInOrden(nodoActual.getHijoDerecho(), recorrido);
    }
    
    public List<K> recorridoEnInOrdenIte(){
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()){
            return recorrido;
        }
        NodoBinario<K,V> nodoActual = this.raiz;
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!nodoActual.esVacioHijoDerecho()){
                nodoActual = nodoActual.getHijoDerecho();
                while (!NodoBinario.esNodoVacio(nodoActual)){
                    pilaDeNodos.push(nodoActual);
                    nodoActual = nodoActual.getHijoIzquierdo();
                }       
            }
        }
        
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new LinkedList<>();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }
    
    private void recorridoEnPostOrden(NodoBinario<K,V> nodoActual, List<K> recorrido){
        if (NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        
        recorridoEnPostOrden(nodoActual.getHijoIzquierdo(), recorrido);
        recorridoEnPostOrden(nodoActual.getHijoDerecho(), recorrido);
        recorrido.add(nodoActual.getClave());
    }
    
    public List<K> recorridoEnPostOrdenIte(){
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()){
            return recorrido;
        }
        
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;
        
        insertarEnPilaParaPostOrden(nodoActual, pilaDeNodos);
        
        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho() && 
                        nodoDelTope.getHijoDerecho() != nodoActual){
                    insertarEnPilaParaPostOrden(nodoDelTope.getHijoDerecho(), 
                            pilaDeNodos);
                }
            }
        }
        
        return recorrido;
    }

    private void insertarEnPilaParaPostOrden(NodoBinario<K, V> nodoActual, Stack pilaDeNodos) {
        while (!NodoBinario.esNodoVacio(nodoActual)){
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esVacioHijoIzquierdo()){
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }

    public int cantidadDeHijosVacios() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int cantidad = 0;

        Queue<NodoBinario<K,V>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(this.raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoBinario<K,V> nodoActual = colaDeNodos.poll();
            if (nodoActual.esHoja()) {
                cantidad = cantidad + 2;
            } else if ((nodoActual.esVacioHijoDerecho() && !nodoActual.esVacioHijoIzquierdo()) ||
                    (!nodoActual.esVacioHijoDerecho() && nodoActual.esVacioHijoIzquierdo())){
                cantidad++;
            }

            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }

            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return cantidad;
    }


    public int cantidadDeHijosVaciosRec() {
        if (this.esArbolVacio()) {
            return 0;
        }
        return cantidadDeHijosVaciosRec(this.raiz);
    }

    private int cantidadDeHijosVaciosRec(NodoBinario<K,V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }

        int cantidaIzq = cantidadDeHijosVaciosRec(nodoActual.getHijoIzquierdo());
        int cantidaDer = cantidadDeHijosVaciosRec(nodoActual.getHijoDerecho());



        return cantidaIzq + cantidaDer;
    }

    public boolean hayHijosVaciosAntesDelNivel (int nivel) {
        return hayHijosVaciosAntesDelNivel(this.raiz, nivel, 0);
    }

    private boolean hayHijosVaciosAntesDelNivel(NodoBinario<K,V> nodoActual, int nivelObjetivo, int nivelActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return false;
        }
        if (nivelActual < nivelObjetivo) {
            if (nodoActual.esVacioHijoDerecho() || nodoActual.esVacioHijoIzquierdo()) {
                return true;
            }
        }

        boolean hijosIzq = hayHijosVaciosAntesDelNivel(nodoActual.getHijoIzquierdo(), nivelObjetivo, nivelActual + 1);
        boolean hijosDer = hayHijosVaciosAntesDelNivel(nodoActual.getHijoDerecho(), nivelObjetivo, nivelActual + 1);

        return hijosIzq || hijosDer ? true : false;

    }


    //ejercicios

    //rconstruir un arból a partir de sus recorridos

    public ArbolBinarioBusqueda(List<K> clavesInOrden, List<V> valoresInOrden,
                                List<K> claveNoInOrden, List<V> valoresNoInOrden,
                                boolean esConPreOrden ) {
        if (!(clavesInOrden.size() == valoresInOrden.size()) &&
                !(clavesInOrden.size() == valoresNoInOrden.size()) &&
                !(clavesInOrden.size() == claveNoInOrden.size())){
            throw new RuntimeException("la listas deben tener el mismo tamaño");
        }

        if (esConPreOrden) {
            //recontruir utilizando pre orden
            K claveRaiz = claveNoInOrden.get(0);
            V valorRaiz = valoresNoInOrden.get(0);
            this.raiz = new NodoBinario<>(claveRaiz, valorRaiz);
            List<K> clavesPreIzquierda = claveNoInOrden.subList(1,clavesInOrden.indexOf(claveRaiz) + 1);
            List<K> clavesPreDerecha = claveNoInOrden.subList(clavesInOrden.indexOf(claveRaiz) + 1, claveNoInOrden.size());
            List<V> valoresPreIzquierda = valoresNoInOrden.subList(1, valoresInOrden.indexOf(valorRaiz) + 1);
            List<V> valoresPreDerecha = valoresNoInOrden.subList(valoresInOrden.indexOf(valorRaiz) + 1, valoresInOrden.size());

            List<K> clavesInIzquierda = claveNoInOrden.subList(0,clavesInOrden.indexOf(claveRaiz) );
            List<K> clavesInDerecha = claveNoInOrden.subList(clavesInOrden.indexOf(claveRaiz) + 1, claveNoInOrden.size());
            List<V> valoresInIzquierda = valoresNoInOrden.subList(0, valoresInOrden.indexOf(valorRaiz) );
            List<V> valoresInDerecha = valoresNoInOrden.subList(valoresInOrden.indexOf(valorRaiz) + 1, valoresInOrden.size());

            reconstruirPreOrden(clavesInIzquierda, valoresInIzquierda, clavesPreIzquierda, valoresPreIzquierda, this.raiz);
            reconstruirPreOrden(clavesInDerecha, valoresInDerecha, clavesPreDerecha, valoresPreDerecha, this.raiz);

        } else {
            //reconstruct utilization post orden
        }



    }

    private void reconstruirPreOrden(List<K> clavesInOrden, List<V> valoresInOrden,
                                     List<K> clavesPreOrden, List<V> valoresPreOrden,
                                     NodoBinario<K,V> nodoActual) {
        if (nodoActual.getClave().compareTo(clavesPreOrden.get(0)) > 0) {
            NodoBinario<K,V> nuevoNodoIzquierdo = new NodoBinario<>(clavesPreOrden.get(0), valoresPreOrden.get(0));
            nodoActual.setHijoIzquierdo(nuevoNodoIzquierdo);
            nodoActual = nuevoNodoIzquierdo;
        } else {
            NodoBinario<K,V> nuevoNodoDerecho = new NodoBinario<>(clavesPreOrden.get(0), valoresPreOrden.get(0));
            nodoActual.setHijoDerecho(nuevoNodoDerecho);
            nodoActual = nuevoNodoDerecho;
        }

        K clavePre = clavesPreOrden.get(0);
        V valorPre = valoresPreOrden.get(0);
        List<K> claveInIzq = clavesInOrden.subList(0, clavesInOrden.indexOf(clavePre)) ;
        List<V> valorInIzq = valoresInOrden.subList(0, valoresInOrden.indexOf(valorPre));
        List<K> clavePreIzq = clavesPreOrden.subList(1, claveInIzq.size() + 1);
        List<V> valorPreIzq = valoresInOrden.subList(1, valorInIzq.size() + 1);

        List<K> claveInDer = clavesInOrden.subList(clavesInOrden.indexOf(clavePre) + 1, clavesInOrden.size());
        List<V> valorInDer = valoresInOrden.subList(valoresInOrden.indexOf(valorPre) + 1, valoresInOrden.size());
        List<K> clavePreDer = clavesPreOrden.subList(claveInIzq.size() + 1, clavesPreOrden.size());
        List<V> valorPreDer = valoresPreOrden.subList(valorInIzq.size() + 1, valoresPreOrden.size());


        reconstruirPreOrden(claveInIzq, valorInIzq, clavePreIzq, valorPreIzq, nodoActual);
        reconstruirPreOrden(claveInDer, valorInDer, clavePreDer, valorPreDer, nodoActual);

    }

//revisar cuando estemos a detalle aqui
    public List<K> recorridoInOrdenIterativo() {
        List<K> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        NodoBinario<K,V> nodoActual = this.raiz;
        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();

        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }


        while (!pilaDeNodos.isEmpty()) {
            nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());


        }
        return recorrido;
    }



    public int sizeConRecorrido(){
        int cantidad = 0;
        if (this.esArbolVacio()){
            return cantidad;
        }

        Stack<NodoBinario<K,V>> pilaDeNodos = new Stack<>();
        NodoBinario<K,V> nodoActual = this.raiz;

        insertarEnPilaParaPostOrden(nodoActual, pilaDeNodos);

        while (!pilaDeNodos.isEmpty()){
            nodoActual = pilaDeNodos.pop();
            cantidad++;
            if (!pilaDeNodos.isEmpty()){
                NodoBinario<K,V> nodoDelTope = pilaDeNodos.peek();
                if (!nodoDelTope.esVacioHijoDerecho() &&
                        nodoDelTope.getHijoDerecho() != nodoActual){
                    insertarEnPilaParaPostOrden(nodoDelTope.getHijoDerecho(),
                            pilaDeNodos);
                }
            }
        }

        return cantidad;
    }






}
