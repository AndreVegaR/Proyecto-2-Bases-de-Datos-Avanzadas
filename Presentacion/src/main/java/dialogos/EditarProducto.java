/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import DTOs.IngredienteProductoDTO;
import DTOs.ProductoDTO;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
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
import java.awt.Dimension;

/**
 *
 * @author Angel
 * Dialog para editar un producto
 */
public class EditarProducto extends JDialog {
    
    private ProductoDTO producto;
    private IObservador observador;
   //Atributo para guardar la imagen que elija el usuario y setearla
    private byte[] imagenSeleccionada;
    
    //Atributo para tener toda la lista de ingredientes
    private List<IngredienteProductoDTO> listaIngredientes = new ArrayList<>();

    //El ProductoDTO esta en el constructor para evitar que llegue null al BO y evitarnos problemas
    public EditarProducto(JFrame padre, ProductoDTO producto, IObservador observador) {
        super(padre, "Editar Producto", true);
        this.producto = producto;
        this.observador = observador;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JTextField tFNombre = new JTextField(producto.getNombre());
        
        JTextField tFPrecio = new JTextField(String.valueOf(producto.getPrecio()));

        //ComboBox para los tipos y estados seleccionandolos 
        JComboBox<ProductoDTO.TipoProducto> comboTipo =new JComboBox<>(ProductoDTO.TipoProducto.values());
        comboTipo.setSelectedItem(producto.getTipoProducto());

        JComboBox<ProductoDTO.EstadoProducto> comboEstado =new JComboBox<>(ProductoDTO.EstadoProducto.values());
        comboEstado.setSelectedItem(producto.getEstadoProducto());
        
        //Le damos un tamaño mas pequeño
        comboTipo.setPreferredSize(new Dimension(120, 25));
        comboEstado.setPreferredSize(new Dimension(120, 25));
        //Ingredientes
        JComboBox<IngredienteDTO> comboIngredientes = new JComboBox<>();
        JTextField tFCantidad = new JTextField(5);
        
          //LLENAR COMBO DESDE BD
        //Iteramos los ingredientes desde la base de datos y los agregamos al ComboBox de ingredientes
        List<IngredienteDTO> ingredientes =CoordinadorNegocio.getInstance().verTodosLosIngredientes();
        for (IngredienteDTO ing : ingredientes) {
            comboIngredientes.addItem(ing); }

        panel.add(new JLabel("Nombre"));
        panel.add(tFNombre);
        
        panel.add(new JLabel("Precio"));
        panel.add(tFPrecio);

        panel.add(new JLabel("Tipo"));
        panel.add(comboTipo);

        panel.add(new JLabel("Estado"));
        panel.add(comboEstado);
        //Para que no se pueda editar el estado
        comboEstado.setEnabled(false);
        
        tFNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        tFPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JButton btnGuardarIngrediente = new JButton("Guardar Ingredientes");
        btnGuardarIngrediente.setAlignmentX(Component.LEFT_ALIGNMENT);
        JButton btnGuardar = new JButton("Guardar");
        panel.add(new JLabel("Ingrediente"));
        panel.add(comboIngredientes);
        panel.add(new JLabel("Cantidad"));
        panel.add(tFCantidad);
        panel.add(btnGuardarIngrediente);
        
        //Boton para elegir la imagen
        JButton btnImagen = new JButton("Seleccionar Imagen");
        btnImagen.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(btnImagen);

        btnGuardar.addActionListener(e -> {
            try {
                
                String nombre = tFNombre.getText().trim();
                String precioTexto = tFPrecio.getText().trim();
                if (nombre.isEmpty() || precioTexto.isEmpty()) {
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
                producto.setImagen(imagenSeleccionada);
                producto.setIngredientes(listaIngredientes);

                //Llamamos al coordinador de negocio para actualizar el producto
                CoordinadorNegocio.getInstance().actualizarProducto(producto);
                JOptionPane.showMessageDialog(this, "Producto actualizado");
                observador.notificarCambio();
                dispose();
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(EditarProducto.this, ex.getMessage());
            }
        });

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(padre);
    
    
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
                //Convertimos lo seleccionado en el combobox en un IngredienteDTO
                IngredienteDTO seleccionado =(IngredienteDTO) comboIngredientes.getSelectedItem();
                //Parseamos el string a double 
                double cantidad = Double.parseDouble(tFCantidad.getText());
                
                //Creamos la tabla intermedia mediante los ingredientes seleccionados y los relacionamos
                IngredienteProductoDTO ip = new IngredienteProductoDTO();
                //Set al ingrediente del dto seleccionado
                ip.setIngredienteId(seleccionado.getId());
                //Set de la cantidad seleccionada
                ip.setCantidad(cantidad);
                //Agregamos a la lista el ingrediente
                listaIngredientes.add(ip);

                JOptionPane.showMessageDialog(this, "Ingrediente agregado");

                tFCantidad.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida");
            }
        });
    
}
}
