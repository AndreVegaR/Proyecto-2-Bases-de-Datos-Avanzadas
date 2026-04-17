package Utilerias;
import Coordinadores.CoordinadorPantallas;
import DTOs.MesaDTO;
import java.awt.Color;
import java.awt.Cursor;
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
    private static class BotonPersonalizado extends JButton {
        public BotonPersonalizado(String label) {
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
     * Encapsula su creación, así el botón personalizado se queda privado
     * Presentación solo sabe que recibe un JButton
     * 
     * @param texto que contendrá el botón
     * @return un botón ya estilizado
     */
    public static JButton crearBoton(String texto) {
        JButton boton = new BotonPersonalizado(texto);
        
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

        //Le asigna hover y regresa el botón
        asignarHoverBoton(boton, null);
        return boton;
    }
    
    
    
    /**
     * Asigna el comportamiento del hover (al pasar por un botón)
     * Depende de si es botón o mesa para que cambie de color según su identidad
     * Utiliza la mesaDTO para asignar el color dinámicamente
     * 
     * @param boton a asignar hover
     * @param mesa el DTO de la mesa
     */
    public static void asignarHoverBoton(JButton boton, MesaDTO mesa){
        Color h;
        //Se encarga de asignar color SOLO SI MESA NO ES NULL (o sea que es un botón representando una mesa)
        if (mesa!=null) {
            if (mesa.getEstadoMesa().equals(Constantes.ESTADO_INICIAL_MESA)) {
                h = Constantes.COLOR_MESA_DISPONIBLE;
            } else {
                h = Constantes.COLOR_MESA_OCUPADA;
            }
        } else {
            h = Constantes.COLOR_BOTON_HOVER;
        }
        Color color = boton.getBackground();
        final Color colorHover = h;
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            //Cambia el color cuando pasa encima
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorHover.darker());
            }
            //Regesa al color original cuando se quita
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(color);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                boton.setBackground(colorHover.darker());
            }
            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt){
                boton.setBackground(colorHover);
            }
        }); 
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
     * Método encargado de navegar de un JFrame a un JDialog.
     * Cierra la ventana principal (JFrame) y abre un diálogo (JDialog).
     * 
     * @param actual ventana actual tipo JFrame
     * @param siguiente Supplier que crea el siguiente JDialog
     */
    public static JButton navegarADialogo(String texto,JFrame actual, Supplier<JDialog> siguiente) {
       JButton boton = crearBoton(texto);
    
    boton.addActionListener(e -> {
        CoordinadorPantallas.getInstance().navegarADialogo(actual, siguiente);
    });
    
    return boton;
    }
    
    
    /**
     * Fábrica de un botón que abre un diálogo
     * 
     * @param texto del botón
     * @param dialogo
     * @return el botón funcional
     */
    public static JButton crearBotonDialogo(String texto, Supplier<? extends JDialog> dialogo) {
        JButton boton = crearBoton(texto);
        boton.addActionListener(e -> {
            CoordinadorPantallas.getInstance().abrirDialogo(dialogo);
        });
        return boton;
    }
}