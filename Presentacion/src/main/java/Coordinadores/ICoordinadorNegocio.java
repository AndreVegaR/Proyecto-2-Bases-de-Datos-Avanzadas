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
    
    //Métodos de clientes
    void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente);
    
    public List<ClienteFrecuenteDTO> consultarClientesFrecuentes();
    
    public ClienteFrecuenteDTO actualizarCliente(ClienteFrecuenteDTO clienteFrecuente);
}