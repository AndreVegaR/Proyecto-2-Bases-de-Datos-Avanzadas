package Utilerias;
import Coordinadores.CoordinadorPantallas;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Utilerías para la lógica relacionada a los botones
 */
public class UtilBoton {
    //Clase anidada de un botón redondeado
    private static class BotonRedondeado extends JButton {
        public BotonRedondeado(String label) {
            super(label);
            //Esta configuración permite que el botón no llene todo su campo y se pueda hacer el efecto de borde redondeado
            setContentAreaFilled(false); 
            setOpaque(false);
            setBorderPainted(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            //Activa el suvaizado de bordes
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            
            //Define el arco de la curva
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            //Dibuja
            super.paintComponent(g2);
            g2.dispose();
        }
    }
    
    
    
    /**
     * Fábrica de un botón estilizado
     * 
     * @param texto que contendrá el botón
     * @return un botón ya estilizado
     */
    public static JButton crearBoton(String texto) {
        JButton boton = new BotonRedondeado(texto);
        
        //Configura la fuente del botón
        boton.setFont(Constantes.FUENTE);
        
        //Configura el color del botón
        boton.setBackground(Constantes.COLOR_BOTONES);
        
        //Configura el color del texto del botón
        boton.setForeground(Constantes.COLOR_TEXTO_BOTONES);
        
        //Elimina el contorno del texto al cliquear un botón
        boton.setFocusPainted(false);
        
        //Crea padding dentro
        boton.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        
        //Quita el borde normal y fondo por defecto
        boton.setContentAreaFilled(false);
        
        //Hace que al pasar el cursor se vuelva una manita
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Hace el efecto "hover" al pasar el ratón encima
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            //Cambia el color cuando pasa encima
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(Constantes.COLOR_BOTON_HOVER);
            }
            //Regesa al color original cuando se quita
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Constantes.COLOR_BOTONES);
            }
            public void mousePressed(java.awt.event.MouseEvent evt){
                boton.setBackground(Constantes.COLOR_BOTON_HOVER.darker());
            }
            public void mouseReleased(java.awt.event.MouseEvent evt){
                boton.setBackground(Constantes.COLOR_BOTON_HOVER);
            }
        });
        return boton;
    }
    
    
    
    /**
     * Fábrica de un botón para salir ya con funcionalidad
     * 
     * @return el botón fucional
     */
    public static JButton crearBotonSalir() {
        JButton boton = crearBoton("Salir");
        boton.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(boton, "¿Desea salir del sistema?", "Salir del sistema", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        return boton;
    }
    
    
    
    /**
     * Fábrica de un botón para navegar entre pantallas
     * 
     * @param texto del botón
     * @param ventanaActual
     * @param ventanaSiguiente
     * @return el botón funcional
     */
    public static JButton crearBotonNavegar(String texto, JFrame ventanaActual, Supplier<JFrame> ventanaSiguiente) {
        JButton boton = crearBoton(texto);
        boton.addActionListener(e -> {
            CoordinadorPantallas.getInstance().navegar(ventanaActual, ventanaSiguiente);
        });
        return boton;
    }
    
    
    
    /**
     * Fábrica de un botón que abre un formulario
     * 
     * @param texto del botón
     * @param formulario
     * @return el botón funcional
     */
    public static JButton crearBotonDialogo(String texto, Supplier<JDialog> formulario) {
        JButton boton = crearBoton(texto);
        boton.addActionListener(e -> {
            CoordinadorPantallas.getInstance().abrirFormulario(formulario);
        });
        return boton;
    }
}