package pantallas;
import Coordinadores.CoordinadorPantallas;
import Coordinadores.ICoordinadorPantallas;
import Coordinadores.CoordinadorPantallas;
import Coordinadores.ICoordinadorPantallas;
import Principal.MenuPrincipal;
import formularios.RegistrarCliente;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * Pantalla que muestra la tabla de clientes
 * @author Andre
 */
public class AdministrarClientes extends JFrame {
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
        
        //Arreglo con las partes de la tabla
        String[] columnas = {"Nombre completo", "Teléfono", "Correo", "Fecha de registro", "Tipo"};
        
        JTable tabla = UtilGeneral.crearTabla(columnas);
        
        //Agrega para scrollear
        JScrollPane scrollPane = new JScrollPane(tabla);

        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //Crea los botones
        JButton botonAgregar = UtilBoton.crearBotonDialogo("Nuevo Cliente", () -> new RegistrarCliente(this));
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
    }
}