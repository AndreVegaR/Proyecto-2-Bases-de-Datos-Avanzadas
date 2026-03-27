/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Coordinador;

import DTOs.ClienteFrecuenteDTO;

/**
 *
 * @author Angel
 * Interfaz para un control de las pantallas
 */
public interface ICoordinadorPantallas {
    
     public void mostrarPantalla();
     
      public void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente);
}
