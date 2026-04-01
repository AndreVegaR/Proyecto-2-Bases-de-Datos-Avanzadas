package Coordinadores;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
import java.util.List;

/**
 * @author Angel
 * Interfaz para un control de las pantallas
 */
public interface ICoordinadorNegocio {
    

    //Métodos de clientes
    public void registrarCliente(ClienteDTO cliente);
    
    /**
     * 
     * public void eliminarClienteFrecuente(ClienteFrecuenteDTO cliente);
    
    public List<ClienteFrecuenteDTO> consultarClientesFrecuentes();
    
    public ClienteFrecuenteDTO actualizarCliente(ClienteFrecuenteDTO clienteFrecuente);
    
    ClienteFrecuenteDTO consultarCliente(Long id);
     * 
     */
}