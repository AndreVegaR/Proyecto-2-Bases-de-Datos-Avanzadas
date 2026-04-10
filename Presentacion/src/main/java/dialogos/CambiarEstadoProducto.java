/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import DTOs.ProductoDTO;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import observadores.IObservador;

/**
 *
 * @author Usuario
 */
public class CambiarEstadoProducto extends JDialog {
         
    private ProductoDTO producto;
    private IObservador observador;
    
    //El ProductoDTO esta en el constructor para evitar que llegue null al BO y evitarnos problemas
    public CambiarEstadoProducto(JFrame padre, ProductoDTO producto, IObservador observador) {
        super(padre, "Editar Producto", true);
        this.producto = producto;
        this.observador = observador;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        int tamanio = 20;
        
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        //False para que no lo pueda editar
        tFNombre.setEditable(false);
        JTextField tFPrecio = UtilGeneral.crearCampoFormulario(panel, "Precio", tamanio);
        //False para que no lo pueda editar
        tFPrecio.setEditable(false);
        tFNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        tFPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);
        //ComboBox que contienen ENUMS 
        JComboBox<ProductoDTO.TipoProducto> comboTipo = new JComboBox<>(ProductoDTO.TipoProducto.values());
        comboTipo.setAlignmentX(Component.LEFT_ALIGNMENT);
        //False para que no lo pueda editar
        comboTipo.setEnabled(false);
        panel.add(new JLabel("Tipo"));
        panel.add(comboTipo);

        JComboBox<ProductoDTO.EstadoProducto> comboEstado =new JComboBox<>(ProductoDTO.EstadoProducto.values());
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Le damos un tamaño mas pequeño
        comboTipo.setPreferredSize(new Dimension(120, 25));
        comboEstado.setPreferredSize(new Dimension(120, 25));
        
        //Boton para elegir la imagen
        JButton btnImagen = UtilBoton.crearBoton("Seleccionar Imagen");
        btnImagen.setEnabled(false);
        btnImagen.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Ingredientes
        JComboBox<IngredienteDTO> comboIngredientes = new JComboBox<>();
        comboIngredientes.setPreferredSize(new Dimension(120,25));
        //False para que no lo pueda editar
        comboIngredientes.setEnabled(false);
        JTextField tFCantidad = new JTextField(5);
        tFCantidad.setEditable(false);
        tFCantidad.setPreferredSize(new Dimension(120,25));
        JButton btnAgregar = new JButton("Agregar ingrediente");
        //False para que no lo pueda editar
        btnAgregar.setEnabled(false);
        btnAgregar.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(new JLabel("Ingrediente"));
        panel.add(comboIngredientes);
        panel.add(new JLabel("Cantidad"));
        panel.add(tFCantidad);
        panel.add(btnAgregar);
        panel.add(new JLabel("Cambiar el estado del producto seleccionado"));
        panel.add(new JLabel("Estado del producto: "+ producto.getNombre()));
        panel.add(comboEstado);
        JButton btnGuardar = new JButton("Guardar");
       
        
        btnGuardar.addActionListener(e -> {
            try {
                if(producto.getEstadoProducto() == null){
                    JOptionPane.showMessageDialog(this, "Campos obligatorios");
                    return;
                }
                producto.setEstadoProducto((ProductoDTO.EstadoProducto) comboEstado.getSelectedItem());

                //Llamamos al coordinador de negocio para actualizar el producto
                CoordinadorNegocio.getInstance().cambiarEstado(producto.getId(),producto.getEstadoProducto());
                JOptionPane.showMessageDialog(this, "Estado del producto actualizado");
                observador.notificarCambio();
                dispose();
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(CambiarEstadoProducto.this, ex.getMessage());
            }
        });

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(padre);
     
} 
    
    
}
