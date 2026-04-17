package Pantallas;
import BO.ComandaBO;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteDTO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.MesaDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import dialogos.ElegirMesa;
import dialogos.InfoComanda;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import observadores.IObservador;

/**
 * Registra una comanda en el sistema
 * Desde aquí se hacen las siguientes funciones:
 * -Se elige un cliente (opcionalmente)
 * -Se elige una mesa
 * -Se eligen los productos
 * -Se muestra un resumen al final
 * @author Andre
 */
public class RegistrarComanda extends JFrame implements IObservador {
    private static String SIN_MESA = "Sin mesa";
    
    //Atributos para ser usados fuera del constructor
    JTable tabla;
    JLabel labelCliente = new JLabel(Constantes.SIN_CLIENTE);
    JLabel labelMesa = new JLabel(SIN_MESA);
    
    private long ultimoLlenado = 0;
    
    /**
     * Atributo en donde se almacena en memoria la lista de todos los productos
     * Esto para poder manipular estos registros (para obtener uno en específico por índice como ejemplo)
     * De esta forma no se tiene que llamar cada vez al BO y gastar recursos en consultas SQL
     * Mejor todo se consulta localmente
     */
    private List<DetallesComandaDTO> listaTemporal;
    
    
    public RegistrarComanda() {
        ComandaDTO com = CoordinadorNegocio.getInstance().getComanda();
        
        if (com == null) {
            com = new ComandaDTO();
            CoordinadorNegocio.getInstance().setComanda(com);
        }
        
        ClienteDTO cli = com.getCliente();
        MesaDTO mes = com.getMesa();
        
        if (cli != null) {
            labelCliente.setText(cli.getNombres() + " " + cli.getApellidoPaterno() + " " + cli.getApellidoMaterno());
        }
        
        String txt = "";
        if (mes != null && CoordinadorNegocio.getInstance().actualizandoComanda()) {
            txt = "Mesa " + mes.getNumero();
        } else {
            txt = SIN_MESA;
            if (!CoordinadorNegocio.getInstance().actualizandoComanda()) {
                CoordinadorNegocio.getInstance().setMesa(null);
            }
        }
        labelMesa.setText(txt);
        
        if (!CoordinadorNegocio.getInstance().actualizandoComanda()) {
            labelMesa.setText(SIN_MESA);
        }
        
        //Actualiza cada vez que se llega a esta pantalla
        actualizarLabels();
        
        //Configuraciones inicialaes
        UtilGeneral.configurarFrame("Registrar Comanda", this);
        setLayout(new BorderLayout());

        //Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        panelBusqueda.setBackground(new Color(235, 245, 251)); 
        panelBusqueda.setPreferredSize(new Dimension(0, 100));
        
        //Label del nombre del cliente
        panelBusqueda.add(labelCliente);
        
        //Botones que buscan clientes y mesas
        JButton botonBuscarClientes = UtilBoton.crearBotonNavegar("Administrar clientes", this, AdministrarClientes::new);
        JButton botonElegirMesa = UtilBoton.crearBotonDialogo("Elegir mesa", () -> new ElegirMesa(this, this));
        panelBusqueda.add(botonBuscarClientes);
        panelBusqueda.add(botonElegirMesa);
        
        //Label de la mesa seleccionada
        panelBusqueda.add(labelMesa);
        
        //Panel de la tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Constantes.COLOR_FONDO);
        
        //Crea la tabla donde aparecerán los detalles de productos
        String[] camposTabla = Constantes.CAMPOS_TABLA_DETALLES;
        tabla = UtilGeneral.crearTabla(camposTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        //Evento que se activa cuando seleccionas una fila de la columna
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                //Crea el cliente desde aquí para usarlo fuera del if
                DetallesComandaDTO detalle = null;
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    
                    /**
                     * Esto mantiene siempre el índice real de los registros
                     * Por ejemplo, si selecciono el primer registro de una tabla filtrada, y sabe
                     * que se trata en realidad de otro índice real en la lista
                     */
                    int indiceReal = tabla.convertRowIndexToModel(fila);
                    
                    //Obtiene el detalle en dicho índice y lo manda al método que muestra su info
                    detalle = listaTemporal.get(indiceReal);
                    
                    //Asigna el cliente al coordinador para que sea usado
                    CoordinadorNegocio.getInstance().setDetalle(detalle);
                }
           }
         });
        
        ComandaDTO comandaActual = CoordinadorNegocio.getInstance().getComanda();
        if (comandaActual.getDetalles() == null) {
            comandaActual.setDetalles(new ArrayList<>());
        }
        this.listaTemporal = new ArrayList<>(comandaActual.getDetalles());
        List<DetallesComandaDTO> nuevos = CoordinadorNegocio.getInstance().getDetallesImportados();
        if (nuevos != null) {
            for (DetallesComandaDTO n : nuevos) {
                if (!this.listaTemporal.contains(n)) {
                    this.listaTemporal.add(n);
                }
            }
            CoordinadorNegocio.getInstance().setDetallesImportados(null);
        }
        comandaActual.setDetalles(this.listaTemporal);
        llenarTabla();
        
        
        //Panel de opcioens inferiores
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panelOpciones.setBackground(new Color(242, 243, 244)); 
        panelOpciones.setPreferredSize(new Dimension(0, 90));
        
        /**
         * Crea y configura el botón de regresar al menú principal
         * Se borrará la comanda que se estaba creando
         */
        JButton btnRegresar = UtilBoton.crearBoton("Regresar");
        btnRegresar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir? Se borrará lo que lleva de esta comanda",
                "Dejar la comanda",             
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE 
            );
            
            //Si sí quiere salir, regresa al menú principal
            if (respuesta == JOptionPane.YES_OPTION) {
                CoordinadorPantallas.getInstance().navegar(RegistrarComanda.this, MenuPrincipal::new);
                CoordinadorNegocio.getInstance().setMesa(null);
                CoordinadorNegocio.getInstance().setComanda(null);
                CoordinadorNegocio.getInstance().setCliente(null);
            }
        });
        
        //Botón para navegar a los productos
        JButton btnBuscarProducto = UtilBoton.crearBotonNavegar("Buscar Producto", this, AdministrarProductos::new);
        
        //Botón para eliminar un detalle de la comanda
        JButton btnEliminarProducto = UtilBoton.crearBoton("Eliminar producto");
        btnEliminarProducto.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();

            //Si no se ha elegido nada
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar");
                return;
            }

            //Confirmación
            int respuesta = JOptionPane.showConfirmDialog(this, 
                    "¿Estás seguro de eliminar este producto de la lista?", 
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);

            //Elimina el producto de la lista directamente
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    int indiceReal = tabla.convertRowIndexToModel(filaSeleccionada);
                    this.listaTemporal.remove(indiceReal);
                    llenarTabla();
                    CoordinadorNegocio.getInstance().setDetalle(null);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar: " + ex.getMessage());
                }
            }
        });
        
        //Si se está actualizando no se puede cambiar al cliente
        if (CoordinadorNegocio.getInstance().actualizandoComanda()) {
            panelBusqueda.remove(botonBuscarClientes);
        }
        
        /**
         * Crea y configura el botón de continuar
         * De ser cliqueado te manda a la pantalla de ResumenComanda
         * Pero te notifica si no has seleccionado mesa todavía
         */
        JButton btnContinuar = UtilBoton.crearBoton("Continuar");
        btnContinuar.addActionListener(e -> {
            if (CoordinadorNegocio.getInstance().getMesa() == null) {
                UtilGeneral.dialogoAviso(RegistrarComanda.this, "Elija una mesa primero");
                return;
            }
            if (listaTemporal.isEmpty()) {
                UtilGeneral.dialogoAviso(RegistrarComanda.this, "Debe haber al menos un producto");
                return;
            }
            CoordinadorNegocio.getInstance().botonContinuarPresionado = true; //se hace true esta cosa
            CoordinadorNegocio.getInstance().setDetallesImportados(new ArrayList<>(this.listaTemporal));
            CoordinadorPantallas.getInstance().abrirDialogo(() -> new InfoComanda(RegistrarComanda.this, RegistrarComanda.this));
        });
        
        //Agrega al panel de botones
        panelOpciones.add(btnRegresar);
        panelOpciones.add(btnBuscarProducto);
        panelOpciones.add(btnEliminarProducto);
        panelOpciones.add(btnContinuar);
        
        
        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);
    }
    
    
    
    /**
     * Llena la tabla con registros de cada cliente
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
        if (System.currentTimeMillis() - ultimoLlenado < 500) return; 
        ultimoLlenado = System.currentTimeMillis();

        // Si por alguna razón la lista es nula, la inicializamos
        if (this.listaTemporal == null) {
            this.listaTemporal = new ArrayList<>();
        }
        limpiarTabla();
        mapearTabla();

        System.out.println("[DEBUG VISTA] Filas en tabla: " + listaTemporal.size());
    }


    
    private void mapearTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        System.out.println("[DEBUG CRÍTICO] Limpiando tabla. Filas actuales: " + modelo.getRowCount());

        for (int i = 0; i < listaTemporal.size(); i++) {
            System.out.println("[DEBUG CRÍTICO] Dibujando fila #" + (i+1) + " de " + listaTemporal.size());
            DetallesComandaDTO d = listaTemporal.get(i);
            modelo.addRow(new Object[]{
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioVenta(),
                d.getSubtotal()
            });
        }
        System.out.println("[DEBUG CRÍTICO] Total filas en modelo tras mapeo: " + modelo.getRowCount());
    }
    
    
    
    /*
     * Si en cualquiera se elige un cliente o mesa, se dispara este método
     * Cambia los labels con el nombre del cliente y el número de mesa seleccionados
     */
    public void actualizarLabels() {
        
        //Cambia el label de cliente
        ClienteDTO cliente = CoordinadorNegocio.getInstance().getCliente();
        if (cliente != null) {
            String infoCliente = cliente.getNombres() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno();
            labelCliente.setText(infoCliente);
        }
        else {
            labelCliente.setText(Constantes.SIN_CLIENTE);
        }
         
        //Cambia el label de la mesa
        MesaDTO mesa = CoordinadorNegocio.getInstance().getMesa();
        if (mesa != null) {
            int numero = mesa.getNumero();
            labelMesa.setText("Mesa " + numero);
        }
    }
    
    
    @Override
    public void notificarCambio() {
        actualizarLabels();
    }
    
    //No pues limpia la tabla ☕
    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); 
    }
}