package dialogos;
import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
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
        
        //Panel inferiorB
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonAceptar = UtilBoton.crearBoton("Registrar");
        panelInferior.add(botonAceptar);
        
        //Crea un Combobox
        JComboBox<String> comboClientes = new JComboBox<>(Constantes.TIPOS_CLIENTES);
        comboClientes.setSelectedIndex(0);
        panelInferior.add(comboClientes);

        //Agregamos todo al JDialog
        add(panel, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

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
                        "¿Desea registrar este cliente?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    
                    //Configura el cliente base
                    ClienteDTO cliente = new ClienteDTO();
                    cliente.setNombres(nombre);
                    cliente.setApellidoPaterno(apellidoP);
                    cliente.setApellidoMaterno(apellidoM);
                    cliente.setTelefono(telefono);
                    cliente.setCorreo(correo);
                    
                    //Crea el DTO y añade información a la subclase en específico
                    String tipoCliente = (String) comboClientes.getSelectedItem();
                    ClienteDTO clienteRegistrar = fabricaCliente(tipoCliente, cliente);
                    
                    if (clienteRegistrar == null) {
                        UtilGeneral.dialogoAviso(RegistrarCliente.this, "Error: no se reconoció ese tipo de cliente");
                    } else {
                        //Agrega al cliente al sistema
                        try {
                            CoordinadorNegocio.getInstance().registrarCliente(clienteRegistrar);
                            UtilGeneral.dialogoAviso(RegistrarCliente.this, "Registro exitoso");
                        } catch (NegocioException ex) {
                            JOptionPane.showMessageDialog(RegistrarCliente.this, ex.getMessage());
                        }
                    }
                    
                    //Notifica al observador sobre el nuevo cliente
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
    
    
    
    /**
     * Fábrica especializada en dar un tipo de cliente
     * Compara el String resultado de la opción elegida de un combobox
     * Ese String lo compara con los elementos del arreglo TIPOS_CLIENTES
     * Y usa el constructor copia para pasar todos los atributos del base al nuevo
     * 
     * @param tipoCliente a crear
     * @param clienteBase y sus atributos a pasar
     * @return el cliente específicio
     */
    private ClienteDTO fabricaCliente(String tipoCliente, ClienteDTO clienteBase) {
        if (tipoCliente.equals(Constantes.TIPOS_CLIENTES[0])) {
            return new ClienteFrecuenteDTO(clienteBase);
        }
        return null;
    }
}