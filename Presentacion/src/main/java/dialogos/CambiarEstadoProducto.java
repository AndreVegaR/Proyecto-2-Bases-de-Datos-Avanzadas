/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import DTOs.ProductoDTO;
import excepciones.NegocioException;
import java.awt.BorderLayout;
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

        JTextField tFNombre = new JTextField(producto.getNombre());
        JTextField tFPrecio = new JTextField(String.valueOf(producto.getPrecio()));

        //ComboBox para los tipos y estados seleccionandolos 
        JComboBox<ProductoDTO.TipoProducto> comboTipo =new JComboBox<>(ProductoDTO.TipoProducto.values());
        comboTipo.setSelectedItem(producto.getTipoProducto());

        JComboBox<ProductoDTO.EstadoProducto> comboEstado =new JComboBox<>(ProductoDTO.EstadoProducto.values());
        comboEstado.setSelectedItem(producto.getEstadoProducto());
        
        //Ingredientes
        JComboBox<IngredienteDTO> comboIngredientes = new JComboBox<>();
        JTextField tFCantidad = new JTextField(5);
        
          //LLENAR COMBO DESDE BD
        //Iteramos los ingredientes desde la base de datos y los agregamos al ComboBox de ingredientes
        List<IngredienteDTO> ingredientes =CoordinadorNegocio.getInstance().verTodosLosIngredientes();
        for (IngredienteDTO ing : ingredientes) {
            comboIngredientes.addItem(ing); }

        panel.add(new JLabel("Estado"));
        panel.add(comboEstado);
        JButton btnGuardar = new JButton("Guardar");
       
        
        btnGuardar.addActionListener(e -> {
            try {
                
                String nombre = tFNombre.getText().trim();
                String precioTexto = tFPrecio.getText().trim();
                if (nombre.isEmpty() || precioTexto.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Campos obligatorios");
                    return;
                }
                producto.setEstadoProducto((ProductoDTO.EstadoProducto) comboEstado.getSelectedItem());

                //Llamamos al coordinador de negocio para actualizar el producto
                CoordinadorNegocio.getInstance().cambiarEstado(producto.getId(),producto.getEstadoProducto());
                JOptionPane.showMessageDialog(this, "Producto actualizado");
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
