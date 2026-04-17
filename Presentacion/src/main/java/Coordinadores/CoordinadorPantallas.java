package Coordinadores;
import java.util.function.Supplier;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * Clase que coordina el flujo entre pantallas
 * También guarda el tipo de client que ingresó: la lógica es mostrar u ocultar componentes gráficos
 */
public class CoordinadorPantallas implements ICoordinadorPantallas {
    
    //Strings estáticos y métodos para manejar el tipo de empleado de la sesión
    private static final String MESERO = "Mesero";
    private static final String ADMINISTRADOR = "Administrador";
    private static String tipoEmpleado = MESERO;
    @Override
    public void establecerMesero() {
        tipoEmpleado = MESERO;
    }
    @Override
    public void establecerAdministrador() {
        tipoEmpleado = ADMINISTRADOR;
    }
    @Override
    public boolean esAdministrador() {
        return tipoEmpleado.equals(ADMINISTRADOR);
    }
    
    
    
    //Única instancia
    private static CoordinadorPantallas instancia;
    
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
    public void abrirDialogo(Supplier<? extends JDialog> formulario) {
        JDialog dialogo = formulario.get();
        if(dialogo == null) return;
        dialogo.setVisible(true);
    }
   
    /**
    * Método encargado de navegar de un JFrame a un JDialog.
    * Cierra la ventana actual (JFrame) y abre un nuevo diálogo (JDialog).
    * 
    * @param actual ventana actual tipo JFrame
    * @param siguiente Supplier que crea el siguiente JDialog
    */
     public void navegarADialogo(JFrame actual, Supplier<JDialog> siguiente) {
       // Cerramos el JFrame actual para liberar recursos
       actual.dispose(); 
       // Creamos el dialog a partir del supplier
       JDialog nuevo = siguiente.get();
       // Centra el diálogo en la pantalla
       nuevo.setLocationRelativeTo(null);
       // Bloqueamos las otras ventanas
       nuevo.setModal(true); 
       // Hacemos visible el dialogo
       nuevo.setVisible(true);
   }
    
    /**
     * Método encargado de navegar de un JDialog a un JFrame.
     * Cierra el diálogo actual y abre una nueva ventana (JFrame).
     * 
     * @param actual diálogo actual
     * @param siguiente (Supplier) que crea el siguiente JFrame
     */
    public void navegarA_Frame(JDialog actual, Supplier<JFrame> siguiente) {
        // Cerramos el diálogo actual
        actual.dispose();
        // Creamos la nueva ventana
        JFrame nueva = siguiente.get();
        // Centramos la ventana en la pantalla
        nueva.setLocationRelativeTo(null);
        //La hacemos visible
        nueva.setVisible(true);
    }
    
    //Metodos para obtener el dialog actual y sirve para no perder informacion al momento de cambiar de pantallas en producto
    private JDialog dialogActual;

    public void setDialogActual(JDialog dialog) {
        this.dialogActual = dialog;
    }

    public JDialog getDialogActual() {
        return dialogActual;
    }
}