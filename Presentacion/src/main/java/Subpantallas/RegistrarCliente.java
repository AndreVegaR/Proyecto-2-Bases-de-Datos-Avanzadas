/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Subpantallas;

import Coordinador.CoordinadorPantallas;
import Coordinador.ICoordinadorPantallas;
import DTOs.ClienteFrecuenteDTO;
import Principal.MenuPrincipal;
import Utilerias.UtilBoton;
import Utilerias.UtilGeneral;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;

/**
 * SUBPANTALLA
 * @author Angel
 * Pantalla para registrar un cliente y que es llamada desde la pantalla principal de administrar clientes
 */
public class RegistrarCliente extends JFrame{
    
    //Referencia a la interfaz del coordinador
    
    private ICoordinadorPantallas coordinador;
    public RegistrarCliente(ICoordinadorPantallas coordinador){
        // Guardamos el coordinador que le pasamos desde el metodo mostrarPantalla del coordinador de pantallas
        //El metodo recibe el coordinador y es para poder usarlo dentro de esta pantalla
        //Es decir para usar el coordinador creado y mandado a este metodo
        this.coordinador = coordinador;
        UtilGeneral.configurarFrame("Administrar clientes", this);
        
        //Crea el panel de búsqueda
        JPanel panelRegistrar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
       
        
        //JTextField para crear un cliente frecuente
        JTextField nombre = new JTextField("Nombre");
        JTextField telefono = new JTextField("Teléfono");
        JTextField apellidoPaterno = new JTextField("Apellido Paterno");
        JTextField apellidoMaterno = new JTextField("Apellido Materno");
        JTextField correo = new JTextField("Correo");
   
        //Los agregamos al panel
        panelRegistrar.add(nombre);
        panelRegistrar.add(telefono);
        panelRegistrar.add(correo);
        panelRegistrar.add(apellidoPaterno);
        panelRegistrar.add(apellidoMaterno);
       
        //Crea panel de botones de abajo
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));

        //Crear los botones
        JButton botonAceptar = UtilBoton.crearBoton("Aceptar");
        JButton botonAtras = UtilBoton.crearBotonNavegar("Atras",this, MenuPrincipal::new);

        //Agrega los botones
        panelBotones.add(botonAceptar);
        panelBotones.add(botonAtras);
        //Agrega todo al frame
        add(panelRegistrar, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    
        /*
        Boton que al asignarle texto a los JTextField guarda al cliente
        */
        botonAceptar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {    
                
          //Ventana emergente para decidir si agregarlo o no
          int opcion = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas registrar este cliente?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );
          //Se registra
            if(opcion == JOptionPane.YES_OPTION){

            ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
            dto.setNombres(nombre.getText());
            dto.setApellidoPaterno(apellidoPaterno.getText());
            dto.setApellidoMaterno(apellidoMaterno.getText());
            dto.setCorreo(correo.getText());
            dto.setTelefono(telefono.getText());

            coordinador.registrarClienteFrecuente(dto);

            JOptionPane.showMessageDialog(null, "Cliente creado correctamente");
        }
            //Cerramos la ventana 
             dispose();
    }             
    });
    
}
}

