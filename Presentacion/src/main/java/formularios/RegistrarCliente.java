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

/**
 * JDialog para registrar un cliente
 * @author Angel
 */
public class RegistrarCliente extends JDialog {

    // Cambiamos el constructor para que no pida el Coordinador (lo sacamos del Singleton)
    public RegistrarCliente(JFrame padre) {
        // Configuramos el JDialog como modal (bloquea la ventana de atrás)
        super(padre, "Registrar Cliente", true);
        
        //Configuración básica del diálogo
        setLayout(new BorderLayout());

        //Panel de registro
        JPanel panelRegistrar = new JPanel();
        panelRegistrar.setLayout(new javax.swing.BoxLayout(panelRegistrar, javax.swing.BoxLayout.Y_AXIS));
        panelRegistrar.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));
        
        //Campos de texto
        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panelRegistrar, "Nombre", tamanio);
        JTextField tFApellidoP = UtilGeneral.crearCampoFormulario(panelRegistrar, "Apellido paterno", tamanio);
        JTextField tFApellidoM = UtilGeneral.crearCampoFormulario(panelRegistrar, "Apellido materno", tamanio);
        JTextField tFTelefono = UtilGeneral.crearCampoFormulario(panelRegistrar, "Teléfono", tamanio);
        JTextField tFCorreo = UtilGeneral.crearCampoFormulario(panelRegistrar, "Correo", tamanio);
        
        //Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Aceptar");
        panelBotones.add(botonAceptar);

        //Agregamos todo al JDialog
        add(panelRegistrar, BorderLayout.CENTER);
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
                        RegistrarCliente.this,
                        "¿Deseas registrar este cliente?",
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
                    CoordinadorNegocio.getInstance().registrarClienteFrecuente(dto);
                    JOptionPane.showMessageDialog(RegistrarCliente.this, "Cliente creado correctamente");
                }
            }
        });
        
        //Configuración final y cierre
        this.pack();
        this.setLocationRelativeTo(padre);
    }
}