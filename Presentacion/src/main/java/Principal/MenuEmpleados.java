package Principal;
import Coordinadores.CoordinadorPantallas;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import dialogos.ElegirMesa;
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
        titulo.setFont(Constantes.FUENTE_TITULO);
        titulo.setForeground(new Color(44, 62, 80));
        panelTitulo.add(titulo, BorderLayout.CENTER);

        //Cuadro central
        JPanel panelCentral = new JPanel();
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(50, 80, 50, 80));

        //Crea los botones
        JButton botonMesero = UtilBoton.crearBoton("Mesero");
        JButton botonAdministrador = UtilBoton.crearBoton("Administrador");

        //Crea el panel de mesero
        String rutaIcono = "/imagenes/iconMesero.png";
        JPanel panelMesero = crearPanelOpciones(rutaIcono, botonMesero);

        //Crea el panel de administrador
        rutaIcono = "/imagenes/iconAdmin.png";
        JPanel panelAdmin = crearPanelOpciones(rutaIcono, botonAdministrador);

        //Añade los botones a su frame
        panelCentral.add(panelMesero);
        panelCentral.add(panelAdmin);

        //Panel intermedio para evitar que los botones se expandan de más
        JPanel panelIntermedio = new JPanel(new GridBagLayout());

        //Vuelve al panel invisible, pues solo contiene, no es visual
        panelIntermedio.setOpaque(false);
        panelIntermedio.add(panelCentral);

        //Panel sur para que el botón de salir esté aislado
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
            CoordinadorPantallas.getInstance().establecerAdministrador();
            CoordinadorPantallas.getInstance().navegar(this, MenuPrincipal::new);
        });

        //Lógica del botón para acceder como mesero
        botonMesero.addActionListener(e -> {
            //temporal, no valida todavía
            /**
             * String pin = JOptionPane.showInputDialog(this,"Ingresa tu Pin de Mesero", "Acceso",
                    JOptionPane.QUESTION_MESSAGE);
            
             */
            CoordinadorPantallas.getInstance().establecerMesero();
            CoordinadorPantallas.getInstance().navegar(this, MenuPrincipal::new);
        });
    }

    
    
    /**
     * Crea un contenedor visual con botón e imagen
     * 
     * @param rutaIcono
     * @param boton
     * @return el panel
     */
    private JPanel crearPanelOpciones(String rutaIcono, JButton boton) {
        //Configura el panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constantes.COLOR_FONDO);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setPreferredSize(new Dimension(300, 250));

        //Configura el ícono
        JLabel icono = new JLabel(new ImageIcon(getClass().getResource(rutaIcono)));
        icono.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(icono, BorderLayout.CENTER);
        panel.add(boton, BorderLayout.SOUTH);
        return panel;
    }
}