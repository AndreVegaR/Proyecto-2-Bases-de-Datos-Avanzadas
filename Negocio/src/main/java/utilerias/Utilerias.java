package utilerias;
import java.time.LocalDateTime;
import excepciones.NegocioException;



/**
 * Lógica repetida sobre decisiones de negocio
 * @author Andre
 */
public class Utilerias {
    
    private static String NULO = "El objeto es nulo";
    private static String VACIO = "El campo no puede estar vacio";
    
    public static void esNulo(Object o) {
        if (o == null) {
              throw new NegocioException(NULO);
        }
    }
    public static void esCadenadaVacia(String cadenaTexto, String nombreCampo){
        esNulo(cadenaTexto);
        if(cadenaTexto.trim().isEmpty()){
            throw new NegocioException(nombreCampo + " " + VACIO);
        }
    }
    public static void esNumeroPositivo(Number n,String nombreCampo){
        esNulo(n);
        if(n.doubleValue()< 0){
            throw new NegocioException(nombreCampo + "debe ser un número positivo");
        }
    }
    public static void esFechaValida(LocalDateTime fecha, String nombreCampo){
        esNulo(fecha);
    }
}