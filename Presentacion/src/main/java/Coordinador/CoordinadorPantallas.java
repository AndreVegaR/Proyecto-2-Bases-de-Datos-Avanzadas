/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Coordinador;

import BO.ClienteFrecuenteBO;
import DTOs.ClienteFrecuenteDTO;
import Subpantallas.RegistrarCliente;

/**
 *
 * @author Angel
 * El coordinador sirve para hacer unica la interacción de las pantallas 
 * 
 */
public class CoordinadorPantallas implements ICoordinadorPantallas{
    
    ClienteFrecuenteBO clienteBO = new ClienteFrecuenteBO();
   
    //En esta parte se mostrará la pantalla de registrar a un cliente
    public void mostrarPantalla(){
        //this es el coordinador actual
     RegistrarCliente pantallaRegistrarCliente = new RegistrarCliente(this);
     pantallaRegistrarCliente.setVisible(true);
        
    }
    
    //Recibe un DTO con los datos del cliente y los guarda mediante el BO de cliente
    public void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente){
        clienteBO.guardarCliente(clienteFrecuente);
    }
}
