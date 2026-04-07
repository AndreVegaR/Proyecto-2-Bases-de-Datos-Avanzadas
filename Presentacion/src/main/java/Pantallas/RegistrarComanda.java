package Pantallas;
import Coordinadores.CoordinadorNegocio;
import Coordinadores.CoordinadorPantallas;
import DTOs.ClienteDTO;
import DTOs.MesaDTO;
import Principal.MenuPrincipal;
import Utilerias.Constantes;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import dialogos.ElegirMesa;
import javax.swing.*;
import java.awt.*;

/**
 * Registra una comanda en el sistema
 * Desde aquí se hacen las siguientes funciones:
 * -Se elige un cliente (opcionalmente)
 * -Se elige una mesa
 * -Se eligen los productos
 * -Se muestra un resumen al final
 * @author Andre
 */
public class RegistrarComanda extends JFrame {

    //Atributos para ser usados fuera del constructor
    JTable tabla;
    JLabel labelCliente = new JLabel(Constantes.SIN_CLIENTE);
    JLabel labelMesa = new JLabel("Sin mesa");
    
    public RegistrarComanda() {
        
        //Actualiza cada vez que se llega a esta pantalla
        actualizarLabels();
        
        //Configuraciones inicialaes
        UtilGeneral.configurarFrame("Registrar Comanda", this);
        setLayout(new BorderLayout());

        //Panel de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 25));
        panelBusqueda.setBackground(new Color(235, 245, 251)); 
        panelBusqueda.setPreferredSize(new Dimension(0, 100));
        
        //Label del nombre del cliente
        panelBusqueda.add(labelCliente);
        
        //Botones que buscan clientes y mesas
        JButton botonBuscarClientes = UtilBoton.crearBotonNavegar("Administrar clientes", this, AdministrarClientes::new);
        JButton botonElegirMesa = UtilBoton.crearBotonDialogo("Elegir mesa", () -> new ElegirMesa(this));
        panelBusqueda.add(botonBuscarClientes);
        panelBusqueda.add(botonElegirMesa);
        
        //Label de la mesa seleccionada
        panelBusqueda.add(labelMesa);
        
        //Panel de la tabla
        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBackground(Constantes.COLOR_FONDO);
        
        //Crea la tabla donde aparecerán los detalles de productos
        String[] camposTabla = Constantes.CAMPOS_TABLA_DETALLES;
        tabla = UtilGeneral.crearTabla(camposTabla);
        JScrollPane scroll = new JScrollPane(tabla);
        panelTabla.add(scroll, BorderLayout.CENTER);
        
        //Panel de opcioens inferiores
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        panelOpciones.setBackground(new Color(242, 243, 244)); 
        panelOpciones.setPreferredSize(new Dimension(0, 90));
        
        /**
         * Crea y configura el botón de regresar al menú principal
         * Se borrará la comanda que se estaba creando
         */
        JButton btnRegresar = UtilBoton.crearBoton("Regresar");
        btnRegresar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Desea salir? Se borrará lo que lleva de esta comanda",
                "Dejar la comanda",             
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE 
            );
            
            //Si sí quiere salir, regresa al menú principal
            if (respuesta == JOptionPane.YES_OPTION) {
                CoordinadorPantallas.getInstance().navegar(RegistrarComanda.this, MenuPrincipal::new);
            }
        });
        
        
        JButton btnBuscarProducto = UtilBoton.crearBoton("Seleccionar Producto");
        //JButton btnBuscarProducto = UtilBoton.crearBotonNavegar("Buscar Producto", this, AdministrarProductos::new);
        
        /**
         * Crea y configura el botón de continuar
         * De ser cliqueado te manda a la pantalla de ResumenComanda
         * Pero te notifica si no has seleccionado mesa todavía
         */
        JButton btnContinuar = UtilBoton.crearBoton("Continuar");
        btnContinuar.addActionListener(e -> {
            if (CoordinadorNegocio.getInstance().getMesa() == null) {
                UtilGeneral.dialogoAviso(RegistrarComanda.this, "Elija una mesa primero");
            }
            //CoordinadorPanallas.getInstance().navegar(RegistrarComanda.this, ResumenComanda::new);
        });
        
        //Agrega al panel de botones
        panelOpciones.add(btnRegresar);
        panelOpciones.add(btnBuscarProducto);
        panelOpciones.add(btnContinuar);
        
        //Agrega todo al frame
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelTabla, BorderLayout.CENTER);
        add(panelOpciones, BorderLayout.SOUTH);
    }
    
    
    
    /*
     * Si en cualquiera se elige un cliente o mesa, se dispara este método
     * Cambia los labels con el nombre del cliente y el número de mesa seleccionados
     */
    public void actualizarLabels() {
        
        //Cambia el label de cliente
        ClienteDTO cliente = CoordinadorNegocio.getInstance().getCliente();
        if (cliente != null) {
            String infoCliente = cliente.getNombres() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno();
            labelCliente.setText(infoCliente);
        }
        else {
            labelCliente.setText(Constantes.SIN_CLIENTE);
        }
        
        //Cambia el label de la mesa
        MesaDTO mesa = CoordinadorNegocio.getInstance().getMesa();
        if (mesa != null) {
            int numero = mesa.getNumero();
            labelMesa.setText("Mesa " + numero);
        }
    } 
}