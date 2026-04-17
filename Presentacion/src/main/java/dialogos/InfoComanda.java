package dialogos;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ComandaDTO;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.awt.Component;
import java.util.List;
import DTOs.DetallesComandaDTO;
import DTOs.MesaDTO;
import Pantallas.AdministrarComandas;
import Pantallas.RegistrarComanda;
import Principal.MenuEmpleados;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import java.awt.Font;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import mappers.ComandaMapper;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Font;
import java.awt.Dimension;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import observadores.IObservador;


/**
 * Diálogo que muestra en una tabla todos los productos de una comanda
 * Se usan DetallesProductoDTO
 * 
 * @author Andre
 */
public class InfoComanda extends JDialog {
    
    //Se instancia como atributo para usarlo en métodos fuera del constructor
    private JTable tabla;
    
    //Observador
    private IObservador observador;
    
    /**
     * Atributo en donde se almacena en memoria la lista de todos los productos
     * Esto para poder manipular estos registros (para obtener uno en específico por índice como ejemplo)
     * De esta forma no se tiene que llamar cada vez al BO y gastar recursos en consultas SQL
     * Mejor todo se consulta localmente
     */
    private List<DetallesComandaDTO> listaTemporal;
    
    public InfoComanda(JFrame frame, IObservador observador) {
        super(frame, "Información de la comanda", true);
        this.observador = observador;
        
        System.out.println("ID Comanda en Coordinador: " + 
        (CoordinadorNegocio.getInstance().getComanda() != null ? 
        CoordinadorNegocio.getInstance().getComanda().getId() : "¡ES NULL!"));
        
        //Configuración inicial
        UtilGeneral.configurarDialogoInicio(this, false);
        
        //Crea el panel de la tabla
        JPanel panelTabla = new JPanel();
        panelTabla.setLayout(new javax.swing.BoxLayout(panelTabla, javax.swing.BoxLayout.Y_AXIS));
        panelTabla.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));
        
        //Crea la tabla y la agrega al panel, guarda la constante en otra variable solo por si acaso
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
                    
                    //Obtiene el cliente en dicho índice y lo manda al método que muestra su info
                    detalle = listaTemporal.get(indiceReal);
                    
                    //Asigna el cliente al coordinador para que sea usado
                    CoordinadorNegocio.getInstance().setDetalle(detalle);
                    
                    /**
                    * Si se cliquea dos veces, el coordinador abre el diálogo
                    * Ese diálogo muestra una tabla con los productos de la comanda
                    */
                    if (evt.getClickCount() == 2) {
                        
                        //Se hace una final solo para usarla dentro de este lambda
                        final DetallesComandaDTO detalleLambda = detalle;
                        CoordinadorNegocio.getInstance().setDetalle(detalleLambda);
                        String info = CoordinadorNegocio.getInstance().getDetalle().getInfo();
                        
                        //Obtiene en bytes la imagen del producto del detalle
                        byte[] bytes = detalleLambda.getProducto().getImagen();
                        
                        //Como es opcional, valida que exista la foto antes de procesarla
                        if (bytes != null && bytes.length > 0) {
                            ImageIcon imagen = UtilGeneral.procesarImagen(bytes);
                            JOptionPane.showMessageDialog(InfoComanda.this, 
                                                                        info, 
                                                                        "Detalles del producto", 
                                                                        JOptionPane.INFORMATION_MESSAGE, 
                                                                        imagen);
                        } else {
                            UtilGeneral.dialogoAviso(InfoComanda.this, info);
                        }
                   } 
                }
           }
         });
        
        //De una vez llena la tabla para luego usar la listaTemporal
        List<DetallesComandaDTO> importados = CoordinadorNegocio.getInstance().getDetallesImportados();
        if (importados != null && !importados.isEmpty()) {
            this.listaTemporal = importados;
            mapearTabla(); 
            CoordinadorNegocio.getInstance().setDetallesImportados(null); 
        } else {
            llenarTabla();
        }

        //Crea un panel para el total de la comanda
        JPanel panelInferior = new JPanel();
        panelInferior.setLayout(new javax.swing.BoxLayout(panelInferior, javax.swing.BoxLayout.Y_AXIS));
        panelInferior.setBorder(new javax.swing.border.EmptyBorder(10, 25, 20, 25));
        
        //Contenido del resumen inferior
        double total = 0;
        int productos = (listaTemporal != null) ? listaTemporal.size() : 0;
        if (listaTemporal != null) {
            for (DetallesComandaDTO d : listaTemporal) total += d.getSubtotal();
        }
        String folio;
        
        //Botón que registra o actualiza una comanda
        JButton botonCambios = null;
        
        /**
         * Si la comanda actual se va a registrar:
         * -Indica que no existe folio pues aún no hay registro
         * -El botón se encarga de registrar
         */
        if (CoordinadorNegocio.getInstance().esNuevaComanda() && !CoordinadorNegocio.getInstance().actualizandoComanda()) {
            folio = "Aún sin registrar";
            botonCambios = UtilBoton.crearBotonNavegar("Registrar", frame, MenuEmpleados::new);
            botonCambios.addActionListener(e -> {
                ComandaDTO comanda = CoordinadorNegocio.getInstance().getComanda();
                comanda.setDetalles(this.listaTemporal);
                comanda.setMesa(CoordinadorNegocio.getInstance().getMesa());
                CoordinadorNegocio.getInstance().registrarComanda(comanda);
                CoordinadorNegocio.getInstance().setMesa(null);
                CoordinadorNegocio.getInstance().setCliente(null);
                observador.notificarCambio();
            });
        } 
        
        /**
         * Si la comanda actual se va a actualizar:
         * -Guarda el folio de la comanda
         * -El botón se encarga de actualizar
         */
        else {
            folio = CoordinadorNegocio.getInstance().getComanda().getFolio();
            
            //Si se está actualizando, lo actualiza en la BD
            if (CoordinadorNegocio.getInstance().actualizandoComanda() && !CoordinadorPantallas.getInstance().esAdministrador()) {
                botonCambios = UtilBoton.crearBoton("Actualizar");
                botonCambios.addActionListener(e -> {
                    long startTime = System.currentTimeMillis();

                    try {
                        List<DetallesComandaDTO> listaParaExportar = new ArrayList<>(this.listaTemporal);
                        CoordinadorNegocio.getInstance().setDetallesImportados(listaParaExportar);


                        if (CoordinadorNegocio.getInstance().botonContinuarPresionado) {
  
                            ComandaDTO comanda = CoordinadorNegocio.getInstance().getComanda();
                            if (comanda != null) {
                                comanda.setDetalles(listaParaExportar);

                                CoordinadorNegocio.getInstance().actualizarComanda(comanda);

                                // Limpieza total del Singleton
                                CoordinadorNegocio.getInstance().setComanda(null);
                                CoordinadorNegocio.getInstance().setDetallesImportados(null);
                                CoordinadorNegocio.getInstance().setMesa(null);

                                CoordinadorPantallas.getInstance().navegar(frame, MenuPrincipal::new);
                                CoordinadorNegocio.getInstance().botonContinuarPresionado = false;
                                this.dispose();
                            }
                        } else {
                            CoordinadorPantallas.getInstance().navegar(frame, RegistrarComanda::new);
                            this.dispose();
                        }

                    } catch (Exception ex) {
                        System.err.println("[ERROR] Fallo en el flujo del botón Actualizar");
                        ex.printStackTrace();
                    }
                });
            }
            else {
                if (CoordinadorNegocio.getInstance().comandaAbierta() && !CoordinadorPantallas.getInstance().esAdministrador()) {
                    botonCambios = UtilBoton.crearBotonNavegar("Editar Productos", frame, RegistrarComanda::new);
                }
            }
        }
        
        //Crea los labels
        JLabel labelFolio = new JLabel("Folio: " + folio);
        labelFolio.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel labelTotal = new JLabel("Total de la comanda: $" + total);
        labelTotal.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel labelCantidad = new JLabel("Cantidad de productos: " + productos);
        labelCantidad.setFont(new Font("Arial", Font.BOLD, 14));
        
        //Crea el panel
        panelInferior.setLayout(new BoxLayout(panelInferior, BoxLayout.Y_AXIS));
        panelInferior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        //Alinea los labels
        labelFolio.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelCantidad.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Agrega los labels
        panelInferior.add(labelFolio);
        panelInferior.add(Box.createVerticalStrut(5));
        panelInferior.add(labelTotal);
        panelInferior.add(Box.createVerticalStrut(5));
        panelInferior.add(labelCantidad);

        //Agrega un pequeño espacio al botón
        panelInferior.add(Box.createVerticalStrut(15)); 

        //Si el botón existe, lo agrega al panel
        if (botonCambios != null) {
            botonCambios.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelInferior.add(botonCambios);
        }
        
        //Redibuja el panel
        panelInferior.revalidate();
        panelInferior.repaint();
        
        //Agrega al diálogo
        add(panelTabla, BorderLayout.NORTH);
        add(panelInferior, BorderLayout.SOUTH);
        
        //Configuración final
        UtilGeneral.configurarDialogoFinal(this);
    }
    
    
    
    /**
     * Llena la tabla con registros de cada cliente
     * También se guardan en el atributo local de listaTemporal
     * De esta forma podemos acceder a su contenido sin tener que conectarnos cada vez
     */
    public void llenarTabla() {
        CoordinadorNegocio.getInstance().setDetallesImportados(new ArrayList<>());
        List<DetallesComandaDTO> detalles = CoordinadorNegocio.getInstance().consultarDetalles();
        listaTemporal = detalles;
        limpiarTabla();
        mapearTabla(); 
        System.out.println("[DEBUG VISTA] Registros que llegaron al llenarTabla: " + detalles.size());
    }
    
    
    
    /**
     * Muestra los detalles de la comanda en la tabla directo de la BD
     */
    private void mapearTabla() {
        UtilGeneral.registrarTabla(tabla, listaTemporal, (DetallesComandaDTO d) -> new Object[]{
            d.getProducto().getNombre(),
            d.getCantidad(),
            d.getPrecioVenta(),
            d.getSubtotal()
        });
    }
    
    //No pues limpia la tabla ☕
    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0); 
    }
}