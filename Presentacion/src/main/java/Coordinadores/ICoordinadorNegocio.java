package Coordinadores;
import DTOs.ClienteFrecuenteDTO;

/**
 * @author Angel
 * Interfaz para un control de las pantallas
 */
public interface ICoordinadorNegocio {
    
    /**
     * Registra un cliente frecuente al sistema
     * 
     * @param clienteFrecuente 
     */
    void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente);
}