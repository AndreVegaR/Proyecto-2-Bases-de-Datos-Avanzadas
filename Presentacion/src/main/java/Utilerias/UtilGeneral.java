package Utilerias;

import java.awt.Color;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Function;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Utilerías que guarda cosas que se ocupan en toda la presentación
 */
public class UtilGeneral {

    public static boolean admin = false;

    //Clase anidada de un campo de texto redondeado
    private static class CampoTextoRedondeado extends JTextField {

        public CampoTextoRedondeado(int tamaño) {
            super(tamaño);
            //Evita que dibuje el rectánglo completo
            setOpaque(false);
            //Padding
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            //Suavizado de bordes
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            //Relleno redondeado
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            //Dibuja todo y libera memoria
            super.paintComponent(g2);
            g2.dispose();
        }

        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            //Suavizado de bordes y color
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.GRAY);

            //Dibuja el borde
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
            g2.dispose();
        }
    }

    /**
     * Fábrica de una tabla ya estilizada
     *
     * @param columnas arreglo con los campos
     * @return la tabla ya lista
     */
    public static JTable crearTabla(String[] columnas) {
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tabla = new JTable(modeloTabla);

        //Evita que el usuario pueda arrastrar las columnas
        tabla.getTableHeader().setReorderingAllowed(false);

        //Espacio en las filas
        tabla.setRowHeight(30);

        //Configura colores
        tabla.setSelectionBackground(Constantes.COLOR_TABLA);
        tabla.setSelectionForeground(Color.WHITE);

        //Elimina bordes
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));

        //Encabezado
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(Constantes.COLOR_TABLA);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 35));

        return tabla;
    }

    /**
     * Configura el frame de donde se llame
     *
     * @param texto
     * @param frame
     */
    public static void configurarFrame(String texto, JFrame frame) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        frame.setTitle(texto);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(15, 15));
    }

    /**
     * Fábrica de un campo de texto redondeado
     *
     * @param tamanio
     * @return el campo redondeado
     */
    public static CampoTextoRedondeado crearCampoTexto(int tamanio) {
        return new CampoTextoRedondeado(tamanio);
    }

    /**
     * Crea un campo de texto ya configurado
     *
     * @param panel
     * @param etiqueta el label
     * @param tamanio
     */
    public static JTextField crearCampoFormulario(JPanel panel, String etiqueta, int tamanio) {
        CampoTextoRedondeado campo = crearCampoTexto(tamanio);
        JLabel label = new JLabel(etiqueta);

        //Configura de qué lado van a aparecer
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Agrega al panel y define pequeño espacio
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(campo);
        panel.add(Box.createVerticalStrut(15));

        return campo;
    }

    /**
     * Método que añade un registro a la tabla Utiliza el último guardado por el
     * coordinador
     *
     * @param <T> puede ser cualquier DTO
     * @param tabla
     * @param lista
     * @param mapeador el método lambda para que sea flexible con cualquier
     * clase
     */
    public static <T> void registrarTabla(JTable tabla, List<T> lista, Function<T, Object[]> mapeador) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        //Si la lista tiene más de un elemento, entonces es para cargar toda la tabla con registros ya existentes
        //Si solo tiene uno, es porque se va a ejecutar al registrar individualmente uno y no se deben limpiar los ya existentes
        modelo.setRowCount(0);

        //Itera para mapear
        if (lista != null) {
            for (T elemento : lista) {
                modelo.addRow(mapeador.apply(elemento));
            }
        }
    }

    //####Esto por ahora ni se usa xd pero seguramente será usado#####
    //Guarda el tipo de empleado con sesión activa
    public static TipoEmpleado tipoEmpleado;

    /**
     * Enumerador anidado que guardan los tipos de empleados
     */
    public static enum TipoEmpleado {
        ADMINISTRADOR,
        MESERO
    }

    /**
     * Asigna la sesión actual a un tipo de empleado
     *
     * @param tipo del empleado
     */
    public static void iniciarSesion(TipoEmpleado tipo) {
        tipoEmpleado = tipo;
    }

    /**
     * Cierra la sesión
     */
    public static void cerrarSesion() {
        tipoEmpleado = null;
    }

    /**
     * Determina si la sesión actual pertenece a un tipo de empleado
     *
     * @param tipo de empleado a determinar
     * @return si la sesión es de ese tipo
     */
    public static boolean es(TipoEmpleado tipo) {
        return tipoEmpleado == tipo;
    }
}
