package Coordinadores;
import java.util.function.Supplier;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Interfaz para los coordinadores de pantallas
 * @author Andre
 */
public interface ICoordinadorPantallas {
    
    //Métodos de empleados
    void establecerMesero();
    void establecerAdministrador();
    boolean esAdministrador();
    
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
    void abrirDialogo(Supplier<? extends JDialog> formulario);
}