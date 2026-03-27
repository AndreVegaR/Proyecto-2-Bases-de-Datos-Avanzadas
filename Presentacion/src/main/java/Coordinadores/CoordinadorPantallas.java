package Coordinadores;
import DTOs.ClienteFrecuenteDTO;
import java.util.function.Supplier;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Clase que coordina el flujo entre pantallas
 * También guarda el tipo de client que ingresó: la lógica es mostrar u ocultar componentes gráficos
 */
public class CoordinadorPantallas implements ICoordinadorPantallas {
    
    //Única instancia
    private static CoordinadorPantallas instancia;
    
    //Temporal para saber si el usuario es administrador o mesero
    private static boolean admin = false;
    
    //Constructor privado
    private CoordinadorPantallas() {}
    
    /**
     * Singleton
     * Si se llama por primera vez, crea la instancia
     * Si ya existía, solo la regresa
     * 
     * @return la instancia lista
     */
    public static CoordinadorPantallas getInstance() {
        if (instancia == null) {
            instancia = new CoordinadorPantallas();
        }
        return instancia;
    }
    
    /**
     * Centraliza la lógica de cambiar de una ventana a otras
     * El supplier hace una instancia rápida solo al hacer clic, sin ya tenerla hecha
     * 
     * @param ventanaActual
     * @param ventanaSiguiente 
     */
    @Override
    public void navegar(JFrame ventanaActual, Supplier<JFrame> ventanaSiguiente) {
        JFrame ventana = ventanaSiguiente.get();
        ventana.setVisible(true);
        ventanaActual.dispose();
    }
    
    /**
     * Abre un diálogo sin cerrar el frame que lo contiene
     * 
     * @param formulario
     */
    @Override
    public void abrirFormulario(Supplier<JDialog> formulario) {
        JDialog dialogo = formulario.get();
        dialogo.setVisible(true);
    }
    
    /**
     * Regresa el valor del atributo
     * 
     * @return true si es admin, false si no
     */
    @Override
    public boolean esAdmin() {
        return admin;
    }
    
    /**
     * Reconfigura el booleano
     * 
     * @param valor 
     */
    @Override
    public void setAdmin(boolean valor) {
        admin = valor;
    }
}