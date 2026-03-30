package Utilerias;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.Arrays;

/**
 * Clase con decisiones centralizadas (fuente, colores)
 * @author Andre
 */
public class Constantes {
    
    //Colores centralizados
    public static final Color COLOR_BOTONES = new Color(37, 99, 235);
    public static final Color COLOR_BOTON_HOVER = new Color(29, 78, 216);
    public static final Color COLOR_TEXTO_BOTONES = Color.WHITE;
    public static final Color COLOR_FONDO = Color.WHITE;
    public static final Color COLOR_TABLA = COLOR_BOTONES;
    
    //Fuente
    public static final Font FUENTE = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    public static final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 28);
    public static final Font FUENTE_BUSCADOR = FUENTE;
    
    //Arreglo con las opciones debajo de la tabla ya delimitadas
    public static final String[] OPCIONES_CRUD = {"Refrescar", "Registrar", "Actualizar", "Eliminar"};
    
    //Arreglo en minúsculas para manipular el mapa de los botones CRUD, siempre idéntico que OPCINES_CRUD
    public static final String[] OPCIONES_CRUD_MINUS = Arrays.stream(OPCIONES_CRUD)
                                                                .map(String::toLowerCase)
                                                                .toArray(String[]::new);
    
    //Cantidad de caracteres que tendrán los campos de texto
    public static final int NUM_CARACTERES = 20;
    
}