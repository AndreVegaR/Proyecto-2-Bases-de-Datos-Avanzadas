package Coordinadores;
import Coordinadores.CoordinadorPantallas.RolUsuario;
import DTOs.ClienteFrecuenteDTO;
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
    void abrirDialogo(Supplier<? extends JDialog> formulario);
    
    /**
     * Regresa el rol del usuario con la sesión actual
     * 
     * @return el valor del enumerador RolUsuario
     */
    RolUsuario getRolUsuario();
}