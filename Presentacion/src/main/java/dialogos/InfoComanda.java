package dialogos;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 *
 * @author Andre
 */
public class InfoComanda extends JDialog {
    
    public InfoComanda(JFrame frame) {
        super(frame, "Información de la comanda", true);
        
        //Configuración inicial
        UtilGeneral.configurarDialogoInicio(this, false);
        
        //Crea el panel de la tabla
        JPanel panelTabla = new JPanel();
        panelTabla.setLayout(new javax.swing.BoxLayout(panelTabla, javax.swing.BoxLayout.Y_AXIS));
        panelTabla.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));
        
        //Crea la tabla
        String[] camposTabla = {"Producto", "Cantidad", "Precio unitario", "Subtotal"};
        JTable tabla = UtilGeneral.crearTabla(camposTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        
        //Agrega la tabla
        panelTabla.add(scroll, BorderLayout.CENTER);

        //Agrega al diálogo
        add(panelTabla, BorderLayout.NORTH);
        
        //Configuración final
        UtilGeneral.configurarDialogoFinal(this);
    }
}