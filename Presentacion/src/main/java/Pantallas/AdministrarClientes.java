package pantallas;
import Utilerias.UtilBoton;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdministrarClientes extends JFrame {

    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JTextField txtBuscar;
    private JSpinner spnStockMinimo;

    public AdministrarClientes() {

        //Configuración básica
        setTitle("Administrar clientes");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        
        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Encabezado
        panelBusqueda.setBorder(BorderFactory.createTitledBorder("Búsqueda por"));
        
        //Búsqueda por nombre
        panelBusqueda.add(new JLabel("Nombre"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);
        
        //Búsqueda por teléfono
        panelBusqueda.add(new JLabel("Teléfono"));
        txtBuscar = new JTextField(10);
        panelBusqueda.add(txtBuscar);
        
        //Búsqueda por correo
        panelBusqueda.add(new JLabel("Correo"));
        txtBuscar = new JTextField(20);
        panelBusqueda.add(txtBuscar);
        
        //Arreglo con las partes de la tabla
        String[] columnas = {"Nombre completo", "Teléfono", "Correo", "Fecha de registro", "Tipo"};
        
        //Toma los valores del arreglo columnas, y despliega con 0 columnas iniciales (si no hay nada)
        modeloTabla = new DefaultTableModel(columnas, 0);
        
        //Crea la tabla y evita que sea arrastrable desde ejecución
        tablaClientes = new JTable(modeloTabla);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        
        //Agrega para scrollear
        JScrollPane scrollPane = new JScrollPane(tablaClientes);

        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //Crea los botones
        JButton botonAgregar = UtilBoton.crearBoton("Agregar cliente");
        JButton botonEliminar = UtilBoton.crearBoton("Eliminar cliente");
        JButton botonRegresar = UtilBoton.crearBoton("Regresar");

        //Agrega los botones
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRegresar);

        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }
}