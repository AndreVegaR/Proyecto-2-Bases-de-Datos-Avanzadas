/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package formularios;

import Coordinadores.CoordinadorNegocio;
import DTOs.ClienteFrecuenteDTO;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import observadores.IObservador;

/**
 * JDialog para eliminar un cliente
 *
 * @author Jazmin
 */
public class EliminarCliente extends JDialog {

    private IObservador observador;

    public EliminarCliente(JFrame padre, IObservador observador, ClienteFrecuenteDTO cliente) {
        super(padre, "Eliminar Cliente", true);
        this.observador = observador;

        setLayout(new BorderLayout());

        // Panel de datos (solo lectura)
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new javax.swing.border.EmptyBorder(20, 25, 20, 25));

        int tamanio = 20;
        JTextField tFNombre = UtilGeneral.crearCampoFormulario(panel, "Nombre", tamanio);
        JTextField tFApellidoP = UtilGeneral.crearCampoFormulario(panel, "Apellido paterno", tamanio);
        JTextField tFApellidoM = UtilGeneral.crearCampoFormulario(panel, "Apellido materno", tamanio);
        JTextField tFTelefono = UtilGeneral.crearCampoFormulario(panel, "Teléfono", tamanio);
        JTextField tFCorreo = UtilGeneral.crearCampoFormulario(panel, "Correo", tamanio);

        // Precarga los datos del cliente
        tFNombre.setText(cliente.getNombres());
        tFApellidoP.setText(cliente.getApellidoPaterno());
        tFApellidoM.setText(cliente.getApellidoMaterno());
        tFTelefono.setText(cliente.getTelefono());
        tFCorreo.setText(cliente.getCorreo() != null ? cliente.getCorreo() : "");

        // Solo lectura
        tFNombre.setEditable(false);
        tFApellidoP.setEditable(false);
        tFApellidoM.setEditable(false);
        tFTelefono.setEditable(false);
        tFCorreo.setEditable(false);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        JButton botonEliminar = UtilBoton.crearBoton("Eliminar");
        JButton botonCancelar = UtilBoton.crearBoton("Cancelar");
        panelBotones.add(botonEliminar);
        panelBotones.add(botonCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Evento botón Eliminar
        botonEliminar.addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(
                    EliminarCliente.this,
                    "¿Deseas eliminar este cliente?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                CoordinadorNegocio.getInstance().eliminarClienteFrecuente(cliente);
                JOptionPane.showMessageDialog(EliminarCliente.this, "Cliente eliminado correctamente");
                if (observador != null) {
                    observador.notificarCambio();
                }
                CoordinadorNegocio.getInstance().setClienteFrecuente(null);
                dispose();
            }
        });

        botonCancelar.addActionListener(e -> dispose());

        this.pack();
        this.setLocationRelativeTo(padre);
    }
}
