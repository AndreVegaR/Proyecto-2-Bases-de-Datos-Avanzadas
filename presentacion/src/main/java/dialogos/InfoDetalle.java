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
        JLabel labelComentarios = new JLabel("Comentarios opcionales");
        JTextArea areaComentarios = new JTextArea(5, tamanio);
        
        //Panel inferior
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Seleccionar producto");
        panelInferior.add(botonAceptar);
        panelInferior.add(labelComentarios);
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
                
                //Valida la cantidad
                int cantidad = 0;
                if (cantidadString.isBlank()) {
                    UtilGeneral.dialogoAviso(InfoDetalle.this, "Seleccione la cantidad de productos");
                } else {
                    cantidad = Integer.parseInt(cantidadString);
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
                    * Tiene un encadenamiento de métodos:
                    * .getInstance(): instancia
                    * .getComanda(): comanda
                    * .getDetalles(): la la lista de detalles de la comanda
                    * .add(detalle): a la lista le añade el detalle creado
                    */
                   if (CoordinadorNegocio.getInstance().getComanda() != null) {
                       DetallesComandaDTO detalle = CoordinadorNegocio.getInstance().getDetalle();
                       CoordinadorNegocio.getInstance().getComanda().getDetalles().add(detalle);
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