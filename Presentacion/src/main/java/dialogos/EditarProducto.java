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
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import Utilerias.UtilBoton;

/**
 *
 * @author Angel Dialog para editar un producto
 */
public class EditarProducto extends JDialog {

    private ProductoDTO producto;
    private IObservador observador;
    //Atributo para guardar la imagen que elija el usuario y setearla
    private byte[] imagenSeleccionada;


    //El ProductoDTO esta en el constructor para evitar que llegue null al BO y evitarnos problemas
    public EditarProducto(JFrame padre, ProductoDTO producto, IObservador observador) {
        super(padre, "Editar Producto", true);
        this.producto = producto;
        this.observador = observador;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //Margen general para que no se vea amontonado
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JTextField tFNombre = new JTextField(producto.getNombre());
        //Altura fija para que no se estire con BoxLayout
        tFNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField tFPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        //Altura fija para que no se estire con BoxLayout
        tFPrecio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField tFDescripcion = new JTextField(producto.getDescripcion());
        tFDescripcion.setMaximumSize(new Dimension(Integer.MAX_VALUE,30));
        tFDescripcion.setAlignmentX(Component.LEFT_ALIGNMENT);

        //ComboBox para los tipos y estados seleccionandolos 
        JComboBox<ProductoDTO.TipoProducto> comboTipo = new JComboBox<>(ProductoDTO.TipoProducto.values());
        comboTipo.setSelectedItem(producto.getTipoProducto());
        comboTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComboBox<ProductoDTO.EstadoProducto> comboEstado = new JComboBox<>(ProductoDTO.EstadoProducto.values());
        comboEstado.setSelectedItem(producto.getEstadoProducto());
        comboEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);
        //Para que no se pueda editar el estado
        comboEstado.setEnabled(false);

        //Agregamos los componentes al panel con espaciado entre ellos
        panel.add(new JLabel("Nombre"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(tFNombre);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Precio"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(tFPrecio);
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Descripcion"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(tFDescripcion);
        panel.add(Box.createVerticalStrut(10));
        
        panel.add(new JLabel("Tipo"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboTipo);
        panel.add(Box.createVerticalStrut(10));
        

        panel.add(new JLabel("Estado"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboEstado);
        panel.add(Box.createVerticalStrut(10));
        
        JButton btnGuardarIngrediente = new JButton("Actualizar Ingredientes");
        btnGuardarIngrediente.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnGuardarIngrediente);
        panel.add(Box.createVerticalStrut(6));

        //Boton para elegir la imagen
        JButton btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnImagen);

        JButton btnGuardar = new JButton("Guardar");

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        //Boton para guardar los cambios del producto
        btnGuardar.addActionListener(e -> {
            try {

                String nombre = tFNombre.getText().trim();
                String precioTexto = tFPrecio.getText().trim();
                String descripcion = tFDescripcion.getText().trim();
                if (nombre.isEmpty() || precioTexto.isEmpty() || descripcion.isEmpty()) {
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

                producto.setId(producto.getId());
                producto.setNombre(tFNombre.getText());
                producto.setPrecio(precio);
                producto.setTipoProducto((ProductoDTO.TipoProducto) comboTipo.getSelectedItem());
                producto.setEstadoProducto((ProductoDTO.EstadoProducto) comboEstado.getSelectedItem());
                producto.setDescripcion(descripcion);
                producto.setImagen(imagenSeleccionada);
                //Obtenemos la lista de ingredientes
                List<IngredienteProductoDTO> ingredientes = CoordinadorNegocio.getInstance().getListaIngredientes();
                //Si la lista no esta vacía agregamos los ingredientes
                if (!ingredientes.isEmpty()) {
                    producto.setIngredientes(ingredientes);
                }

                //Llamamos al coordinador de negocio para actualizar el producto
                CoordinadorNegocio.getInstance().actualizarProducto(producto);
                JOptionPane.showMessageDialog(this, "Producto actualizado");
                CoordinadorNegocio.getInstance().limpiarIngredientes();
                observador.notificarCambio();
                dispose();
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(EditarProducto.this, ex.getMessage());
            }
        });

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
        btnGuardarIngrediente.addActionListener(e -> {
             try {
                //Activamos el booleano para que en administrar ingredientes se modifique
                CoordinadorNegocio.getInstance().setAdministrarIngrediente(true);
                //Le damos valor al dialog
                CoordinadorPantallas.getInstance().setDialogActual(this);
                //Lo ocultamos sin cerrarlo<
                this.setVisible(false);
                //Navegamos de este dialog al frame de administrarIngredientes
                CoordinadorPantallas.getInstance().navegarA_Frame(this, AdministrarIngredientes::new);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al abrir la ventana de ingredientes");
            }
        });

        //Tamaño mínimo y pack al final para que tome en cuenta todos los componentes
        setMinimumSize(new Dimension(400, 520));
        pack();
        setLocationRelativeTo(padre);
    }
}