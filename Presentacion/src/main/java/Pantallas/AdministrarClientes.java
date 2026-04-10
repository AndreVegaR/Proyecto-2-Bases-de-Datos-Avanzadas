package Pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import dialogos.RegistrarCliente;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import Utilerias.UtilLogica;
import dialogos.ActualizarCliente;
import dialogos.EliminarCliente;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
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
    
    /**
     * Arreglo que contiene solo un elemento: el índice del botón que filtra en la búsqueda
     * Se implementa así porque los addActionListeners usan variables final
     * Como una variable final no puede cambiar, se guarda en un arreglo
     * Entonces el arreglo en sí nunca cambia en sí; solo cambia su contenido
     * Este movimiento no es ilegal para Java y permite el flujo perfectamente
     * 
     */
    final int[] columnaActiva = {-1};
    
    /**
     * Atributo en donde se almacena en memoria la lista de todos los clientes
     * Esto para poder manipular estos registros (para obtener uno en específico por índice como ejemplo)
     * De esta forma no se tiene que llamar cada vez al BO y gastar recursos en consultas SQL
     * Mejor todo se consulta localmente
     */
    private List<ClienteDTO> listaTemporal;

    public AdministrarClientes() {

        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Arreglo con los botones que permitirán filtrar según su campo de la tabla
        String[] filtros = {"ID", "Nombre", "Teléfono", "Correo"};
        
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
            System.out.println("Soy una prueba"); 
        });
        
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

        /**
         * Pregunta al coordinador si la sesión es de administrador o no
         * En caso de que no lo sea, activa el método que configura la perspectiva de un mesero
         * Limitándole bastante las funcionalidades, solo puede seleccionar meseros
         * Una vez seleccionado, regresará a la pantalla de RegistrarComanda
         */
        if (!CoordinadorPantallas.getInstance().esAdministrador()) {
            configurarPerspectivaMesero(panelBotones, mapaBotones);
            
            //Recupera el panel de regresar para removerlo de las opciones
            JButton botonReg = recuperarBoton(panelBotones, "regresar");
            panelBotones.remove(botonReg);
            panelBotones.revalidate();
            panelBotones.repaint();
            
            //Crea otro boton regresar para usarlo
            JButton botonRegresarNuevo = UtilBoton.crearBotonNavegar("Regresar", AdministrarClientes.this, RegistrarComanda::new);
            panelBotones.add(botonRegresarNuevo);
            
            //Reconfigura y dedibuja el panel
            panelBotones.revalidate();
            panelBotones.repaint();
        }
        
        //Evento que se activa cuando seleccionas una fila de la columna
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                //Crea el cliente desde aquí para usarlo fuera del if
                ClienteDTO cliente = null;
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    /**
                     * Esto mantiene siempre el índice real de los registros
                     * Por ejemplo, si selecciono el primer registro de una tabla filtrada, y sabe
                     * que se trata en realidad de otro índice real en la lista
                     */
                    int indiceReal = tabla.convertRowIndexToModel(fila);
                    
                    //Obtiene el cliente en dicho índice y lo manda al método que muestra su info
                    cliente = listaTemporal.get(indiceReal);
                    
                    //Asigna el cliente al coordinador para que sea usado
                    CoordinadorNegocio.getInstance().setCliente(cliente);
                }
                
                //Si se cliquea dos veces, muestra su información específica
                if (evt.getClickCount() == 2) {
                    mostrarDetalles(cliente);
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
     * Busca un botón en específicio de un panel por si se necesita y lo rescata
     * 
     * @param panel a buscar
     * @param texto del botóna recuperar
     * @return el botón recuperado
     */
    private JButton recuperarBoton(JPanel panel, String textoBoton) {
        for (Component componente: panel.getComponents()) {
            if (componente instanceof JButton boton && boton.getText().equalsIgnoreCase(textoBoton)) {
                return boton;
            }
        }
        return null;
    }
    
    
    
    /**
     * Configura la perspectiva del mesero en esta pantalla
     * Un mesero que accede a esta pantalla solo está seleccionando un cliente, nada más
     * Una vez seleccionado, regresará a la pantalla de RegistrarComanda
     * El método existe para no ensuciar el constructor
     * 
     * @param panelBotones donde se van a remover los botones
     * @param mapaBotones que se van a remover
     */
    private void configurarPerspectivaMesero(JPanel panelBotones, Map<String, JButton> mapaBotones) {
       
        /**
        * Estas dos líneas se encargan de desaparecer de la interfaz botones del CRUD
        * El método ensamblarPantallaAdministrar asume todo el CRUD, pero a veces no se ocupa
        * Un administrador no registra comandas, y las comandas no pueden ser eliminadas
        * Entonces el método esconderBotones se encarga de extirparlos:
        * -Busca coincidencias entre el arreglo y el mapa de botones
        * -Remueve del panel cada coincidencia
        * -Al final recarga el panel
        */
       String[] botonesEliminar = {"registrar", "eliminar", "actualizar", "generar reporte"};
       UtilLogica.esconderBotones(panelBotones, mapaBotones, botonesEliminar);

       //Crea el botón de seleccionar cliente y le da lógica
       JButton botonSeleccionarCliente = UtilBoton.crearBoton("Seleccionar cliente");
       botonSeleccionarCliente.addActionListener(e -> {

            //Obliga a elegir primero un cliente validando si el cliente guardado es null
            if (CoordinadorNegocio.getInstance().getCliente() == null) {
                UtilGeneral.dialogoAviso(AdministrarClientes.this, "Seleccione un cliente primero");
            } else {
                //Si hay un observador, es avisado de que se eligió el cliente y se asigna a la comanda
                CoordinadorPantallas.getInstance().navegar(AdministrarClientes.this, RegistrarComanda::new);
                if (CoordinadorNegocio.getInstance().getComanda() != null) {
                    ClienteDTO cliente = CoordinadorNegocio.getInstance().getCliente();
                    if (cliente != null) {
                        CoordinadorNegocio.getInstance().getComanda().setCliente(cliente);
                    }
                }
            }

       });
       panelBotones.add(botonSeleccionarCliente);
    }
    
    
    
    /**
     * Llena la tabla con registros de cada cliente
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
        List<ClienteDTO> clientes = CoordinadorNegocio.getInstance().consultarClientes();
        
        for(ClienteDTO c : clientes) {
        if(c instanceof ClienteFrecuenteDTO) {
             System.out.println("DTO Recibido: " + c.getNombres() + " - Info: " + c.getInfoAdicional());
        }
    }
        
        
        listaTemporal = clientes;
        mapearTabla(); 
    }
    
    
    
    /**
     * SOLO ES TEMPORAL, método para probar sin conectarse
     * @return 
     */
    public List<ClienteDTO> llenarTablaFalsa() {
        List<ClienteDTO> listaFalsa = new ArrayList<>();
        
        String[] nombres = {"Andre", "Angel", "Jazmin", "Maye", "Quiñones", "Domitsu"};
        
        Long contador = 0L;
        
        for (String nombre: nombres) {
            contador++;
            ClienteFrecuenteDTO cliente = new ClienteFrecuenteDTO();
            cliente.setNombres(nombre);
            cliente.setApellidoPaterno("");
            cliente.setApellidoMaterno("");
            cliente.setTelefono("1234566");
            cliente.setCorreo(nombre + "@gmail.com");
            cliente.setId(contador);
            listaFalsa.add(cliente);
        }
        
        ClienteFrecuenteDTO cliente = new ClienteFrecuenteDTO();
        cliente.setNombres("menchaca");
        cliente.setApellidoPaterno("");
        cliente.setApellidoMaterno("");
        cliente.setTelefono("999999");
        cliente.setCorreo("correoinsano@hotmail.com");
        cliente.setId(20L);
        listaFalsa.add(cliente);
        
        //POR AHORA LO REEMPLAZA ESTO ES SOLO PARA PRUEBAS
        //listaTemporal = listaFalsa;
        
        
        mapearTabla();
        
        return listaFalsa;
    }
    
    
    
    /**
     * Muestra los atributos base de los clientes en la tabla directo de la BD
     */
    private void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, c -> new Object[]{
            c.getId(),
            c.getNombres() + " " + c.getApellidoPaterno() + " " + c.getApellidoMaterno(),
            c.getTelefono(),
            (c.getCorreo() != null && !c.getCorreo().isEmpty()) ? c.getCorreo() : "No tiene",
            c.getFechaRegistro(),
            c.getTipo()
        });
    }
   
   
    
   /**
    * Muestra la información adicional del tipo de cliente
    * No debe preocuparse por esos datos en específico
    * Solo sabe que cualquier subclase de ClienteDTO tiene ese método
    * Así que es ajeno a los tipos de clientes, solo sabe que debe llamar al método getInfoAdicional()
    * 
    * @param cliente a mostrar información adicional
    */
    private void mostrarDetalles(ClienteDTO cliente) {
       String info = cliente.getInfoAdicional();

       //Si sí regresó algo despliga en efecto el mensaje
       if (info != null && !info.isBlank()) {
           UtilGeneral.dialogoAviso(this, info);
       }
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