/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.IngredienteDTO;
import DTOs.IngredienteProductoDTO;
import DTOs.ProductoDTO;
import Pantallas.AdministrarIngredientes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Utilerias.UtilBoton;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import observadores.IObservador;

/**
 *
 * @author Angel
 * JDialog para registrar un producto con ingredientes y su imagen
 */
public class RegistrarProducto extends JDialog{
    
    private IObservador observador;
    //Atributo para guardar la imagen que elija el usuario y setearla
    private byte[] imagenSeleccionada;
    //Atributo para tener toda la lista de ingredientes
    private List<IngredienteProductoDTO> listaIngredientes = new ArrayList<>();

    public RegistrarProducto(JFrame padre, IObservador observador) {
        //Bloquea la ventana de atrás
        super(padre, "Registrar Producto", true);
        this.observador = observador;
        
        //Configuración
        setLayout(new BorderLayout());

        //Panel de registro
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        //Margen general para que no se vea amontonado
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        JTextField tFPrecio = UtilGeneral.crearCampoFormulario(panel, "Precio", tamanio);
        JTextField tFDescripcion = UtilGeneral.crearCampoFormulario(panel, "Descripcion", tamanio);
        //Altura fija para que no se estire con BoxLayout
        tFNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //Altura fija para que no se estire con BoxLayout
        tFPrecio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        //Altura fija para que no se estire con BoxLayout
        tFDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        tFDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        //ComboBox que contienen ENUMS 
        JComboBox<ProductoDTO.TipoProducto> comboTipo = new JComboBox<>(ProductoDTO.TipoProducto.values());
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Tipo"));
        panel.add(comboTipo);

        JComboBox<ProductoDTO.EstadoProducto> comboEstado =new JComboBox<>(ProductoDTO.EstadoProducto.values());
        panel.add(new JLabel("Estado"));
        comboEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(comboEstado);

        //Le damos un tamaño mas pequeño
        comboTipo.setPreferredSize(new Dimension(120, 25));
        comboEstado.setPreferredSize(new Dimension(120, 25));
        
        //Boton para elegir la imagen
        JButton btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnAgregar = new JButton("Agregar ingrediente");
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnAgregar);
        panel.add(btnImagen);


        // PANEL INFERIOR
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton botonAceptar = new JButton("Registrar");
        panelInferior.add(botonAceptar);

        add(panel, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        //Boton para seleccionar la imagen
        btnImagen.addActionListener(e -> {
            //Creamos el explorador de archivos
            JFileChooser fileChooser = new JFileChooser();
            //Si el usuario le da a aceptar
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    //Obtenemos el archivo seleccionado
                    File archivo = fileChooser.getSelectedFile();
                    //Convertimos el archivo a Bytes
                    imagenSeleccionada = Files.readAllBytes(archivo.toPath());
                    JOptionPane.showMessageDialog(this, "Imagen cargada");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al cargar imagen");
                }
            }
        });

        //Boton para agregar ingrediente
        btnAgregar.addActionListener(e -> {
            try {
                //Activamos el booleano para que en administrar ingredientes se modifique
                CoordinadorNegocio.getInstance().setAdministrarIngrediente(true);
                //Le damos valor al dialog
                CoordinadorPantallas.getInstance().setDialogActual(this);
                //Lo ocultamos sin cerrarlo
                this.setVisible(false);
                //Navegamos de este dialog al frame de administrarIngredientes
                CoordinadorPantallas.getInstance().navegarA_Frame(this, AdministrarIngredientes::new);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la ventana de ingredientes");
            }
        });

        //REGISTRAR PRODUCTO
        botonAceptar.addActionListener(e -> {
            try {
                String nombre = tFNombre.getText().trim();
                String precioTexto = tFPrecio.getText().trim();
                String descripcion = tFDescripcion.getText().trim();

                if (nombre.isEmpty() || precioTexto.isEmpty() ||descripcion.isEmpty() ) {
                    JOptionPane.showMessageDialog(this, "Campos obligatorios");
                    return;
                }
     
                // Validamos que el precio sea un numero positivo
                double precio;
            try {
                precio = Double.parseDouble(precioTexto);
                if (precio < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El precio debe ser un número positivo", "Precio invalido", JOptionPane.WARNING_MESSAGE);
                return;
            }

                ProductoDTO producto = new ProductoDTO();
                producto.setNombre(nombre);
                producto.setPrecio(precio);
                //Toma el valor de las combobox y los guarda en producto
                //con el getSelectedItem seleccionamos el enum y lo convertimos a ya sea a TipoProducto o EstadoProducto (Casteo)
                producto.setTipoProducto((ProductoDTO.TipoProducto) comboTipo.getSelectedItem());
                producto.setEstadoProducto((ProductoDTO.EstadoProducto) comboEstado.getSelectedItem());
                //Este set es mediante el boton de agregar imagen
                producto.setImagen(imagenSeleccionada);
                
                //Seteamos la descripcion
                producto.setDescripcion(descripcion);
                //Obtenemos la lista de ingredientes
                List<IngredienteProductoDTO> ingredientes = CoordinadorNegocio.getInstance().getListaIngredientes();
                //Si la lista no esta vacía agregamos los ingredientes
                if (!ingredientes.isEmpty()) {
                    producto.setIngredientes(ingredientes);
                }
                     
                //Registramos el producto
                CoordinadorNegocio.getInstance().registrarProducto(producto);
                JOptionPane.showMessageDialog(this, "Producto registrado");
           
                //Limpiamos la lista de ingredientes 
                CoordinadorNegocio.getInstance().limpiarIngredientes();

                if (observador != null) {
                    observador.notificarCambio();
                }
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio inválido");
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(RegistrarProducto.this, ex.getMessage());
            }
        });

          //Tamaño mínimo y pack al final para que tome en cuenta todos los componentes
        setMinimumSize(new Dimension(400, 500));
        pack();
        setLocationRelativeTo(padre);
    }
}