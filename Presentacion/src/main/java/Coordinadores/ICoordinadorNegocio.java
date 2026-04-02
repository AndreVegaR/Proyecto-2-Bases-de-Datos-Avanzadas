package Coordinadores;
import DTOs.ClienteDTO;
import java.util.List;

/**
 * @author Angel
 * Interfaz para un control de las pantallas
 * Establece el contrato entre negocio y presentación
 * Es un patrón fachada
 */
public interface ICoordinadorNegocio {
    
    //Métodos de clientes
    ClienteDTO consultarCliente(Long id);
    
    ClienteDTO registrarCliente(ClienteDTO cliente);
    
    ClienteDTO actualizarCliente(ClienteDTO cliente);
    
    ClienteDTO eliminarCliente();
    
    List<ClienteDTO> consultarClientes();
}