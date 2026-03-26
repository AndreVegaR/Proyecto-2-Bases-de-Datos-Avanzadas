package Principal;
import Controles.ControlPantallas;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Dibuja el menú de empleados
 */
public class MenuEmpleados extends JFrame {
    public static Color COLOR_CUADRO = new Color(245, 247, 250);
    
    public MenuEmpleados() {
        UtilGeneral.configurarFrame("Menú empleados", this);

        //Cada vez que se entra a este menú es como cerrar sesión de administrador
        UtilGeneral.admin = false;
        
        //Panel del título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setOpaque(false);
        panelTitulo.setBorder(new EmptyBorder(40, 0, 30, 0));

        //Título
        JLabel titulo = new JLabel("Menú de empleados", SwingConstants.CENTER);
        titulo.setFont(Constantes.FUENTE);
        titulo.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titulo, BorderLayout.CENTER);
        
        //Cuadro central
        JPanel panelCentral = new JPanel();
        panelCentral.setBackground(Color.WHITE);
        panelCentral.setLayout(new GridLayout(3, 2, 25, 25));
        panelCentral.setBorder(new EmptyBorder(50, 80, 50, 80));
        panelCentral.setPreferredSize(new Dimension(550, 450));
        
        //Crea los botones
        JButton botonAdministrador = UtilBoton.crearBoton("Administrador");
        JButton botonMesero = UtilBoton.crearBoton("Mesero");
        JButton botonSalir = UtilBoton.crearBotonSalir();
        
        //Añade los botones a su frame
        panelCentral.add(botonAdministrador);
        panelCentral.add(botonMesero);
        panelCentral.add(botonSalir);
        
        //Panel intermedio para evitar que los botones se expandan de más
        JPanel panelIntermedio = new JPanel(new GridBagLayout());
        
        //Vuelve al panel invisible, pues solo contiene, no es visual
        panelIntermedio.setOpaque(false);
        
        //Le agrega el panel de botones para contenerlos
        GridBagConstraints gbc = new GridBagConstraints();
        panelIntermedio.add(panelCentral, gbc);
        
        //Agrega los otros dos paneles al panel intermedio
        add(panelTitulo, BorderLayout.NORTH);
        add(panelIntermedio, BorderLayout.CENTER);
        
        //Lógica del botón para acceder como administrador
        botonAdministrador.addActionListener(e -> {
            UtilGeneral.admin = true;
            ControlPantallas.navegar(this, MenuPrincipal::new);
        });
        
        //Lógica del botón para acceder como mesero
        botonMesero.addActionListener(e -> {
            ControlPantallas.navegar(this, MenuPrincipal::new);
        });
    }
}