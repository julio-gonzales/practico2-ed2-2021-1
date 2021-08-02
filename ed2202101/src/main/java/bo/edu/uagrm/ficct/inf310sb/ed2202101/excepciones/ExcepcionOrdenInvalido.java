package bo.edu.uagrm.ficct.inf310sb.ed2202101.excepciones;

public class ExcepcionOrdenInvalido extends Exception {

    public ExcepcionOrdenInvalido(){
        super("Orden del Arbol debe ser mayor a 3");
    }

    public  ExcepcionOrdenInvalido(String msg) {
        super(msg);
    }
}
