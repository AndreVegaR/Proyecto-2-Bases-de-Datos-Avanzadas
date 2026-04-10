package Utilerias;
import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;
import mappers.MesaMapper;

/**
 * Clase con decisiones centralizadas (fuente, colores)
 * @author Andre
 */
public class Constantes {
    
    public static final boolean TEST_MODE = true;
    
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
    
    /**
     * Arreglo con los tipos de clientes
     * Esto es útil para el método fábrica del módulo de clientes
     * Que regresa un cliente buscando aquí la opción de un combobox de clientes
     */
    public static final String[] TIPOS_CLIENTES = {"Frecuente"};
    
    //Cantidad establecida de mesas en el restaurante
    public static final int NUMERO_MESAS = 20;
    
    //El estado inicial de la mesa. Siempre coordinado con el establecido en el mapper
    public static final String ESTADO_INICIAL_MESA = MesaMapper.getEstadoInicial();
    public static final String ESTADO_MESA_OCUPADA = "Ocupada";
    
    //Colores para los estados de la mesa
    public static final Color COLOR_MESA_DISPONIBLE = new Color(165, 214, 167);
    public static final Color COLOR_MESA_OCUPADA   = new Color(239, 154, 154);
    
    /**
     * Arreglo con los campos a mostrar de los detalles de la comanda
     * Es una constante ubicada aquí debido a que se usa en más de un lugar
     * Si cambia aquí, cambia en todos los lugares implementados
     */
    public static final String[] CAMPOS_TABLA_DETALLES = {"Producto", "Cantidad", "Precio unitario", "Subtotal"};
    
    //Color de cuando una fila de la tabla está seleccionada
    public static final Color COLOR_ELEGIR_FILA = new Color(210, 214, 218);
    
    public static final String SIN_CLIENTE = "Cliente general";
}