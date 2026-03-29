package pantallas;
import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import formularios.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import formularios.ActualizarCliente;
import formularios.EliminarCliente;
import java.awt.*;
import javax.swing.*;
import java.util.List;
import observadores.IObservador;

/**
 * Pantalla que muestra la tabla de clientes Observa al formulario de registro
 * de cliente
 *
 * @author Andre
 */
public class AdministrarClientes extends JFrame implements IObservador {
    private JTable tabla;

    
    private JTextField textoBuscar;
    private java.util.Set<String> criteriosActivos = new java.util.HashSet<>();
    private JButton btnNombre, btnTelefono, btnCorreo;
    

    private ClienteFrecuenteDTO clienteSeleccionado;

    public AdministrarClientes() {
        UtilGeneral.configurarFrame("Administrar clientes", this);

        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));

        //Búsqueda por nombre
        panelBusqueda.add(new JLabel("Buscar"));
        JTextField textoBuscar = UtilGeneral.crearCampoTexto(20);
        textoBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textoBuscar.setPreferredSize(new Dimension(250, 35));
        textoBuscar.setToolTipText("Ingrese el término a buscar...");
        panelBusqueda.add(textoBuscar);


        criteriosActivos.add("Nombre");

        //Botones para cambiar el criterio de búsqueda
        btnNombre = UtilBoton.crearBoton("Nombre");
        btnTelefono = UtilBoton.crearBoton("Teléfono");
        btnCorreo = UtilBoton.crearBoton("Correo");

        //Interruptores de los botones
        btnNombre.addActionListener(e -> alternarCriterio("Nombre", btnNombre));
        btnTelefono.addActionListener(e -> alternarCriterio("Teléfono", btnTelefono));
        btnCorreo.addActionListener(e -> alternarCriterio("Correo", btnCorreo));

        panelBusqueda.add(btnNombre);
        panelBusqueda.add(btnTelefono);
        panelBusqueda.add(btnCorreo);
        
        
        actualizarEstiloBoton(btnNombre, true);

        //Buscador dinámico
        textoBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filtrar(); }
            private void filtrar() { ejecutarFiltrado(textoBuscar.getText()); }
        });

        

        JButton botonBuscarNombre = UtilBoton.crearBoton("Nombre");
        JButton botonBuscarTelefono = UtilBoton.crearBoton("Teléfono");
        JButton botonBuscarCorreo = UtilBoton.crearBoton("Correo");
        panelBusqueda.add(botonBuscarNombre);
        panelBusqueda.add(botonBuscarTelefono);
        panelBusqueda.add(botonBuscarCorreo);

        
        //Crea la tabla
        String[] columnas = {"ID", "Nombre", "Apellido Paterno", "Apellido Materno", "Teléfono", "Correo", "Fecha de registro", "Gasto total", "Puntos de fidelidad", "Visitas"};
        tabla = UtilGeneral.crearTabla(columnas);

        //Agrega para scrollear
        JScrollPane scrollPane = new JScrollPane(tabla);

       tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    // Asignamos clienteSeleccionado según la fila actual
                    List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();
                    clienteSeleccionado = lista.get(fila);

                    CoordinadorNegocio.getInstance().setClienteFrecuente(clienteSeleccionado);

                    // doble click para eliminar
                    if (evt.getClickCount() == 2) {
                        new EliminarCliente(AdministrarClientes.this, AdministrarClientes.this, clienteSeleccionado);
                    }
                }
            }
        });

        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //Crea los botones
        JButton botonAgregar = UtilBoton.crearBotonDialogo("Nuevo Cliente", () -> new RegistrarCliente(this, this));

        JButton botonEditar = UtilBoton.crearBotonDialogo("Editar cliente", () -> new ActualizarCliente(this, this));

        JButton botonEliminar = UtilBoton.crearBotonDialogo("Eliminar cliente", () -> {
           int fila = tabla.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un cliente primero",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            return new EliminarCliente(this, this, clienteSeleccionado);
        });
        JButton botonRegresar = UtilBoton.crearBotonNavegar("Regresar", this, MenuPrincipal::new);

        //Agrega los botones
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRegresar);

        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //Llena la tabla
        llenarTabla();
    }

    /**
     * Escucha el llamado el formulario y activa la lógica de registrar al
     * cliente en la tabla Hace una lista con el único elemento, esto para poder
     * trabajar bien con el método
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }

    /**
     * Llena la tabla con los registros Obtiene del coordinador todos los
     * registros de la BD Con el lambda CASTEA a la clase en concreto para
     * aplicar getters
     */
    public void llenarTabla() {
        List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();

        dibujarTabla(lista);

        UtilGeneral.registrarTabla(tabla, lista, (ClienteFrecuenteDTO c) -> new Object[]{
            c.getId(),
            c.getNombres(),
            c.getApellidoPaterno(),
            c.getApellidoMaterno(),
            c.getTelefono(),
            (c.getCorreo() != null && !c.getCorreo().isEmpty()) ? c.getCorreo() : "No tiene",
            c.getFechaRegistro(),
            c.getVisitas(),
            "$ " + String.format("%.2f", c.getGastoTotal() != null ? c.getGastoTotal() : 0.0),
            c.getPuntosFidelidad()
        });
    }
    
    
    
    /**
    * Activa o desactiva un criterio de búsqueda
    */
   private void alternarCriterio(String criterio, JButton boton) {
       if (criteriosActivos.contains(criterio)) {
           if (criteriosActivos.size() > 1) { // Evita que se quede sin ningún criterio
               criteriosActivos.remove(criterio);
               actualizarEstiloBoton(boton, false);
           }
       } else {
           criteriosActivos.add(criterio);
           actualizarEstiloBoton(boton, true);
       }
       ejecutarFiltrado(textoBuscar.getText()); // Refresca la tabla al cambiar el botón
   }

   
   
   /**
    * Cambia el color del botón si está activo o no
    * @param boton
    * @param activo 
    */
   private void actualizarEstiloBoton(JButton boton, boolean activo) {
       if (activo) {
           boton.setBackground(new Color(100, 150, 255)); // Azul suave (ajusta a tu gusto)
           boton.setForeground(Color.WHITE);
       } else {
           boton.setBackground(UIManager.getColor("Button.background"));
           boton.setForeground(Color.BLACK);
       }
   }

   
   
   /**
    * Filtra la lista de clientes según el texto y los criterios activos.
    */
   private void ejecutarFiltrado(String texto) {
       List<ClienteFrecuenteDTO> todos = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();

       if (texto.trim().isEmpty()) {
           dibujarTabla(todos);
           return;
       }

       String filtro = texto.toLowerCase();
       List<ClienteFrecuenteDTO> filtrados = todos.stream().filter(c -> {
           for (String criterio : criteriosActivos) {
               boolean coincide = switch (criterio) {
                   case "Nombre" -> (c.getNombres() + " " + c.getApellidoPaterno()).toLowerCase().contains(filtro);
                   case "Teléfono" -> c.getTelefono().contains(filtro);
                   case "Correo" -> (c.getCorreo() != null && c.getCorreo().toLowerCase().contains(filtro));
                   default -> false;
               };
               if (coincide) return true; // Si cumple uno, ya entra en la lista
           }
           return false;
       }).toList();

       dibujarTabla(filtrados);
   }

   
   
   /**
    * Método centralizado para dibujar los datos en la JTable.
    */
   private void dibujarTabla(List<ClienteFrecuenteDTO> lista) {
       UtilGeneral.registrarTabla(tabla, lista, c -> new Object[]{
           c.getId(),
           c.getNombres(),
           c.getApellidoPaterno(),
           c.getApellidoMaterno(),
           c.getTelefono(),
           (c.getCorreo() != null && !c.getCorreo().isEmpty()) ? c.getCorreo() : "No tiene",
           c.getFechaRegistro(),
           "$ " + c.getGastoTotal(),
           c.getPuntosFidelidad(),
           c.getVisitas()
       });
   }
}
