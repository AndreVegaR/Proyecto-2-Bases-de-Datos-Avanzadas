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
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(50, 80, 50, 80));

        //Crea los botones
        JButton botonMesero = UtilBoton.crearBoton("Mesero");
        JButton botonAdministrador = UtilBoton.crearBoton("Administrador");

        JPanel panelMesero = crearPanelOpciones(
                "/imagenes/iconMesero.png",
                botonMesero
        );

        JPanel panelAdmin = crearPanelOpciones(
                "/imagenes/iconAdmin.png",
                botonAdministrador
        );

        //Añade los botones a su frame
        panelCentral.add(panelMesero);
        panelCentral.add(panelAdmin);

        //Panel intermedio para evitar que los botones se expandan de más
        JPanel panelIntermedio = new JPanel(new GridBagLayout());

        //Vuelve al panel invisible, pues solo contiene, no es visual
        panelIntermedio.setOpaque(false);
        panelIntermedio.add(panelCentral);

        JPanel panelSalir = new JPanel();
        panelSalir.setOpaque(false);
        JButton botonSalir = UtilBoton.crearBotonSalir();
        panelSalir.add(botonSalir);
        //Agrega los otros  paneles al panel intermedio
        add(panelTitulo, BorderLayout.NORTH);
        add(panelIntermedio, BorderLayout.CENTER);
        add(panelSalir, BorderLayout.SOUTH);

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

    private JPanel crearPanelOpciones(String rutaIcono, JButton boton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(300, 250));

        JLabel icono = new JLabel(new ImageIcon(getClass().getResource(rutaIcono)));
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(icono, BorderLayout.CENTER);
        panel.add(boton, BorderLayout.SOUTH);
        return panel;

    }
}
