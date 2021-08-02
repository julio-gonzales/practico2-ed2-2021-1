/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.uagrm.ficct.inf310sb.ed2202101.ui;

import bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles.AVL;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles.ArbolBinarioBusqueda;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles.ArbolMViasBusqueda;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.arboles.IArbolBusqueda;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionClaveNoExiste;
import bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones.ExcepcionOrdenInvalido;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 *
 * @author Hp 240
 */
public class TestArboles {
    public static void main(String argumentos[]) throws ExcepcionClaveNoExiste, ExcepcionOrdenInvalido {
        IArbolBusqueda<Integer,String> arbolBusqueda = new ArbolMViasBusqueda<>(4);
      /* Scanner entrada = new Scanner(System.in);
        System.out.print("Elija un tipo de arbol(ABB, AVL): ");
        String tipoArbol = entrada.next();
        tipoArbol = tipoArbol.toUpperCase();
        switch (tipoArbol){
            case "ABB":
                    arbolBusqueda = new ArbolBinarioBusqueda<>();
                break;
            case "AVL":
                    arbolBusqueda = new AVL<>();
                 break;
            default :
                break;
            
        }*/

        
        arbolBusqueda.insertar(50,"a");
        arbolBusqueda.insertar(60,"b");
        arbolBusqueda.insertar(58,"e");
        arbolBusqueda.insertar(45,"a");
        arbolBusqueda.insertar(12,"f");
        arbolBusqueda.insertar(66,"w");
        arbolBusqueda.insertar(13,"z");
        arbolBusqueda.insertar(46,"w");
        arbolBusqueda.insertar(83,"z");

        //System.out.println(((ArbolBinarioBusqueda)arbolBusqueda).hayHijosVaciosAntesDelNivel(3));
        System.out.println("recorrido por niveles " + arbolBusqueda.recorridoPorNiveles());
        System.out.println("recorrido por postOrden " + arbolBusqueda.recorridoEnPostOrden());
        System.out.println("recorrido por inOrden " + arbolBusqueda.recorridoEnInOrden());
        System.out.println("recorrido por preOrden " + arbolBusqueda.recorridoEnPreOrden());
        System.out.println("eliminado:   " + arbolBusqueda.eliminar(50));
        //arbolBusqueda.insertar(59,"z");
        System.out.println("eliminado:   " + arbolBusqueda.eliminar(60));
        System.out.println("eliminado:   " + arbolBusqueda.eliminar(83));
        System.out.println("eliminado" + arbolBusqueda.eliminar(58));
        System.out.println("eliminado" + arbolBusqueda.eliminar(46));
        System.out.println("recorrido por niveles " + arbolBusqueda.recorridoPorNiveles());
        System.out.println("recorrido por inOrden " + arbolBusqueda.recorridoEnInOrden());
        arbolBusqueda.insertar(59,"z");
        arbolBusqueda.insertar(79,"z");
        arbolBusqueda.insertar(99,"z");
        arbolBusqueda.insertar(49,"z");
        arbolBusqueda.insertar(21,"z");
        arbolBusqueda.insertar(33,"z");
        System.out.println("recorrido por niveles " + arbolBusqueda.recorridoPorNiveles());
        System.out.println("recorrido por inOrden " + arbolBusqueda.recorridoEnInOrden());
        List<Integer> listaCA = arbolBusqueda.recorridoEnInOrden();
        List<Integer> listaCB = arbolBusqueda.recorridoEnPostOrden();
        /*List<String> listaSA = new LinkedList<>();
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        listaSA.add("a");
        ArbolBinarioBusqueda<Integer,String> arbolB = new ArbolBinarioBusqueda<>(listaCA,listaSA, listaCB, listaSA, true);
        arbolB.recorridoEnInOrden();
        */


        Stack<Integer> pila = new Stack<>();
        int numero = pila.pop();
        System.out.println("el numero es :  " + numero);



    }
    
}
