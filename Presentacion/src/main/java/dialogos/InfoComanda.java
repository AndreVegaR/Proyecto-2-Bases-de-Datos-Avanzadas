package dialogos;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Component;


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
        
        //Crea la tabla y la agrega al panel
        String[] camposTabla = {"Producto", "Cantidad", "Precio unitario", "Subtotal"};
        JTable tabla = UtilGeneral.crearTabla(camposTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);

        //Crea un panel para el total de la comanda
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new javax.swing.BoxLayout(panelInferior, javax.swing.BoxLayout.Y_AXIS));
        panelInferior.setBorder(new javax.swing.border.EmptyBorder(10, 25, 20, 25));
        
        //Contenido del resumen inferior
        double total = 500.40;
        String folio = "SOY-UN-FOLIO";
        int productos = 10;
        
        //Crea los labels
        JLabel labelFolio = new JLabel("Folio: " + folio);
        JLabel labelTotal = new JLabel("Total de la comanda: $" + total);
        JLabel labelCantidad = new JLabel("Cantidad de productos: " + productos);
        
        //Ordena los labels
        labelFolio.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelCantidad.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        //Agrega al panel inferior
        panelInferior.add(labelFolio);
        panelInferior.add(labelTotal);
        panelInferior.add(labelCantidad);
        
        //Agrega al diálogo
        add(panelTabla, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.SOUTH);
        
        //Configuración final
        UtilGeneral.configurarDialogoFinal(this);
    }
}