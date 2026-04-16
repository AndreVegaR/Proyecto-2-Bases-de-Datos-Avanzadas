package dialogos;
import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
import DTOs.DetallesComandaDTO;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import observadores.IObservador;
import excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * JDialog para registrar un cliente
 * @author Angel
 */
public class InfoDetalle extends JDialog {
    private IObservador observador;
    
    public InfoDetalle(JFrame padre) {
        //Bloquea la ventana de atrás
        super(padre, "Registrar Cliente", true);
        //this.observador = observador;
        
        //Configuración
        setLayout(new BorderLayout());

        //Panel de registro
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));
        
        //Campos de texto
        int tamanio = 20;
        JTextField tfCantidad = UtilGeneral.crearCampoFormulario(panel, "Cantidad", tamanio);
        JTextArea areaComentarios = new JTextArea(5, tamanio);
        
        //Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Seleccionar producto");
        panelInferior.add(botonAceptar);
        panelInferior.add(areaComentarios);

        //Agrega todo al JDialog
        add(panel, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        //Evento del botón Aceptar
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Extre valores
                String cantidadString = tfCantidad.getText().trim();
                
                //Valida que el campo haya sido llenado
                if (cantidadString.isBlank()) {
                    UtilGeneral.dialogoAviso(InfoDetalle.this, "Seleccione la cantidad de productos");
                    return;
                }
                
                //Valida que sea un número
                int cantidad = 0;
                try {
                    cantidad = Integer.parseInt(cantidadString);
                } catch (NumberFormatException ex) {
                    UtilGeneral.dialogoAviso(InfoDetalle.this, "La cantidad debe ser un número entero");
                    return;
                }
                
                //Valida que sea un entero positivo
                cantidad = Integer.parseInt(cantidadString);
                if (cantidad < 1) {
                    UtilGeneral.dialogoAviso(InfoDetalle.this, "Introduzca un número positivo");
                    return;
                }
                
                //Obtiene los comentarios
                String comentarios = "";
                if (!areaComentarios.getText().isBlank()) {
                    comentarios = areaComentarios.getText();
                }

                //Confirmación
                int opcion = JOptionPane.showConfirmDialog(
                        InfoDetalle.this,
                        "¿Desea agrgar este producto?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    
                    //Añade la información
                    CoordinadorNegocio.getInstance().getDetalle().setCantidad(cantidad);
                    if (!comentarios.isBlank()) {
                        CoordinadorNegocio.getInstance().getDetalle().setComentarios(comentarios);
                    }
                    
                   /**
                    * Agrega el DTO a la comanda actual
                    */
                   if (CoordinadorNegocio.getInstance().getComanda() != null) {
                        DetallesComandaDTO detalle = CoordinadorNegocio.getInstance().getDetalle();
                        detalle.setCantidad(cantidad);
                        detalle.setComentarios(comentarios);
                        List<DetallesComandaDTO> importados = new ArrayList<>();
                        importados.add(detalle);
                        CoordinadorNegocio.getInstance().setDetallesImportados(importados);
                   }
                   
                   //Cierra el diálogo
                   InfoDetalle.this.dispose();
                }
            }
        });
        
        //Configuración final y cierre 
        this.pack();
        this.setLocationRelativeTo(padre);
    }
}