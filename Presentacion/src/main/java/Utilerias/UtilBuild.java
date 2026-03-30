package Utilerias;
import Coordinadores.CoordinadorPantallas;
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
     * Se encarga de inyectar lógica en todo los botones CRUD
     * Itera sobre el tamaño de OPCIONES_CRUD_MINUS, obtiene lo necesario y lo pone en el mapa
     * Modifica el mapa del parámetro, por lo que la lista va a quedar con botones funcionales
     * 
     * @param panel
     * @param botones El mapa String, BotonPersonalizado que crea el método dibujarBotonesCRUD
     * @param dialogos Un arrayList de suppliers de JDialog en este orden: Registrar, Actualizar, Eliminar
     */
    public static void inyectarLogicaCRUD(JPanel panel, Map<String, JButton> botones, ArrayList<Supplier<? extends JDialog>> dialogos) {
        
        //Mapa actualizado de botones con lógica
        Map<String, JButton> botonesNuevos = new HashMap<>();
        
        //Rescata en una variable el arreglo de opciones para manejarlo fácilmente
        String[] opciones = Constantes.OPCIONES_CRUD_MINUS;
        
        //Itera sobre el arreglo de opciones. O sea, trabaja sobre los botones definidos
        for (int i = 0; i < opciones.length; i++) {
            
            //Crea un botón ya con lógica de abrir el formulario
            //Si es 0, se trata de refrescar y no debe tener esa lógica
            String opcion = opciones[i];
            if (i > 0){
   
                //El arreglo de suppliers solo tiene tres elementos, debe ser ajustado
                int indiceDialogo = i-1;
                
                //Guarda el botón actual del mapa de botones
                //Como opciones no tiene "Refresca", se ajusta el índice por desfase
                JButton boton = botones.get(opciones[i]);
                
                //Guarda el supplier actual
                Supplier<? extends JDialog> dialogo = dialogos.get(indiceDialogo);
                
                //Inyecta logica al botón
                boton.addActionListener(e -> {
                    CoordinadorPantallas.getInstance().abrirDialogo(dialogo);
                });
            }
        }
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
     * @param columnasTabla los campos de la tabla
     * @param botones el mapa con el juego texto del botón y el botón en sí mismo
     * @param dialogos una lista tipo Suplier con los diálogos que van a abrir
     * @return la tabla creada para inyectarle un mouseClicked por ejemplo
     */
    public static JTable ensamblarPantallaAdministrar(String tituloVentana,
                                                        JFrame frame, 
                                                        JPanel panelBusqueda,
                                                        JPanel panelBotones,
                                                        JPanel panelTabla,
                                                        String[] columnasTabla,
                                                        Map<String, JButton> botones,
                                                        ArrayList<Supplier<? extends JDialog>> dialogos) {
        
        //Configuración básica del frame
        UtilGeneral.configurarFrame(tituloVentana, frame);
        
        //Crea y configura la tabla
        JTable tabla = UtilGeneral.crearTabla(columnasTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        //Redibuja la tabla
        panelTabla.revalidate();
        panelTabla.repaint();
        
        //Crea el campo para CRUD
        botones.putAll(dibujarBotonesCRUD(frame, panelBotones));
        inyectarLogicaCRUD(panelBotones, botones, dialogos);
        
        //Agrega los paneles al frame
        frame.add(panelBusqueda, BorderLayout.NORTH);
        frame.add(panelTabla, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);
        
        //Regresa la tabla para trabajar después con ella
        return tabla;
    }
}