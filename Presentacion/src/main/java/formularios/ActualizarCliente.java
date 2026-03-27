package formularios;
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

 //####IGNORAR POR AHORA#####

/**
 * JDialog para actualizar un cliente
 * @author Andre
 */
public class ActualizarCliente extends JDialog {
    public ActualizarCliente(JFrame padre) {
        //Bloquea la ventana de atrás
        super(padre, "Actualizar Cliente", true);
        
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

        //Agrega todo al JDialog
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //Actualizar
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
                    JOptionPane.showMessageDialog(ActualizarCliente.this, 
                        "Todos los campos son obligatorios (excepto el correo)", 
                        "Campos vacíos", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                //Esto va pa negocio creo
                /**
                 * if (!telefono.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(RegistrarCliente.this, 
                        "El teléfono debe tener exactamente 10 dígitos", 
                        "Formato incorrecto", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                 */

                //Confirmación
                int opcion = JOptionPane.showConfirmDialog(
                        ActualizarCliente.this,
                        "¿Actualizar al cliente?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {
                    //Crea el DTO
                    ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
                    dto.setNombres(nombre);
                    dto.setApellidoPaterno(apellidoP);
                    dto.setApellidoMaterno(apellidoM);
                    dto.setTelefono(telefono);
                    dto.setCorreo(correo); 

                    //Agrega
                    CoordinadorNegocio.getInstance().actualizarCliente(dto);
                    JOptionPane.showMessageDialog(ActualizarCliente.this, "Cliente actualizado correctamente");
                }
            }
        });
        
        //Configuración final y cierre 
        this.pack();
        this.setLocationRelativeTo(padre);
    }
}