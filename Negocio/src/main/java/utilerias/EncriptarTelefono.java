/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilerias;

import java.util.Base64;

/**
 *Utileria para encriptar y desencriptar telefonos de clientes
 * @author Jazmin
 */
public class EncriptarTelefono {
    /**
     * Encripta un telefono en texto claro
     * @param telefono
     * @return 
     */
    public static String encriptar(String telefono){
        if(telefono == null || telefono.isBlank()){
            return telefono;
        }
        return Base64.getEncoder().encodeToString(telefono.getBytes());
    }
    /**
     * Desencripta un telefono leido desde la base de datos
     * @param telefonoEncriptado
     * @return 
     */
    public static String desencriptar(String telefonoEncriptado){
        if(telefonoEncriptado == null || telefonoEncriptado.isBlank()){
            return telefonoEncriptado;
        }
        return new String(Base64.getDecoder().decode(telefonoEncriptado));
    }
    
}
