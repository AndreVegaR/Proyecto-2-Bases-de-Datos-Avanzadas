package Pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteFrecuenteDTO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.MesaDTO;
import DTOs.ProductoDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import Utilerias.UtilLogica;
import dialogos.ActualizarCliente;
import dialogos.EliminarCliente;
import dialogos.InfoComanda;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import observadores.IObservador;

/**
 * Pantalla que muestra todas las comandas registradas
 *
 * @author Andre
 */

//ESTE OBSERVADOR ES TEMPORAL PUEDA QUE SE VAYA O NO YA DEPENDE
public class AdministrarComandas extends JFrame implements IObservador {
    
    public static final boolean TEST_MODE = false;
    
    //Se instancia como atributo para usarlo en métodos fuera del constructor
    private JTable tabla;
    
    /**
     * Arreglo que contiene solo un elemento: el índice del botón que filtra en la búsqueda
     * Se implementa así porque los addActionListeners usan variables final
     * Como una variable final no puede cambiar, se guarda en un arreglo
     * Entonces el arreglo en sí nunca cambia en sí; solo cambia su contenido
     * Este movimiento no es ilegal para Java y permite el flujo perfectamente
     */
    final int[] columnaActiva = {-1};
    
    /**
     * Atributo en donde se almacena en memoria la lista de todos las comandas
     * Esto para poder manipular estos registros (para obtener uno en específico por índice como ejemplo)
     * De esta forma no se tiene que llamar cada vez al BO y gastar recursos en consultas SQL
     * Mejor todo se consulta localmente
     */
    private List<ComandaDTO> listaTemporal = new ArrayList<>();

    public AdministrarComandas() {

        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Arreglo con los botones que permitirán filtrar según su campo de la tabla
        String[] filtros = {"Total", "Mesa", "Estado"};
        
        //Mapa vacío que será poblado con botones de filtrad opor un método posterior
        Map<String, JButton> botonesFiltros = new HashMap<>();
        
        //Crea paneles
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.add(new JLabel("Doble clic para desplegar información específica"));
        JPanel panelTabla = new JPanel(new BorderLayout());
        
        //Crea y agrega el botón encargado de generar reportes
        JButton botonReportes = UtilBoton.crearBoton("Generar reporte");
        panelBotones.add(botonReportes);
        botonReportes.addActionListener(e -> {
            this.dispose();
            new ReporteComandas().setVisible(true);
        });
        
        /**
         * Arreglo de Strings con los campos de la tabla
         * Será pasado a un próximo método para automatizar la creación de la tabla
         * Importante: debe coincidir con el orden del método mapearTabla
         */
        String[] columnas = {"ID", "Total", "Mesa", "Estado", "Folio"};
        
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
        dialogos.add(() -> new InfoComanda(this)); 
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
        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar comandas", //Título de la ventana
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

        /**
         * Estas dos líneas se encargan de desaparecer de la interfaz botones del CRUD
         * El método ensamblarPantallaAdministrar asume todo el CRUD, pero a veces no se ocupa
         * Un administrador no registra comandas, y las comandas no pueden ser eliminadas
         * Entonces el método esconderBotones se encarga de extirparlos:
         * -Busca coincidencias entre el arreglo y el mapa de botones
         * -Remueve del panel cada coincidencia
         * -Al final recarga el panel
         */
        String[] botonesEliminar = {"registrar", "eliminar"};
        UtilLogica.esconderBotones(panelBotones, mapaBotones, botonesEliminar);
        
        //Evento que se activa cuando seleccionas una fila de la columna
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                //Crea el cliente desde aquí para usarlo fuera del if
                ComandaDTO comanda = null;
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    
                    /**
                     * Esto mantiene siempre el índice real de los registros
                     * Por ejemplo, si selecciono el primer registro de una tabla filtrada, y sabe
                     * que se trata en realidad de otro índice real en la lista
                     */
                    int indiceReal = tabla.convertRowIndexToModel(fila);
                    
                    //Obtiene el cliente en dicho índice y lo manda al método que muestra su info
                    comanda = listaTemporal.get(indiceReal);
                    
                    //Asigna el cliente al coordinador para que sea usado
                    CoordinadorNegocio.getInstance().setComanda(comanda);
                    
                    /**
                    * Si se cliquea dos veces, el coordinador abre el diálogo
                    * Ese diálogo muestra una tabla con los productos de la comanda
                    */
                   if (evt.getClickCount() == 2) {
                        //Se hace una final solo para usarla dentro de este lambda
                        final ComandaDTO comandaLambda = comanda;
                        CoordinadorNegocio.getInstance().setComanda(comanda);
                        CoordinadorPantallas.getInstance().abrirDialogo(() -> {
                             CoordinadorNegocio.getInstance().setComanda(comandaLambda);
                             return new InfoComanda(AdministrarComandas.this);
                        });
                   }
                }
           }
         });
        
        //Inyecta la lógica de refrescar la tabla al botón Refrescar
        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> {
            llenarTabla();
        });

        //Llena la tabla cada vez que se entre a la pantalla
        llenarTabla();
    }
    
    
    /**
     * Llena la tabla con registros de cada comanda
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
        
        if (!TEST_MODE) {
            List<ComandaDTO> comandas = CoordinadorNegocio.getInstance().consultarComandas();
            listaTemporal = comandas;
        } else {
            MesaDTO mesa = new MesaDTO("Abierta", 1);
            mesa.setId(1L);
            
            ClienteFrecuenteDTO cliente = new ClienteFrecuenteDTO();
            cliente.setId(1L);
            cliente.setNombres("menchaca");
            cliente.setApellidoPaterno("");
            cliente.setApellidoMaterno("");
            cliente.setTelefono("999999");
            cliente.setCorreo("correoinsano@hotmail.com");
            
            ProductoDTO producto1 = new ProductoDTO();
            producto1.setId(1L);
            producto1.setNombre("Filete");
            producto1.setPrecio(100.0);
            
            ProductoDTO producto2 = new ProductoDTO();
            producto2.setId(2L);
            producto2.setNombre("Vino");
            producto2.setPrecio(50.0);
            
            List<DetallesComandaDTO> detalles = new ArrayList<>();
            
            DetallesComandaDTO detalle1 = new DetallesComandaDTO();
            detalle1.setId(1L);
            detalle1.setProducto(producto1);
            detalle1.setCantidad(3);
            detalle1.setPrecioVenta(producto1.getPrecio());
            detalle1.setSubtotal(detalle1.getSubtotal());
            detalles.add(detalle1);
            
            DetallesComandaDTO detalle2 = new DetallesComandaDTO();
            detalle2.setId(2L);
            detalle2.setProducto(producto2);
            detalle2.setCantidad(5);
            detalle2.setPrecioVenta(producto2.getPrecio());
            detalle2.setSubtotal(detalle2.getSubtotal());
            detalles.add(detalle2);
            
            ComandaDTO comanda = new ComandaDTO();
            comanda.setId(1L);
            comanda.setFolio("BO-20260404-001");
            comanda.setEstado("Abierta");
            comanda.setComentarios("Hola");
            comanda.setDetalles(detalles);
            comanda.setCliente(cliente);
            comanda.setMesa(mesa);
            
            listaTemporal.add(comanda);
        }
        mapearTabla();
    }
    
    
    
    /**
     * Muestra los atributos base de los clientes en la tabla directo de la BD
     */
    private void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, (ComandaDTO c) -> new Object[]{
            c.getId(),
            c.getTotal(),
            c.getMesa().getNumero(),
            c.getEstado(),
            c.getFolio()
        });
    }
    
    
    
    /**
     * Método de la IObservador
     * Escucha el llamado el formulario de registrar o actualizar cliente
     * Entonces cuando se haga el procedimiennto automáticamnete se refleja en la tabla
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }
}