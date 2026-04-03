package Principal;
import Pantallas.AdministrarComandas;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import pantallas.AdministrarClientes;

/**
 * Dibuja el menú principal del sistema
 */
public class MenuPrincipal extends JFrame {

    public static Color COLOR_CUADRO = new Color(245, 247, 250);

    public MenuPrincipal() {
        UtilGeneral.configurarFrame("Restaurante", this);

        //Panel del título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(40, 0, 0, 0));

        //Título
        JLabel titleLabel = new JLabel("Menu principal", SwingConstants.CENTER);
        titleLabel.setFont(Constantes.FUENTE_TITULO);
        titleLabel.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titleLabel, BorderLayout.CENTER);
        
        
        //panel usuario 
        JPanel panelUsuario = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panelUsuario.setOpaque(false);
        JLabel iconoUsuario = new JLabel(
                new ImageIcon(getClass().getResource("/imagenes/iconUsuario.png")));
        
        JLabel labelHora = new JLabel();
        labelHora.setFont(Constantes.FUENTE);
        labelHora.setForeground(new Color(100,100,100));
        
        Timer timer = new Timer(1000,e ->{
            LocalTime horaActual = LocalTime.now();
            String textoHora = horaActual.format(DateTimeFormatter.ofPattern("hh:mm a"));
            labelHora.setText(textoHora);
    });
        timer.start();
        
        panelUsuario.add(iconoUsuario);
        panelUsuario.add(labelHora);
        
        panelTitulo.add(panelUsuario,BorderLayout.EAST);
        
        //Cuadro central
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(new GridLayout(3, 2, 25, 25));
        panelCentral.setBorder(new EmptyBorder(50, 80, 50, 80));

        //En caso de que el usuario sea administrador añade funciones extra
        if (UtilGeneral.admin) {
            //Crea botones
            JButton botonAdministrarComandas = UtilBoton.crearBotonNavegar("Administrar comandas", this, AdministrarComandas::new);
            JButton botonAdministrarProductos = UtilBoton.crearBoton("Administrar productos");
            JButton botonAdministrarIngredientes = UtilBoton.crearBoton("Administrar ingredientes");
            JButton administrarClientes = UtilBoton.crearBotonNavegar("Administrar clientes", this, AdministrarClientes::new);
            JButton botonReportesComandas = UtilBoton.crearBoton("Reporte de comandas");

            //Los agrega al panel central
            panelCentral.add(botonAdministrarComandas);
            panelCentral.add(botonAdministrarProductos);
            panelCentral.add(botonAdministrarIngredientes);
            panelCentral.add(administrarClientes);
            panelCentral.add(botonReportesComandas);
        }

        //Si es mesero
        if (!UtilGeneral.admin) {
            JButton botonRegistrarComanda = UtilBoton.crearBoton("Registrar comanda");
            panelCentral.add(botonRegistrarComanda);
        }

        //Siempre crea el botón de regresar
        JButton botonRegresar = UtilBoton.crearBotonNavegar("Regresar", this, MenuEmpleados::new);
        panelCentral.add(botonRegresar);

        //Panel intermedio para evitar que los botones se expandan de más
        JPanel panelIntermedio = new JPanel(new GridBagLayout());

        //Vuelve al panel invisible, pues solo contiene, no es visual
        panelIntermedio.setOpaque(false);

        //Le agrega el panel de botones para contenerlos
        panelIntermedio.add(panelCentral);

        //Agrega los otros dos paneles al panel intermedio
        add(panelTitulo, BorderLayout.NORTH);
        add(panelIntermedio, BorderLayout.CENTER);
    }
}
