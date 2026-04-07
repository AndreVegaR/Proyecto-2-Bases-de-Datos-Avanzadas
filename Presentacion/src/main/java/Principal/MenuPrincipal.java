package Principal;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import Pantallas.AdministrarComandas;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import Pantallas.AdministrarClientes;
import Pantallas.RegistrarComanda;
import Pantallas.AdministrarIngredientes;
import Pantallas.AdministrarProductos;

/**
 * Menú principal del sistema
 * Cambia la vista según el tipo de empleado ingresado
 * Puedes administrar productos, ingredientes, clientes y comandas
 * También generar reportes
 */
public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        
        //Reinicia referencias por si acaso
        CoordinadorNegocio.getInstance().setCliente(null);
        CoordinadorNegocio.getInstance().setMesa(null);
        CoordinadorNegocio.getInstance().setComanda(null);
        //CoordinadorNegocio.getInstance().setProducto(null);
        
        UtilGeneral.configurarFrame("Restaurante", this);

        //Panel de título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(40, 0, 0, 0));

        //Label del menú principal
        JLabel titleLabel = new JLabel("Menu principal", SwingConstants.CENTER);
        titleLabel.setFont(Constantes.FUENTE_TITULO);
        titleLabel.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titleLabel, BorderLayout.CENTER);

        //Panel de usuario
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelUsuario.setOpaque(false);
        JLabel labelHora = new JLabel();
        labelHora.setFont(Constantes.FUENTE);
        labelHora.setForeground(new Color(100,100,100));
        
        //Timer para ver la hora en tiempo reaql
        Timer timer = new Timer(1000, e -> {
            labelHora.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a")));
        });
        timer.start();
        
        //Añade al panel
        panelUsuario.add(new JLabel(new ImageIcon(getClass().getResource("/imagenes/iconUsuario.png"))));
        panelUsuario.add(labelHora);
        panelTitulo.add(panelUsuario, BorderLayout.EAST);

        //Panel central redondeado para que no se vea tan tosco
        JPanel panelCentral = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
                g2.dispose();
            }
        };
        panelCentral.setOpaque(false);
        
        //Marco de bordes
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBorder(new EmptyBorder(50, 100, 50, 100));

        //Agrega los botones según el rol del usuario
        if (CoordinadorPantallas.getInstance().esAdministrador()) {
            agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Administrar comandas", this, AdministrarComandas::new));
            agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Administrar productos", this, AdministrarProductos::new));
            agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Administrar ingredientes", this, AdministrarIngredientes::new));
            agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Administrar clientes", this, AdministrarClientes::new));
            agregarPanel(panelCentral, UtilBoton.crearBoton("Reporte de comandas"));
        } else {
            agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Registrar comanda", this, RegistrarComanda::new));
        }

        //Este botón siempre existe
        agregarPanel(panelCentral, UtilBoton.crearBotonNavegar("Regresar", this, MenuEmpleados::new));

        //Panel intermedio para contener, no debe ser visible
        JPanel panelIntermedio = new JPanel(new GridBagLayout());
        panelIntermedio.setOpaque(false);
        panelIntermedio.add(panelCentral);

        //Agrega todo al frame
        add(panelTitulo, BorderLayout.NORTH);
        add(panelIntermedio, BorderLayout.CENTER);
    }

    
    
    /**
     * Auxiliar para insertar un botón a un panel
     * Maneja la lógica de acomodo, centrando los botones
     * 
     * @param panel
     * @param boton 
     */
    private void agregarPanel(JPanel panel, JButton boton) {
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Ajusta el 320 para que los botones sean más o menos anchos
        boton.setMaximumSize(new Dimension(320, 50)); 
        panel.add(boton);
        panel.add(Box.createRigidArea(new Dimension(0, 15))); // Espacio vertical entre botones
    }
}