package dialogos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ElegirMesa extends JDialog {

    public ElegirMesa(Frame padre) {
        super(padre, "Selección de Mesas", true);

        // 1. Definimos el tamaño fijo del diálogo
        int anchoVentana = 800;
        int altoVentana = 600;
        setSize(anchoVentana, altoVentana);
        setResizable(false); // Evita que el usuario cambie el tamaño

        // 2. Configuración de espaciado
        int espacioEntreBotones = 15; // Un poco más de espacio ya que es más grande
        int paddingMarco = espacioEntreBotones * 2;

        JPanel panelContenedor = new JPanel();
        
        // Al usar GridLayout, los botones se estirarán para llenar los 800x600 px
        panelContenedor.setLayout(new GridLayout(4, 5, espacioEntreBotones, espacioEntreBotones));
        panelContenedor.setBorder(new EmptyBorder(paddingMarco, paddingMarco, paddingMarco, paddingMarco));

        for (int i = 1; i <= 20; i++) {
            JButton boton = new JButton("Mesa " + i);
            
            // Aumentamos la fuente para que combine con el tamaño de los botones
            boton.setFont(new Font("Arial", Font.BOLD, 18));
            
            panelContenedor.add(boton);
        }

        add(panelContenedor);

        // Centrar en pantalla respecto al padre
        setLocationRelativeTo(padre);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    
    
    // Método main para probarlo rápidamente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ElegirMesa dialogo = new ElegirMesa(null);
            dialogo.setVisible(true);
        });
    }
}