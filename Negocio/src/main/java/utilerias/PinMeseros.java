/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.util.Arrays;
import java.util.List;

/**
 *
 * Clase utilieria temporal para probar
 */
public class PinMeseros {
    
    private static final List<String> PINES = Arrays.asList("1", "2", "3", "4");
    
    public static boolean validarPin(String pinIngresado){
        if(pinIngresado == null){
            return false;
        }
        String pin = pinIngresado.trim();
        return PINES.contains(pin);
    }
}
