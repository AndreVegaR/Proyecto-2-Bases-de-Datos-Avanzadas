package Controles;
import java.util.function.Supplier;
import javax.swing.JFrame;

/**
 * Clase control que maneja la lógica del flujo del sistema, abriendo y cerrando pantallas
 */
public class ControlPantallas {

    /**
     * Centraliza la lógica de cambiar de una ventana a otras
     * El supplier hace una instancia rápida solo al hacer clic, sin ya tenerla hecha
     * 
     * @param ventanaActual
     * @param ventanaSiguiente 
     */
    public static void navegar(JFrame ventanaActual, Supplier<JFrame> ventanaSiguiente) {
        ventanaActual.dispose();
        JFrame ventana = ventanaSiguiente.get();
        ventana.setVisible(true);
    }
}