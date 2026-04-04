package Coordinadores;
import DTOs.ClienteDTO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
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
    
    
    
    //Métodos de comandas
    ComandaDTO consultarComanda(Long id);
    
    ComandaDTO registrarComanda(ComandaDTO comanda);
    
    ComandaDTO actualizarComanda(ComandaDTO comanda);
    
    List<ComandaDTO> consultarComandas();
    
    List<DetallesComandaDTO> consultarDetalles();
}