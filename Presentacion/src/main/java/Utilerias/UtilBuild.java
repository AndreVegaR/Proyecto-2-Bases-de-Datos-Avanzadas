package Utilerias;
import Principal.MenuPrincipal;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 * Utilerías que implementa el patrón Builder:
 * Una clase con métodos que construyen parts del programa y evitar repetir código
 * Es como una linea de ensamblaje con un plano que solo debe replicarr
 * @author Andre
 */
public class UtilBuild {
    
    /**
     * Crea un mapa (diccionario) de tipo String, BotonPersonalizado
     * Así se rescatan los botones para ser usados después
     * Básicamente hace el esqueleto de botones para un CRUD
     * 
     * @param frame
     * @param panel
     * @return el mapa con los textos de los botones y el botón en sí
     */
    public static Map<String, JButton> dibujarBotonesCRUD(JFrame frame, JPanel panel) {
        Map<String, JButton> mapaBotones = new HashMap<>();
        
        //Crea un par llave, valor en el mapa según el texto del boton
        //Itera sobre un arreglo constante de las opciones que debe tener
        for (String opcion: Constantes.OPCIONES_CRUD) {
            JButton boton = UtilBoton.crearBoton(opcion);
            mapaBotones.put(opcion.toLowerCase(), boton);
            panel.add(boton);
        }
        
        //Botón para navegar
        JButton botonRegresar = UtilBoton.crearBotonNavegar("Regresar", frame, MenuPrincipal::new);
        panel.add(botonRegresar);
        
        return mapaBotones;
    }
    
    
    
    /**
     * Dibuja la parte del campo de búsqueda y los botones según el filtro
     * Llena el mapa del parámetro para ser usado después
     * 
     * @param panelBusqueda
     * @param filtros en un arreglo de Strings
     * @param botonesFiltros en un mapa que vacío que será poblado con los botones
     * @return 
     */
    public static JTextField dibujarCampoBusqueda(JPanel panelBusqueda, String[] filtros, Map<String, JButton> botonesFiltros) {
        
        //Crea el campo de búsqueda
        JTextField campoBuscar = UtilGeneral.crearCampoFormulario(panelBusqueda, "Buscar", Constantes.NUM_CARACTERES);
        campoBuscar.setToolTipText("Ingrese el término a buscar");
        panelBusqueda.add(campoBuscar);
        
        //Crea los botones y los agrega al mapa
        for (String filtro: filtros) {
             JButton boton = UtilBoton.crearBoton(filtro);
             panelBusqueda.add(boton);
             botonesFiltros.put(filtro.toLowerCase(), boton);
        }
        return campoBuscar;
    }
    
    
    
    /**
     * Método que se encarga de ensamblar toda una pantalla para administrar cierto objeto
     * Configura el frame, crea botones, configura paneles, crea la tabla
     * Recibe diferentes parámetros necesarios para esto
     * También llama a diferentes métodos menores, orquestándolos en una gran línea de ensamblaje
     * No crea los paneles porque podrías querer volverlos a usar
     * 
     * @param tituloVentana
     * @param frame donde se va a mostrar
     * @param panelBusqueda
     * @param panelBotones
     * @param panelTabla
     * @param filtros Arreglo de Strings con el texto de los botones para filtrar
     * @param columnasTabla los campos de la tabla
     * @param botonesFiltros el mapa con botones de filtrado
     * @param botonesCRUD el mapa con el juego texto del botón y el botón en sí mismo
     * @param dialogos una lista tipo Suplier con los diálogos que van a abrir
     * @param columnaActiva arreglo donde se guarda el índice de la tabla a fitrar
     * @return la tabla creada para inyectarle un mouseClicked por ejemplo
     */
    public static JTable ensamblarPantallaAdministrar(String tituloVentana,
                                                        JFrame frame, 
                                                        JPanel panelBusqueda,
                                                        JPanel panelBotones,
                                                        JPanel panelTabla,
                                                        String[] filtros,
                                                        Map<String, JButton> botonesFiltros,
                                                        String[] columnasTabla,
                                                        Map<String, JButton> botonesCRUD,
                                                        ArrayList<Supplier<? extends JDialog>> dialogos,
                                                        int[] columnaActiva) {
        
        //Configuración básica del frame
        UtilGeneral.configurarFrame(tituloVentana, frame);
        
        //Crea el campo de búsqueda y sus botones
        JTextField campoBusqueda = dibujarCampoBusqueda(panelBusqueda, filtros, botonesFiltros);
        
        //Crea y configura la tabla
        JTable tabla = UtilGeneral.crearTabla(columnasTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        //Redibuja la tabla
        panelTabla.revalidate();
        panelTabla.repaint();
        
        //Configura el sorter para el modelo de la tabla
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelo);
        tabla.setRowSorter(sorter);
        
        //Inyecta la lógica al campo de búsqueda
        UtilLogica.inyectarLogicaBusqueda(campoBusqueda, filtros, columnasTabla, botonesFiltros, sorter, columnaActiva);
        
        //Crea el campo para CRUD
        botonesCRUD.putAll(dibujarBotonesCRUD(frame, panelBotones));
        UtilLogica.inyectarLogicaCRUD(panelBotones, botonesCRUD, dialogos);
        
        //Agrega los paneles al frame
        frame.add(panelBusqueda, BorderLayout.NORTH);
        frame.add(panelTabla, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
        
        //Regresa la tabla para trabajar después con ella
        return tabla;
    }
}