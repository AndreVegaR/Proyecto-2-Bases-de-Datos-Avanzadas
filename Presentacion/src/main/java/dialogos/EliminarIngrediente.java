/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.IngredienteDTO;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import excepciones.NegocioException;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import observadores.IObservador;

/**
 *  * Diálogo  para eliminar un ingrediente del sistema.
 * @author Jazmin
 */
public class EliminarIngrediente extends JDialog {
    //Observador que sera notificado tras una eliminacion exitosa
    private IObservador observador;
    /**
     * Constructor que construye y configura el diálogo de eliminación.
     * @param padre ventana padre sobre la que se centra el diálogo
     * @param obrservador observador al que se notificará tras una eliminación exitos
     */
    public EliminarIngrediente(JFrame padre,IObservador obrservador) {
        super(padre,"Eliminar Ingrediente",true);
        this.observador = obrservador;
        
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20,25,20,25));
         // Recupera el ingrediente que el usuario seleccionó en la tabla
        IngredienteDTO ingrediente = CoordinadorNegocio.getInstance().getIngrediente();
        
        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        JTextField tFUnidad = UtilGeneral.crearCampoFormulario(panel, "Unidad de medida", tamanio);
        JTextField tFStock = UtilGeneral.crearCampoFormulario(panel, "Stock", tamanio);
         //prellena los campos si hay un ingrediente seleccionado
        if(ingrediente !=null){
            tFNombre.setText(ingrediente.getNombre());
            tFUnidad.setText(ingrediente.getUnidadMedida().toString());
            tFStock.setText(String.valueOf(ingrediente.getStock()));
        }
         //solo lectura
        tFNombre.setEditable(false);
        tFUnidad.setEditable(false);
        tFStock.setEditable(false);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,15));
        
        JButton botonEliminar = UtilBoton.crearBoton("Eliminar");
        JButton botonCancelar = UtilBoton.crearBoton("Cancelar");
        panelBotones.add(botonEliminar);
        panelBotones.add(botonCancelar);
        
        add(panel,BorderLayout.CENTER);
        add(panelBotones,BorderLayout.SOUTH);
        
         botonEliminar.addActionListener(e -> {
              //confirmacion antes de eliminar
            int opcion = JOptionPane.showConfirmDialog(this,
                    "¿Deseas eliminar el ingrediente \"" + ingrediente.getNombre() + "\"?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                try {
                    CoordinadorNegocio.getInstance().eliminarIngrediente(ingrediente.getId());
                    JOptionPane.showMessageDialog(this, "Ingrediente eliminado correctamente");
                    if (observador != null) observador.notificarCambio();
                    dispose();
                } catch (NegocioException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
           //cancela y cierra el dialogo
        botonCancelar.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(padre);
    }    
}
