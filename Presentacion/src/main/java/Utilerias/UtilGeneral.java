package Utilerias;
import java.awt.Color;

/**
 * Utilerías que guarda cosas que se ocupan en toda la presentación
 */
public class UtilGeneral {
    
    //Variable todavía más temporal jaja
    public static boolean admin = false;
    
    //Colores centralizados
    public static Color COLOR_BOTONES = new Color(37, 99, 235);
    public static Color COLOR_BOTON_HOVER = new Color(29, 78, 216);
    public static Color COLOR_TEXTO_BOTONES = Color.WHITE;
    public static Color COLOR_FONDO = Color.WHITE;
    public static String FUENTE = "SansSerif";
    
    
    //#####LO SIGUIENTE ES UNA SOLUCIÓN TEMPORAL!!!#####
    //####Y por ahora ni se usa xd pero seguramente será empleado#####
    //Guarda el tipo de empleado con sesión activa
    public static TipoEmpleado tipoEmpleado;
    
    /**
     * Enumerador anidado que guardan los tipos de empleados
     */
    public static enum TipoEmpleado {
        ADMINISTRADOR,
        MESERO
    }
    
    /**
     * Asigna la sesión actual a un tipo de empleado
     * @param tipo del empleado
     */
    public static void iniciarSesion(TipoEmpleado tipo) {
        tipoEmpleado = tipo;
    }
    
    /**
     * Cierra la sesión
     */
    public static void cerrarSesion() {
        tipoEmpleado = null;
    }
    
    /**
     * Determina si la sesión actual pertenece a un tipo de empleado
     * @param tipo de empleado a determinar
     * @return si la sesión es de ese tipo
     */
    public static boolean es(TipoEmpleado tipo) {
        return tipoEmpleado == tipo;
    }
}