package pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import formularios.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import formularios.ActualizarCliente;
import formularios.EliminarCliente;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import observadores.IObservador;

/**
 * Pantalla que muestra la tabla de clientes Observa al formulario de registro
 * de cliente
 *
 * @author Andre
 */
public class AdministrarClientes extends JFrame implements IObservador {
    
    //Se declaran como atributos para acceder a ellos en diferentes momentos<s
    private JTable tabla;

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

        JButton botonBuscarNombre = UtilBoton.crearBoton("Nombre");
        JButton botonBuscarTelefono = UtilBoton.crearBoton("Teléfono");
        JButton botonBuscarCorreo = UtilBoton.crearBoton("Correo");
        panelBusqueda.add(botonBuscarNombre);
        panelBusqueda.add(botonBuscarTelefono);
        panelBusqueda.add(botonBuscarCorreo);
        
        //Crea la tabla
        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo", "Fecha de registro", "Tipo"};
        tabla = UtilGeneral.crearTabla(columnas);

        //Agrega para scrollear
        JScrollPane scrollPane = new JScrollPane(tabla);

        //Evento que se activa cuando seleccionas una fila de la columna
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    //Obtiene la lista de todos los clientes
                    List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();

                    //El cliente seleccionado es el mismo del índice seleccionado de las filas
                    ClienteFrecuenteDTO clienteSeleccionado = lista.get(fila);

                    //El coordinador reconoce al cliente seleccionado para operaciones futuras
                    CoordinadorNegocio.getInstance().setClienteFrecuente(clienteSeleccionado);
                }
           }
         });
        
        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //ArrayList de suppliers que guarda los diálogos que se quieren abrir del CRUD
        //La lógica es la misma siempre, solo se cambian las clases que extienden de JDialog
        ArrayList<Supplier<? extends JDialog>> dialogos = new ArrayList<>();
        dialogos.add(() -> new RegistrarCliente(this, this)); 
        dialogos.add(() -> new ActualizarCliente(this, this));
        dialogos.add(() -> new EliminarCliente(this, this));
        
        //Crea un mapa con el esqueleto de un CRUD dibujado
        Map<String, JButton> mapaBotones = UtilBoton.dibujarBotonesCRUD(this, panelBotones);
        
        //Inyecta la lógica a los botones CRUD
        UtilBoton.inyectarLogicaCRUD(panelBotones, mapaBotones, dialogos);
        
        //Inyecta la lógica de refrescar la tabla al botón Refrescar
        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> {
            llenarTabla();
        });
        
        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        //Llena la tabla cada vez que se entre a la pantalla
        //llenarTabla();
    }
    
    
    
    /**
     * Llena la tabla con los registros Obtiene del coordinador todos los
     * registros de la BD Con el lambda CASTEA a la clase en concreto para
     * aplicar getters
     */
    public void llenarTabla() {
        List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();
        mapearTabla(lista);
    }

    
   
   /**
    * Método centralizado de una expresión lambda que llena n columnas
    */
   private void mapearTabla(List<ClienteFrecuenteDTO> lista) {
       UtilGeneral.registrarTabla(tabla, lista, c -> new Object[]{
           c.getId(),
           c.getNombres() + " " + c.getApellidoPaterno() + " " + c.getApellidoMaterno(),
           c.getTelefono(),
           (c.getCorreo() != null && !c.getCorreo().isEmpty()) ? c.getCorreo() : "No tiene",
           c.getFechaRegistro(),
           "Frecuente"
       });
   }
   
   
   
   /**
     * Método de la interfaz observador
     * Escucha el llamado el formulario y activa la lógica de registrar al
     * cliente en la tabla Hace una lista con el único elemento, esto para poder
     * trabajar bien con el método
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }
}