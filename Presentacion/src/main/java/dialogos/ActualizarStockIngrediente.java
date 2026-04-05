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
 *Diálogo para actualizar el stock de un ingrediente existente.
 * @author Jazmin
 */
public class ActualizarStockIngrediente extends JDialog {
    //Observador que sera notificado tras una actualizacion de stock exitosa
    private IObservador observador;
    /**
     * Constructor que construye y configura el diálogo de actualización de stock.
     * @param padre ventana padre sobre la que se centra el diálogo
     * @param obrservador observador al que se notificará tras una actualización exitosa
     */
    public ActualizarStockIngrediente(JFrame padre,IObservador obrservador) {
        super(padre,"Actualizar Stock",true);
        this.observador = obrservador;
        
        setLayout(new BorderLayout());
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20,25,20,25));
         // Recupera el ingrediente que el usuario seleccionó en la tabla
        IngredienteDTO ingrediente = CoordinadorNegocio.getInstance().getIngrediente();
        
        int tamanio = 20;
        
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel,"Nombre", tamanio);
        JTextField tFUnidad = UtilGeneral.crearCampoFormulario(panel,"Unidad", tamanio);
        JTextField tFStock = UtilGeneral.crearCampoFormulario(panel,"Stock", tamanio);
        //prellena los campos si hay un ingrediente seleccionado
        if(ingrediente != null){
            tFNombre.setText(ingrediente.getNombre());
            tFUnidad.setText(ingrediente.getUnidadMedida().toString());
            tFStock.setText(String.valueOf(ingrediente.getStock()));
        }
        
        //nombre y unidad no se pueden modificar
        tFNombre.setEditable(false);
        tFUnidad.setEditable(false);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT,15,15));
        JButton botonAceptar = UtilBoton.crearBoton("Aceptar");
        JButton botonCancelar = UtilBoton.crearBoton("Cancelar");
        panelBotones.add(botonAceptar);
        panelBotones.add(botonCancelar);
        
        add(panel,BorderLayout.CENTER);
        add(panelBotones,BorderLayout.SOUTH);
        
        botonAceptar.addActionListener(e -> {
            String stockTexto = tFStock.getText().trim();
            
            //validar campo vacio
            if(stockTexto.isEmpty()){
                JOptionPane.showMessageDialog(this,"El campo de stock es obligatorio","Campos vacios",JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double nuevoStock;
            
            //validar que el numero sea positivo
            try {
                nuevoStock = Double.parseDouble(stockTexto);
                if(nuevoStock <0){
                     throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,"El stock debe ser un número positivo","Dato invalido",JOptionPane.WARNING_MESSAGE);
                return;
            }
            int opcion = JOptionPane.showConfirmDialog(this,"¿Desea actualizar el stock del ingrediente: " + ingrediente.getNombre() + "?"
            ,"Confirmar", JOptionPane.YES_NO_OPTION);
            
            if(opcion == JOptionPane.YES_OPTION){
                try {
                    CoordinadorNegocio.getInstance().actualizarStock(ingrediente.getId(), nuevoStock);
                    if(obrservador!=null) obrservador.notificarCambio();
                    dispose();
                } catch (NegocioException ex) {
                    JOptionPane.showConfirmDialog(this,ex.getMessage(),"Error",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //cancela y cierra el dialogo
        botonCancelar.addActionListener(e -> dispose());
        
        pack();
        setLocationRelativeTo(padre);
    }
    
    
}
