/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Pantallas;

import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.IngredienteDTO;
import DTOs.IngredienteProductoDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilBuild;
import Utilerias.UtilGeneral;
import Utilerias.UtilLogica;
import dialogos.ActualizarStockIngrediente;
import dialogos.EliminarIngrediente;
import dialogos.RegistrarIngrediente;
import dialogos.RegistrarProducto;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
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
 * Pantalla principal del módulo de ingredientes.
 *
 * @author Jazmin
 */
public class AdministrarIngredientes extends JFrame implements IObservador {
    /**
     * Tabla que muestra los ingredientes registrados
     */
    private JTable tabla;
    /**
     * Arreglo de un elemento que guarda el índice de la columna activa para filtrar.
     */
    final int[] columnaActiva = {-1};
    /**
     * Lista local de ingredientes cargados desde la BD.
     */
    private List<IngredienteDTO> listaTemporal;
    /**
     * Constructor que ensambla y configura toda la pantalla:
     * panel de búsqueda, tabla, botones CRUD y listener de doble clic.
     */
    public AdministrarIngredientes() {
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        String[] filtros = {"Nombre", "Unidad de medida"};
        Map<String, JButton> botonesFiltros = new HashMap<>();

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panelBotones.add(new JLabel("Doble clic para ver detalles"));
        JPanel panelTabla = new JPanel(new BorderLayout());

        String[] columnas = {"ID", "Nombre", "Unidad de medida", "Stock"};
        Map<String, JButton> mapaBotones = new HashMap<>();

        ArrayList<Supplier<? extends JDialog>> dialogos = new ArrayList<>();
        dialogos.add(() -> new RegistrarIngrediente(this, this));
        dialogos.add(() -> new ActualizarStockIngrediente(this, this));
        dialogos.add(() -> new EliminarIngrediente(this, this));

        tabla = UtilBuild.ensamblarPantallaAdministrar("Administrar Ingrediente", this, panelBusqueda, panelBotones, panelTabla, filtros, botonesFiltros, columnas, mapaBotones, dialogos, columnaActiva);
        // Listener que selecciona el ingrediente al hacer clic,
        // y muestra sus detalles al hacer doble clic
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                IngredienteDTO ingrediente = null;
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    int indiceReal = tabla.convertRowIndexToModel(fila);
                    ingrediente = listaTemporal.get(indiceReal);
                    CoordinadorNegocio.getInstance().setIngrediente(ingrediente);
                }
                if (evt.getClickCount() >= 2 && ingrediente != null) {
                    mostrarDetalles(ingrediente);
                }
            }
        });

        JButton botonRefrescar = mapaBotones.get(Constantes.OPCIONES_CRUD_MINUS[0]);
        botonRefrescar.addActionListener(e -> llenarTabla());
        
        //Boton para regresar al anterior frame
        JButton botonAtras = UtilBoton.crearBoton("Atras");
        
        botonAtras.addActionListener(e -> {
            
              int opcion = JOptionPane.showConfirmDialog( this,"¿Seguro que quieres regresar?","Confirmar",JOptionPane.YES_NO_OPTION);
              
              if (opcion == JOptionPane.YES_OPTION) {
                  //Desactivamos el metodo de registrar ingredientes por que vamos a regresar al anterior frame
                CoordinadorNegocio.getInstance().setAdministrarIngrediente(false);
                //Cerramos la ventana de administrar ingredientes
                this.dispose();
                //Recuperamos el anterior dialog que es el de Registrar productos o el de EditarProducto
                JDialog dialog = CoordinadorPantallas.getInstance().getDialogActual();
                //Si hay un dialogo lo muestra
                if (dialog != null) {
                    dialog.setVisible(true);
                }
            }
        });  
        

        llenarTabla();
        
         /*
         Pregunta al coordinador si administrarIngredientes es true y si lo es llama al metodo de agregarIngrediente
         */
        if (CoordinadorNegocio.getInstance().isAdministrarIngrediente() == true) {
            agregarIngrediente(panelBotones, mapaBotones);
            
            panelBotones.add(botonAtras);
            //Reorganizamos con revalidate
            panelBotones.revalidate();
            //Actualizamos la vista con el repaint
            panelBotones.repaint();    
        }
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
     * Consulta todos los ingredientes desde el coordinador y refresca la tabla.
     * También actualiza la lista local para acceso por índice.
     */
    public void llenarTabla() {
        listaTemporal = CoordinadorNegocio.getInstance().buscarIngredientes(null, null);
        mapearTabla();
    }
    /**
     * Mapea la lista local de ingredientes a filas de la tabla.
     * La unidad de medida se convierte a String para que el filtro regex funcione correctamente.
     */
    public void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, i -> new Object[]{
            i.getId(),
            i.getNombre(),
            i.getUnidadMedida().toString(),
            i.getStock()
        });
    }

    /**
     * Muestra un diálogo con la información detallada del ingrediente. Si tiene
     * imagen registrada, la muestra; si no, solo muestra el texto.
     *
     * @param ingrediente el ingrediente seleccionado en la tabla
     */
    private void mostrarDetalles(IngredienteDTO ingrediente) {
        IngredienteDTO dto = CoordinadorNegocio.getInstance().buscarIngredientePorId(ingrediente.getId());

        String info = "ID: " + dto.getId()
                + "\nNombre: " + dto.getNombre()
                + "\nUnidad de medida: " + dto.getUnidadMedida()
                + "\nStock: " + dto.getStock();

        byte[] imagenBytes = dto.getImagen();

        if (imagenBytes != null && imagenBytes.length > 0) {
             // Escala la imagen para mostrarla en el diálogo
            ImageIcon icono = new ImageIcon(imagenBytes);
            Image img = icono.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            ImageIcon imagen = new ImageIcon(img);
            JOptionPane.showMessageDialog(this, info, "Detalles del ingrediente",
                    JOptionPane.INFORMATION_MESSAGE, imagen);
        } else {
            JOptionPane.showMessageDialog(this, info, "Detalles del ingrediente",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    /**
     * Método de IObservador.
     * Es llamado por los diálogos tras registrar, actualizar o eliminar un ingrediente,
     * lo que provoca que la tabla se refresque automáticamente.
     */
    @Override
    public void notificarCambio() {
        llenarTabla();
    }
  
    /*
    Metodo para agregar ingredientes
    Primero debe de pasar una validacion de negocio que es que el administrarIngrediente sea true y si lo es llama a este metodo
    Lo que hace este metodo es mostrar la pantalla de ingredientes y mediante un listener del mouse obtenemos el ingrediente
    Tiene que elegir un ingrediente y darle al boton de seleccionar ingrediente y elejir la cantidad
    Validamos la cantidad 
    Creamos la tabla intermedia entre producto e ingrediente y se lo asignamos a una lista del coordinador de negocio que tiene los ingredientes
    Y le damos el valor de false para que se salga de este metodo o modo de agregar ingrediente
    Cerramos la pantalla y volvemos a la pantalla anterior mediante un get que es la de registrarProducto
    */
      private void agregarIngrediente(JPanel panelBotones, Map<String, JButton> mapaBotones) {

       String[] botonesEliminar = {"registrar", "eliminar", "actualizar","refrescar","regresar"};
       UtilLogica.esconderBotones(panelBotones, mapaBotones, botonesEliminar);

       //Crea el botón de seleccionar ingrediente y le da lógica
       JButton botonSeleccionarIngrediente= UtilBoton.crearBoton("Seleccionar ingrediente");
       botonSeleccionarIngrediente.addActionListener(e -> {

            //Obligar a que haya un ingrediente seleccionado
            if (CoordinadorNegocio.getInstance().getIngrediente()== null) {
                UtilGeneral.dialogoAviso(AdministrarIngredientes.this, "Seleccione un ingrediente primero");
            } else {
                //Recupera el ingrediente guardado del coordinador
                IngredienteDTO ingrediente = CoordinadorNegocio.getInstance().getIngrediente();
                
                //Pedimos la cantidad
                //Ok para aceptar y Cancel para cancelar
                String cantidadTexto = JOptionPane.showInputDialog(this,"Ingrese la cantidad para: " + ingrediente.getNombre() );
                // Si lo cancela se sale del optionPane
                if (cantidadTexto == null) return;
                double cantidad;
                try {
                    //Convertimos la cantidad en Double
                    cantidad = Double.parseDouble(cantidadTexto);
                    //Si es menor o igual a 0 mostramos que debe de ser mayor
                    if (cantidad <= 0) {
                        UtilGeneral.dialogoAviso(this, "Cantidad inválida");
                        return;
                    }
                      //Validamos que sea un numero
                      } catch (NumberFormatException ex) {
                    UtilGeneral.dialogoAviso(this, "Debe ser un número");
                    return;
                }
                //Crea un ingredienteProductoDTO 
                IngredienteProductoDTO ip = new IngredienteProductoDTO();
                ip.setIngredienteId(ingrediente.getId());
                ip.setCantidad(cantidad);
                //Le asignamos valor a la tabla intermedia mediante el coordinador
                CoordinadorNegocio.getInstance().agregarIngrediente(ip);
                //Desactivamos el modo de registrar ingrediente
                CoordinadorNegocio.getInstance().setAdministrarIngrediente(false);
                JOptionPane.showMessageDialog(this, "Ingrediente Agregado Exitosamente");
                this.dispose();
                
               //Recuperar el dialog original y si no es null lo muestra
                JDialog dialog = CoordinadorPantallas.getInstance().getDialogActual();
                    if (dialog != null) {
                        dialog.setVisible(true);
                    }
            }
       });
       panelBotones.add(botonSeleccionarIngrediente);
    }
}