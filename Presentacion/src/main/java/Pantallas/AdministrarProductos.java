/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas;

import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.DetallesComandaDTO;
import DTOs.IngredienteDTO;
import DTOs.IngredienteProductoDTO;
import DTOs.ProductoDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import Utilerias.UtilLogica;
import dialogos.CambiarEstadoProducto;
import dialogos.EditarProducto;
import dialogos.InfoDetalle;
import dialogos.RegistrarProducto;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import observadores.IObservador;

/**
 *
 * @author Angel
 * Menu para admistrar los productos
 */
public class AdministrarProductos extends JFrame implements IObservador {

    //True por default
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
    //Lista para guardar los productos desde el metodo de llenarTabla
    private List<ProductoDTO> listaTemporal = new ArrayList<>();
    //Atributo de producto para moverse entre pantallas
    private ProductoDTO productoSeleccionado;

    public AdministrarProductos() {

        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Arreglo con los botones que permitirán filtrar según su campo de la tabla
        String[] filtros = {"Nombre", "Categoría"};
        
        //Mapa vacío que será poblado con botones de filtrad o por un método posterior
        Map<String, JButton> botonesFiltros = new HashMap<>();
        
        //Crea paneles
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.add(new JLabel("Doble clic para editar un producto"));
        JPanel panelTabla = new JPanel(new BorderLayout());
        
        /**
         * Arreglo de Strings con los campos de la tabla
         * Será pasado a un próximo método para automatizar la creación de la tabla
         * Importante: debe coincidir con el orden del método mapearTabla
         */
        String[] columnas = {"ID", "Nombre", "Precio", "Estado", "Tipo","Ingredientes"};
        
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
        dialogos.add(() -> new RegistrarProducto(this,this)); 
        dialogos.add(() -> new EditarProducto(this,productoSeleccionado,this));
        dialogos.add(() -> new CambiarEstadoProducto(this,productoSeleccionado,this));
        
        /**
         * Este método crea y configura toda la pantalla de administrar x cosa
         * Llama a un método de UtilBuild al cual le pasas los datos previamente configurados
         * Regresa una pantalla poblada, funcional y fácilmente escalable
         * Sirve para el molde base: pantalla, CRUD, búsqueda
         * Regresa la tabla para inyectarle la lógica de mouseClicked
         * Puedes acceder a los botones creados usando el mapa que fue llenado
         * Se le pueden agregar otros botones de forma fácil
         */
        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar productos", //Título de la ventana
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
         * Limitándole bastante las funcionalidades, solo puede seleccionar productos
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
            JButton botonRegresarNuevo = UtilBoton.crearBotonNavegar("Regresar", AdministrarProductos.this, RegistrarComanda::new);
            panelBotones.add(botonRegresarNuevo);
            
            //Reconfigura y dedibuja el panel
            panelBotones.revalidate();
            panelBotones.repaint();
        }
        
        
        
        //Evento que se activa cuando seleccionas una fila de la columna
        //Sirve para seleccionar el producto y poder editarlo o cambiarle el estado
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent evt) {
            //Obtenemos la fila de la tabla que selecciono el usuario
            int fila = tabla.getSelectedRow();
            //Si selecciono al menos una fila pasa el if
            if (fila != -1) {    
                //Convierte el índice de la fila seleccionada en la tabla 
                //al índice real en la lista de datos ya que la tabla puede estar ordenada o filtrada y los índices pueden cambiar
                int indiceReal = tabla.convertRowIndexToModel(fila);
                //Obtenemos el objeto completo
                productoSeleccionado = listaTemporal.get(indiceReal);
                CoordinadorNegocio.getInstance().setProducto(productoSeleccionado);
                //Si le da doble click entra en el modo edicion de producto
                if (evt.getClickCount() == 2) {
//                    new EditarProducto(AdministrarProductos.this,productoSeleccionado,AdministrarProductos.this)
//                        .setVisible(true);d
                    mostrarDetalles();
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
     * Un mesero que accede a esta pantalla solo está seleccionando un producto, nada más
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
       String[] botonesEliminar = {"registrar", "eliminar", "actualizar"};
       UtilLogica.esconderBotones(panelBotones, mapaBotones, botonesEliminar);

       //Crea el botón de seleccionar producto y le da lógica
       JButton botonSeleccionarProducto= UtilBoton.crearBoton("Seleccionar producto");
       botonSeleccionarProducto.addActionListener(e -> {

            //Obliga a elegir primero un cliente validando si el producto guardado es null
            if (CoordinadorNegocio.getInstance().getProducto() == null) {
                UtilGeneral.dialogoAviso(AdministrarProductos.this, "Seleccione un producto primero");
            } else {
                //Recupera el cliente guardado del coordinador
                ProductoDTO producto = CoordinadorNegocio.getInstance().getProducto();
                
                //Crea un detalles comanda con los datos del producto
                DetallesComandaDTO detalle = new DetallesComandaDTO();
                detalle.setPrecioVenta(producto.getPrecio());
                detalle.setProducto(producto);
                detalle.setImagen(producto.getImagen());
                CoordinadorNegocio.getInstance().setDetalle(detalle);
                
                //Abre el diálogo para información extra (cantidad y comentarios)
                CoordinadorPantallas.getInstance().abrirDialogo(() -> new InfoDetalle(AdministrarProductos.this));
            }
       });
       panelBotones.add(botonSeleccionarProducto);
    }
    
    
    
    /**
     * Llena la tabla con registros de cada producto
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
      listaTemporal = CoordinadorNegocio.getInstance().verTodos();
      mapearTabla();
    }
       
    
    /**
     * Muestra los atributos base de los clientes en la tabla directo de la BD
     */
    private void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, (ProductoDTO p) -> new Object[]{
          p.getId(),
          p.getNombre(),
          p.getPrecio(),
          p.getEstadoProducto(),
          p.getTipoProducto(),
          //Si existe ingredientes los usa si no usa Sin ingredientes
         (p.getIngredientes() != null)? p.getIngredientes().toString()  : "Sin ingredientes"
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
    
       private void mostrarDetalles() {

        String info = "ID: " + productoSeleccionado.getId()
                + "\nNombre: " + productoSeleccionado.getNombre()
                + "\nPrecio: " + productoSeleccionado.getPrecio()
                + "\nEstado: " + productoSeleccionado.getEstadoProducto()
                + "\nIngredientes: " + productoSeleccionado.getIngredientes()
                + "\nTipo: " + productoSeleccionado.getTipoProducto();

        byte[] imagenBytes = productoSeleccionado.getImagen();

        if (imagenBytes != null && imagenBytes.length > 0) {
             // Escala la imagen para mostrarla en el diálogo
            ImageIcon icono = new ImageIcon(imagenBytes);
            Image img = icono.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon imagen = new ImageIcon(img);
            JOptionPane.showMessageDialog(this, info, "Detalles del producto",
                    JOptionPane.INFORMATION_MESSAGE, imagen);
        } else {
            JOptionPane.showMessageDialog(this, info, "Detalles del producto",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    
}
}
