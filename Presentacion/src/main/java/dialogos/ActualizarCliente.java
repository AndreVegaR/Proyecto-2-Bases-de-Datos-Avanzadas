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
import java.time.LocalDateTime;

/**
 * JDialog para actualizar un cliente
 * @author Andre
 */
public class ActualizarCliente extends JDialog {
    private IObservador observador;
    
    public ActualizarCliente(JFrame padre, IObservador observador) {
        //Bloquea la ventana de atrás
        super(padre, "Actualizar Cliente", true);
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

        //Agrega todo al JDialog
        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        //Actualizar
        botonAceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Confirmación
                int opcion = JOptionPane.showConfirmDialog(
                        ActualizarCliente.this,
                        "¿Actualizar al cliente?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );

                if (opcion == JOptionPane.YES_OPTION) {

                    // 1. Obtener los datos del cliente que seleccionaste (EL QUE TIENE EL ID REAL)
                    ClienteDTO clienteOriginal = CoordinadorNegocio.getInstance().getCliente();

                    // 2. Crear el objeto actualizado
                    ClienteFrecuenteDTO clienteActualizado = new ClienteFrecuenteDTO();

                    //Extre valores
                    String nombre = tFNombre.getText().trim();
                    String apellidoP = tFApellidoP.getText().trim();
                    String apellidoM = tFApellidoM.getText().trim();
                    String telefono = tFTelefono.getText().trim();
                    String correo = tFCorreo.getText().trim();
                    LocalDateTime fechaRegistro = clienteActualizado.getFechaRegistro();

                    
                    //Pasa los atributos del cliente seleccionado, pero actualiza si reconoce si hubo cambios en el formulario
                    clienteActualizado.setId(clienteOriginal.getId()); 
                    clienteActualizado.setNombres(nombre.isBlank() ? clienteOriginal.getNombres() : nombre);
                    clienteActualizado.setApellidoPaterno(apellidoP.isBlank() ? clienteOriginal.getApellidoPaterno() : apellidoP);
                    clienteActualizado.setApellidoMaterno(apellidoM.isBlank() ? clienteOriginal.getApellidoMaterno() : apellidoM);
                    clienteActualizado.setTelefono(telefono.isBlank() ? clienteOriginal.getTelefono() : telefono);
                    clienteActualizado.setCorreo(correo.isBlank() ? clienteOriginal.getCorreo() : correo);
                    clienteActualizado.setFechaRegistro(fechaRegistro);

                    //Lo manda a actualizar
                    try {
                        CoordinadorNegocio.getInstance().actualizarCliente(clienteActualizado);
                        JOptionPane.showMessageDialog(ActualizarCliente.this, "Cliente creado correctamente");
                    } catch (NegocioException ex) {
                        JOptionPane.showMessageDialog(ActualizarCliente.this, ex.getMessage());
                    }

                    //Observador
                    if (ActualizarCliente.this.observador != null) {
                        ActualizarCliente.this.observador.notificarCambio();
                    }
                    
       
                }
            }
        });
        
        //Configuración final y cierre 
        this.pack();
        this.setLocationRelativeTo(padre);
    }
}