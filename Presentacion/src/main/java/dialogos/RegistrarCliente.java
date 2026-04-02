package dialogos;

import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteFrecuenteDTO;
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
import DTOs.ClienteDTO;
import excepciones.NegocioException;

/**
 * JDialog para registrar un cliente
 * @author Angel
 */
public class RegistrarCliente extends JDialog {
    private IObservador observador;
    
    public RegistrarCliente(JFrame padre, IObservador observador) {
        //Bloquea la ventana de atrás
        super(padre, "Registrar Cliente", true);
        this.observador = observador;
        
        //Configuración
        setLayout(new BorderLayout());

        //Panel de registro
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));
        
        //Campos de texto
        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        JTextField tFApellidoP = UtilGeneral.crearCampoFormulario(panel, "Apellido paterno", tamanio);
        JTextField tFApellidoM = UtilGeneral.crearCampoFormulario(panel, "Apellido materno", tamanio);
        JTextField tFTelefono = UtilGeneral.crearCampoFormulario(panel, "Teléfono", tamanio);
        JTextField tFCorreo = UtilGeneral.crearCampoFormulario(panel, "Correo", tamanio);
        
        //Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Aceptar");
        panelBotones.add(botonAceptar);

        //Agregamos todo al JDialog
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //Evento del botón Aceptar
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Extre valores
                String nombre = tFNombre.getText().trim();
                String apellidoP = tFApellidoP.getText().trim();
                String apellidoM = tFApellidoM.getText().trim();
                String telefono = tFTelefono.getText().trim();
                String correo = tFCorreo.getText().trim();

                //Validaciones
                if (nombre.isEmpty() || apellidoP.isEmpty() || apellidoM.isEmpty() || telefono.isEmpty()) {
                    JOptionPane.showMessageDialog(RegistrarCliente.this, 
                        "Todos los campos son obligatorios (excepto el correo)", 
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //Confirmación
                int opcion = JOptionPane.showConfirmDialog(
                        RegistrarCliente.this,
                        "¿Deseas registrar este cliente?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    //Crea el DTO
                    ClienteFrecuenteDTO cliente = new ClienteFrecuenteDTO();
                    cliente.setNombres(nombre);
                    cliente.setApellidoPaterno(apellidoP);
                    cliente.setApellidoMaterno(apellidoM);
                    cliente.setTelefono(telefono);
                    cliente.setCorreo(correo); 

                    //Agrega al cliente al sistema
                    try {
                        CoordinadorNegocio.getInstance().registrarCliente(cliente);
                        JOptionPane.showMessageDialog(RegistrarCliente.this, "Cliente creado correctamente");
                    } catch (NegocioException ex) {
                        JOptionPane.showMessageDialog(RegistrarCliente.this, ex.getMessage());
                    }
                    
                    
                    //Notifica al observador sobre el nuevo cliente para que lo registre
                    if (RegistrarCliente.this.observador != null) {
                        RegistrarCliente.this.observador.notificarCambio();
                    }
                }
            }
        });
        
        //Configuración final y cierre 
        this.pack();
        this.setLocationRelativeTo(padre);
    }
}