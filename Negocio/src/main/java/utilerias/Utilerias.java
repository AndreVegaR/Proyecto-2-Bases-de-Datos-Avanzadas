package utilerias;
import java.time.LocalDateTime;
import excepciones.NegocioException;

/**
 * Lógica repetida sobre decisiones de negocio
 * @author Andre
 */
public class Utilerias {
    //Mensajes centralizados
    private static String NULO = "El objeto es nulo";
    private static String VACIO = "no puede estar vacio";
    private static String POSITIVO = "debe ser un número positivo";
    
    /**
     * Determina si un objeto es nulo
     * 
     * @param o Objeto
     */
    public static void esNulo(Object o) {
        if (o == null) {
              throw new NegocioException(NULO);
        }
    }
    
    /**
     * Determina si una cadena está vacía
     * 
     * @param cadenaTexto
     * @param nombreCampo 
     */
    public static void esCadenadaVacia(String cadenaTexto, String nombreCampo){
        esNulo(cadenaTexto);
        if(cadenaTexto.trim().isEmpty()){
            throw new NegocioException(nombreCampo + " " + VACIO);
        }
    }
    
    /**
     * Determina si un número es mayor a 0
     * 
     * @param n
     * @param nombreCampo 
     */
    public static void esNumeroPositivo(Number n,String nombreCampo){
        esNulo(n);
        if(n.doubleValue()< 0){
            throw new NegocioException(nombreCampo + " " + POSITIVO);
        }
    }
    
    /**
     * Determina si una fecha es válida
     * 
     * @param fecha
     * @param nombreCampo 
     */
    public static void esFechaValida(LocalDateTime fecha, String nombreCampo){
        esNulo(fecha);
    }
    
    
    /**
     * Valida si el teléfono son 10 dígitos
     * @param telefono 
     */
    /**
     * public static void validarTelefono(String telefono) {
        if (!telefono.matches("\\d{10}")) {
             throw new NegocioException("Teléfono en formato inválido");
        }
    }
     */
}