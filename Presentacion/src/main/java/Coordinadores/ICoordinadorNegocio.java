package Coordinadores;
import DTOs.ClienteFrecuenteDTO;
import java.util.List;

/**
 * @author Angel
 * Interfaz para un control de las pantallas
 */
public interface ICoordinadorNegocio {
    
    //Getter y setter de clienteFrecuente
    void setClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente);
    ClienteFrecuenteDTO getClienteFrecuente();
    
    /**
     * Registra un cliente frecuente al sistema
     * 
     * @param clienteFrecuente 
     */
    void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente);
    
    /**
     * Obtiene todos los clientes
     * @return 
     */
    public List<ClienteFrecuenteDTO> consultarClientesFrecuentes();
}