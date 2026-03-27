package pantallas;
import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import formularios.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;
import observadores.IObservador;

/**
 * Pantalla que muestra la tabla de clientes
 * Observa al formulario de registro de cliente
 * @author Andre
 */
public class AdministrarClientes extends JFrame implements IObservador{
    private JTable tabla;
    
    public AdministrarClientes() {
        UtilGeneral.configurarFrame("Administrar clientes", this);
        
        //Crea el panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        
        //Búsqueda por nombre
        panelBusqueda.add(new JLabel("Buscar"));
        JTextField textoBuscar = UtilGeneral.crearCampoTexto(20);
        textoBuscar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        textoBuscar.setPreferredSize(new Dimension(250, 35)); // Un poco más alto
        textoBuscar.setToolTipText("Ingrese el término a buscar...");
        panelBusqueda.add(textoBuscar);
        
        //Botones para cambiar el criterio de búsqueda
        JButton botonBuscarNombre = UtilBoton.crearBoton("Nombre");
        JButton botonBuscarTelefono = UtilBoton.crearBoton("Teléfono");
        JButton botonBuscarCorreo = UtilBoton.crearBoton("Correo");
        panelBusqueda.add(botonBuscarNombre);
        panelBusqueda.add(botonBuscarTelefono);
        panelBusqueda.add(botonBuscarCorreo);
        
        //Crea la tabla
        String[] columnas = {"ID", "Nombres", "Apellido paterno", "Apellido materno", "Teléfono", "Correo", "Fecha de registro", "Gasto total", "Puntos de fidelidad", "Visitas"};
        tabla = UtilGeneral.crearTabla(columnas);
        
        //Agrega para scrollear
        JScrollPane scrollPane = new JScrollPane(tabla);

        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //Crea los botones
        JButton botonAgregar = UtilBoton.crearBotonDialogo("Nuevo Cliente", () -> new RegistrarCliente(this, this));
        JButton botonEditar = UtilBoton.crearBoton("Editar cliente");
        JButton botonEliminar = UtilBoton.crearBoton("Eliminar cliente");
        JButton botonRegresar = UtilBoton.crearBotonNavegar("Regresar", this, MenuPrincipal::new);

        //Agrega los botones
        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonRegresar);

        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);   
        
        //Llena la tabla
        llenarTabla();
    }
    
    
    
    /**
     * Escucha el llamado el formulario y activa la lógica de registrar al cliente en la tabla
     * Hace una lista con el único elemento, esto para poder trabajar bien con el método
     */
    @Override
    public void notificarCambio() {
        ClienteFrecuenteDTO dto = CoordinadorNegocio.getInstance().getClienteFrecuente();
        List<ClienteFrecuenteDTO> lista = new ArrayList<>();
        lista.add(dto);
        UtilGeneral.registrarTabla(tabla, lista, c -> new Object[]{
                        c.getId(),
                        c.getNombres(),
                        c.getApellidoPaterno(),
                        c.getApellidoMaterno(),
                        c.getTelefono(),
                        c.getCorreo(),
                        c.getVisitas(),
                        "$ " + c.getGastoTotal(),
                        c.getPuntosFidelidad()
                    });
    }
    
    
    
    /**
     * Llena la tabla con los registros
     * Obtiene del coordinador todos los registros de la BD
     * Con el lambda CASTEA a la clase en concreto para aplicar getters
     */
    public void llenarTabla() {
        List<ClienteFrecuenteDTO> lista = CoordinadorNegocio.getInstance().consultarClientesFrecuentes();
        UtilGeneral.registrarTabla(tabla, lista, (ClienteFrecuenteDTO c) -> new Object[]{
                        c.getId(),
                        c.getNombres(),
                        c.getApellidoPaterno(),
                        c.getApellidoMaterno(),
                        c.getTelefono(),
                        c.getCorreo(),
                        c.getVisitas(),
                        "$ " + c.getGastoTotal(),
                        c.getPuntosFidelidad()
                    });
    }
}