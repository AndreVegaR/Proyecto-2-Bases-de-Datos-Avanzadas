package Coordinadores;
import java.util.function.Supplier;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Interfaz para los coordinadores de pantallas
 * @author Andre
 */
public interface ICoordinadorPantallas {
    
    /**
     * Cambia una pantalla a otra
     * 
     * @param ventanaActual
     * @param ventanaSiguiente 
     */
    void navegar(JFrame ventanaActual, Supplier<JFrame> ventanaSiguiente);
    
    /**
     * Abre un JDialog formulario
     * 
     * @param formulario a abrir
     */
    void abrirFormulario(Supplier<JDialog> formulario);
    
    /**
     * Determina si el usuario actual es administrador
     * 
     * @return true si es, false si no
     */
    boolean esAdmin();
    
    /**
     * Configura si es admin o no el usuario que ingresó
     * 
     * @param estado true si será empleado, false si será mesero
     */
    void setAdmin(boolean estado); 
}