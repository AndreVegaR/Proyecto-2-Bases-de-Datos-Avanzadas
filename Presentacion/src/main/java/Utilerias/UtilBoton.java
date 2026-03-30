package Utilerias;
import Coordinadores.CoordinadorPantallas;
import Principal.MenuPrincipal;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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

        //Hace el efecto "hover" al pasar el ratón encima
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            //Cambia el color cuando pasa encima
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(Constantes.COLOR_BOTON_HOVER);
            }
            //Regesa al color original cuando se quita
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(Constantes.COLOR_BOTONES);
            }
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt){
                boton.setBackground(Constantes.COLOR_BOTON_HOVER.darker());
            }
            @Override
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
    public static JButton crearBotonDialogo(String texto, Supplier<? extends JDialog> formulario) {
        JButton boton = crearBoton(texto);
        boton.addActionListener(e -> {
            CoordinadorPantallas.getInstance().abrirDialogo(formulario);
        });
        return boton;
    }
    
    
    
    /**
     * Crea un mapa (diccionario) de tipo String, BotonPersonalizado
     * Así se rescatan los botones para ser usados después
     * Básicamente hace el esqueleto de botones para un CRUD
     * 
     * @param frame
     * @param panel
     * @return el mapa con los textos de los botones y el botón en sí
     */
    public static Map<String, JButton> dibujarBotonesCRUD(JFrame frame, JPanel panel) {
        Map<String, JButton> mapaBotones = new HashMap<>();
        
        //Crea un par llave, valor en el mapa según el texto del boton
        //Itera sobre un arreglo constante de las opciones que debe tener
        for (String opcion: Constantes.OPCIONES_CRUD) {
            BotonPersonalizado boton = (BotonPersonalizado) crearBoton(opcion);
            mapaBotones.put(opcion.toLowerCase(), boton);
            panel.add(boton);
        }
        
        //Botón para navegar
        JButton botonRegresar = crearBotonNavegar("Regresar", frame, MenuPrincipal::new);
        panel.add(botonRegresar);
        
        return mapaBotones;
    }
    
    
    
    /**
     * Se encarga de inyectar lógica en todo los botones CRUD
     * Itera sobre el tamaño de OPCIONES_CRUD_MINUS, obtiene lo necesario y lo pone en el mapa
     * Modifica el mapa del parámetro, por lo que la lista va a quedar con botones funcionales
     * 
     * @param panel
     * @param botones El mapa String, BotonPersonalizado que crea el método dibujarBotonesCRUD
     * @param dialogos Un arrayList de suppliers de JDialog en este orden: Registrar, Actualizar, Eliminar
     */
    public static void inyectarLogicaCRUD(JPanel panel, Map<String, JButton> botones, ArrayList<Supplier<? extends JDialog>> dialogos) {
        
        //Mapa actualizado de botones con lógica
        Map<String, JButton> botonesNuevos = new HashMap<>();
        
        //Rescata en una variable el arreglo de opciones para manejarlo fácilmente
        String[] opciones = Constantes.OPCIONES_CRUD_MINUS;
        
        //Itera sobre el arreglo de opciones. O sea, trabaja sobre los botones definidos
        for (int i = 0; i < opciones.length; i++) {
            
            //Crea un botón ya con lógica de abrir el formulario
            //Si es 0, se trata de refrescar y no debe tener esa lógica
            String opcion = opciones[i];
            if (i > 0){
   
                //El arreglo de suppliers solo tiene tres elementos, debe ser ajustado
                int indiceAjustado = i-1;
                
                //Guarda el botón actual del mapa de botones
                //Como opciones no tiene "Refresca", se ajusta el índice por desfase
                JButton boton = botones.get(opciones[indiceAjustado]);
                
                //Guarda el supplier actual
                Supplier<? extends JDialog> dialogo = dialogos.get(indiceAjustado);
                
                //Inyecta logica al botón
                boton.addActionListener(e -> {
                    CoordinadorPantallas.getInstance().abrirDialogo(dialogo);
                });
            }
        }
    }
    
    
    
    public void inyectarLogicaAdministrar(Map<String, JButton> botones) {
        
        
        //Rescata en una variable el arreglo de opciones para manejarlo fácilmente
        String[] opciones = Constantes.OPCIONES_CRUD_MINUS;
        
        
        
    }
    
}