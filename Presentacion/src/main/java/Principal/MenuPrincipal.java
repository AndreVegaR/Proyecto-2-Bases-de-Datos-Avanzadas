package Principal;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dibuja el menú principal del sistema
 */
public class MenuPrincipal extends JFrame {
    public static Color COLOR_CUADRO = new Color(245, 247, 250);
    
    public MenuPrincipal() {
        //Configuración de la ventana
        setTitle("Restaurante");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(COLOR_CUADRO);
        setLayout(new BorderLayout());
        
        //Panel del título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(40, 0, 0, 0));

        //Título
        JLabel titleLabel = new JLabel("Menu principal", SwingConstants.CENTER);
        titleLabel.setFont(new Font(UtilGeneral.FUENTE, Font.PLAIN, 42));
        titleLabel.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titleLabel, BorderLayout.CENTER);

        //Cuadro central
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(new GridLayout(3, 2, 25, 25));
        panelCentral.setBorder(new EmptyBorder(50, 80, 50, 80));
        
        //En caso de que el usuario sea administrador añade funciones extra
        if (UtilGeneral.admin) {
            //Crea botones
            JButton botonAdministrarProductos = UtilBoton.crearBoton("Administrar productos");
            JButton botonAdministrarIngredientes = UtilBoton.crearBoton("Administrar ingredientes");
            JButton botonClientesFrecuentes = UtilBoton.crearBoton("Clientes frecuentes");
            JButton botonReportesComandas = UtilBoton.crearBoton("Reporte de comandas");
            
            //Los agrega al panel central
            panelCentral.add(botonAdministrarProductos);
            panelCentral.add(botonAdministrarIngredientes);
            panelCentral.add(botonClientesFrecuentes);
            panelCentral.add(botonReportesComandas);
        }
        
        //Siempre crea los botones de comandas y el de salir
        JButton botonAdministrarComandas = UtilBoton.crearBoton("Administrar comandas");
        JButton botonSalir = UtilBoton.crearBotonSalir();
        panelCentral.add(botonAdministrarComandas);
        panelCentral.add(botonSalir);
 
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