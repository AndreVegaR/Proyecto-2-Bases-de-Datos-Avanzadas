package pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import formularios.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import formularios.ActualizarCliente;
import formularios.EliminarCliente;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    //Se declaran como atributos para acceder a ellos en diferentes momentoss
    private JTable tabla;

    public AdministrarClientes() {

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
        
        
        
        
        
        //Crea paneles
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JPanel panelTabla = new JPanel(new BorderLayout());

        //String con los campos de la tabla
        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo", "Fecha de registro", "Tipo"};
        
        //Mapa para guardar los botones inferiores
        Map<String, JButton> mapaBotones = new HashMap<>();
        
        //ArrayList de suppliers que guarda los diálogos que se quieren abrir del CRUD
        //La lógica es la misma siempre, solo se cambian las clases que extienden de JDialog
        ArrayList<Supplier<? extends JDialog>> dialogos = new ArrayList<>();
        dialogos.add(() -> new RegistrarCliente(this, this)); 
        dialogos.add(() -> new ActualizarCliente(this, this));
        dialogos.add(() -> new EliminarCliente(this, this));
        
        
        
        
        /**
         * Este método crea y configura toda la pantalla de administrar x cosa
         * Llama a un método de UtilBuild al cual le pasas los datos previamente configurados
         * Regresa una pantalla poblada, funcional y fácilmente escalable
         * Sirve para el molde base: pantalla, CRUD, búsqueda
         * Regresa la tabla ya configurada donde van a aparecer los registros
         * Puedes acceder a los botones creados usando el mapa que fue llenado
         * Se le pueden agregar otros botones de forma fácil
         */
        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar clientes", //Título de la ventana
                                                              this, //Frame actual
                                                              panelBotones, //Panel de botones
                                                              panelTabla, //Panel de la tabla
                                                              columnas, //Campos que tendrá la tabla
                                                              mapaBotones, //Mapa con los botones
                                                              dialogos); //Lista con los diálogos a abrir
        
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
        
        
        
        //Inyecta la lógica de refrescar la tabla al botón Refrescar
        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> {
            llenarTabla();
        });
        
        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.SOUTH);
        add(panelTabla, BorderLayout.CENTER);
        
        
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