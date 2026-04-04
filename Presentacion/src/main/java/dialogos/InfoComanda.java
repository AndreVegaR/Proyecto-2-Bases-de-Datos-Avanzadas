package dialogos;
import Coordinadores.CoordinadorNegocio;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.List;
import DTOs.DetallesComandaDTO;
import java.awt.Font;


/**
 * Diálogo que muestra en una tabla todos los productos de una comanda
 * Se usan DetallesProductoDTO
 * 
 * @author Andre
 */
public class InfoComanda extends JDialog {
    
    //Se instancia como atributo para usarlo en métodos fuera del constructor
    private JTable tabla;
    
    /**
     * Atributo en donde se almacena en memoria la lista de todos los productos
     * Esto para poder manipular estos registros (para obtener uno en específico por índice como ejemplo)
     * De esta forma no se tiene que llamar cada vez al BO y gastar recursos en consultas SQL
     * Mejor todo se consulta localmente
     */
    private List<DetallesComandaDTO> listaTemporal;
    
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
        tabla = UtilGeneral.crearTabla(camposTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        //De una vez llena la tabla para luego usar la listaTemporal
        llenarTabla();

        //Crea un panel para el total de la comanda
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new javax.swing.BoxLayout(panelInferior, javax.swing.BoxLayout.Y_AXIS));
        panelInferior.setBorder(new javax.swing.border.EmptyBorder(10, 25, 20, 25));
        
        //Contenido del resumen inferior
        double total = CoordinadorNegocio.getInstance().getComanda().getTotal();
        String folio = CoordinadorNegocio.getInstance().getComanda().getFolio();
        int productos = listaTemporal.size();
        
        //Crea los labels
        JLabel labelFolio = new JLabel("Folio: " + folio);
        labelFolio.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel labelTotal = new JLabel("Total de la comanda: $" + total);
        labelTotal.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel labelCantidad = new JLabel("Cantidad de productos: " + productos);
        labelCantidad.setFont(new Font("Arial", Font.BOLD, 14));
        
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
    
    
    /**
     * Llena la tabla con registros de cada cliente
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
        List<DetallesComandaDTO> detalles = CoordinadorNegocio.getInstance().consultarDetalles();
        listaTemporal = detalles;
        mapearTabla(); 
    }
    
    
    
    /**
     * Muestra los detalles de la comanda en la tabla directo de la BD
     */
    private void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, (DetallesComandaDTO d) -> new Object[]{
            d.getProducto().getNombre(),
            d.getCantidad(),
            d.getPrecioVenta(),
            d.getSubtotal()
        });
    }
}