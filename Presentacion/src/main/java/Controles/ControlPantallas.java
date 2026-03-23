package Controles;
import Principal.MenuEmpleados;
import Principal.MenuPrincipal;
import javax.swing.JFrame;

/**
 * Clase control que maneja la lógica del flujo del sistema, abriendo y cerrando pantallas
 */
public class ControlPantallas {
    

    /**
     * Abre el menú para decidir qué empleado es el usuario
     * Permite null porque puede ser la primera pantalla apenas inicia el sistema
     * 
     * @param ventanaAnterior a cerrar
     */
    public static void abrirMenuEmpleados(JFrame ventanaAnterior) {
        if (ventanaAnterior != null) {
            ventanaAnterior.dispose();
        }
        MenuEmpleados ventana = new MenuEmpleados();
        ventana.setVisible(true);
    }
    
    /**
     * Abre el menú principal del sistema
     * 
     * @param ventanaAnterior a cerrar
     */
    public static void abrirMenuPrincipal(JFrame ventanaAnterior) {
        ventanaAnterior.dispose();
        MenuPrincipal ventana = new MenuPrincipal();
        ventana.setVisible(true);
    }
}