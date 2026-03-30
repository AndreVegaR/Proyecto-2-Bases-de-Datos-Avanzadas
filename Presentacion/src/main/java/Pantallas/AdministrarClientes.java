package pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import dialogos.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import dialogos.ActualizarCliente;
import dialogos.EliminarCliente;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import observadores.IObservador;

/**
 * Pantalla que muestra la tabla de clientes 
 * Observa los formularios mediante IObservador
 *
 * @author Andre
 */
public class AdministrarClientes extends JFrame implements IObservador {
    
    //Se instancia como atributo para usarlo en métodos fuera del constructor
    private JTable tabla;
    
    final int[] columnaActiva = {-1}; // -1 significa "todas las columnas"

    public AdministrarClientes() {

        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Arreglo con los textos de los diferentes botones
        String[] filtros = {"Nombre", "Teléfono", "Correo"};
        
        //Mapa vacío que será poblado con botones de filtrad opor un método posterior
        Map<String, JButton> botonesFiltros = new HashMap<>();
        
        //Crea paneles
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.add(new JLabel("Doble clic para desplegar información específica"));
        JPanel panelTabla = new JPanel(new BorderLayout());
        
        /**
         * Arreglo de Strings con los campos de la tabla
         * Será pasado a un próximo método para automatizar la creación de la tabla
         * Importante: debe coincidir con el orden del método mapearTabla
         */
        String[] columnas = {"ID", "Nombre", "Teléfono", "Correo", "Fecha de registro", "Tipo"};
        
        /**
         * Mapa para guardar los botones interiores
         * Es básicamente un diccionario de Python pero en Java
         * Este en específico tiene esta estructura llave-valor: el mensaje del botón y el botón
         * Se crea vacío para ser pasado a un próximo método que loe va a llenar
         * Una vez lleno, serán fácilmente accesibles
         */
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
         * Regresa la tabla para inyectarle la lógica de mouseClicked
         * Puedes acceder a los botones creados usando el mapa que fue llenado
         * Se le pueden agregar otros botones de forma fácil
         */
        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar clientes", //Título de la ventana
                                                              this, //Frame actual
                                                              panelBusqueda, //Panel de opciones de búsqueda
                                                              panelBotones, //Panel de botones
                                                              panelTabla,//Panel de la tabla
                                                              filtros, //Arreglo con filtros
                                                              botonesFiltros, //Mapa de botones que indican los filtros
                                                              columnas, //Campos que tendrá la tabla
                                                              mapaBotones, //Mapa con los botones
                                                              dialogos, //Lista con los JDialog a abrir
                                                              columnaActiva); //Arreglo que contiene el índice para filtrar
                                                              
        
        //Evento que se activa cuando seleccionas una fila de la columna
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                        /**
                         * //Obtiene la lista de todos los clientes
                        List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();

                        //El cliente seleccionado es el mismo del índice seleccionado de las filas
                        ClienteFrecuenteDTO clienteSeleccionado = lista.get(fila);

                        //El coordinador reconoce al cliente seleccionado para operaciones futuras
                        CoordinadorNegocio.getInstance().setClienteFrecuente(clienteSeleccionado);
                         * 
                         * 
                         */
                    }
                if (evt.getClickCount() >= 2) {
                    List<ClienteFrecuenteDTO> l = llenarTablaFalsa();
                    ClienteFrecuenteDTO cliente = l.get(fila);
                    mostrarInfoEspecifica(cliente);
                }
           }
         });
        
        //Inyecta la lógica de refrescar la tabla al botón Refrescar
        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> {
            llenarTablaFalsa();
        });

        //Llena la tabla cada vez que se entre a la pantalla
        llenarTablaFalsa();
    }
    
    
    
    /**
     * Llena la tabla con los registros Obtiene del coordinador todos los
     * registros de la BD Con el lambda CASTEA a la clase en concreto para
     * aplicar getters
     */
    public void llenarTabla() {
        //List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();
        //mapearTabla(lista);
        
    }
    
    
    public List<ClienteFrecuenteDTO> llenarTablaFalsa() {
        List<ClienteFrecuenteDTO> listaFalsa = new ArrayList<>();
        
        String[] nombres = {"Andre", "Angel", "Jazmin", "Maye", "Quiñones", "Domitsu"};
        
        for (String nombre: nombres) {
            ClienteFrecuenteDTO cliente = new ClienteFrecuenteDTO();
            cliente.setNombres(nombre);
            cliente.setApellidoPaterno("");
            cliente.setApellidoMaterno("");
            cliente.setTelefono("1234566");
            cliente.setCorreo(nombre + "@gmail.com");
            listaFalsa.add(cliente);
        }
        
        mapearTabla(listaFalsa);
        
        return listaFalsa;
    }
    

    
   
   
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
   
   
   
   private void mostrarInfoEspecifica(ClienteFrecuenteDTO dto) {
       
       String info = "Información adicional del cliente " + dto.getId() + ": \n"
                      + "Puntos de fidelidad: " + dto.getPuntosFidelidad() +  "\n"
                      + "Número de visitas: " + dto.getVisitas() + "\n"
                      + "Gasto total. " + dto.getGastoTotal() + "\n";
       
       UtilGeneral.dialogoAviso(this, info);
   }
   
   
   
   /**
     * Método de la IObservador
     * Escucha el llamado el formulario y activa la lógica de registrar al
     * cliente en la tabla Hace una lista con el único elemento, esto para poder
     * trabajar bien con el método
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }
    
    
    
    public void ejecutarAccion(int i) {
        UtilGeneral.dialogoSiNo(this, "Prueba: ¡Yo soy el índice " + i + "!");
    }
}