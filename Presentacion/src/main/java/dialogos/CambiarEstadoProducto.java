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
import javax.swing.Box;
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
        super(padre, "Cambiar Estado Producto", true);
        this.producto = producto;
        this.observador = observador;

        setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        //Margen general para que no se vea amontonado
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 15, 10, 15));
        int tamanio = 20;

        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        //False para que no lo pueda editar
        tFNombre.setEditable(false);
        tFNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFNombre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextField tFPrecio = UtilGeneral.crearCampoFormulario(panel, "Precio", tamanio);
        //False para que no lo pueda editar
        tFPrecio.setEditable(false);
        tFPrecio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        tFPrecio.setAlignmentX(Component.LEFT_ALIGNMENT);

        //Esto es para que al momento de editarlo aparesca los valores del Producto
        if (producto != null) {
            Double precio = producto.getPrecio();
            tFNombre.setText(producto.getNombre());
            tFPrecio.setText(String.valueOf(precio));
        }

        JComboBox<ProductoDTO.EstadoProducto> comboEstado = new JComboBox<>(ProductoDTO.EstadoProducto.values());
        comboEstado.setSelectedItem(producto.getEstadoProducto());
        comboEstado.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        comboEstado.setAlignmentX(Component.LEFT_ALIGNMENT);


        //Agregamos los componentes al panel con espaciado entre ellos
        panel.add(Box.createVerticalStrut(10));

        panel.add(new JLabel("Cambiar el estado del producto seleccionado"));
        panel.add(Box.createVerticalStrut(4));
        panel.add(new JLabel("Estado del producto: " + producto.getNombre()));
        panel.add(Box.createVerticalStrut(4));
        panel.add(comboEstado);
        panel.add(Box.createVerticalStrut(10));
        JButton btnGuardar = new JButton("Guardar");

        add(panel, BorderLayout.CENTER);
        add(btnGuardar, BorderLayout.SOUTH);

        //Boton para guardar el cambio de estado
        btnGuardar.addActionListener(e -> {
            try {
                if (producto.getEstadoProducto() == null) {
                    JOptionPane.showMessageDialog(this, "Campos obligatorios");
                    return;
                }
                producto.setEstadoProducto((ProductoDTO.EstadoProducto) comboEstado.getSelectedItem());

                //Llamamos al coordinador de negocio para actualizar el producto
                CoordinadorNegocio.getInstance().cambiarEstado(producto.getId(), producto.getEstadoProducto());
                JOptionPane.showMessageDialog(this, "Estado del producto actualizado");
                observador.notificarCambio();
                dispose();
            } catch (NegocioException ex) {
                JOptionPane.showMessageDialog(CambiarEstadoProducto.this, ex.getMessage());
            }
        });
        
        //Tamaño mínimo y pack al final para que tome en cuenta todos los componentes
        setMinimumSize(new Dimension(400, 500));
        pack();
        setLocationRelativeTo(padre);
    }
}
