package Coordinadores;
import BO.ClienteFrecuenteBO;
import DTOs.ClienteFrecuenteDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angel
 * Coordinador que unifica lógica de negocio
 * Utiliza un singleton
 */
public class CoordinadorNegocio implements ICoordinadorNegocio {
    private ClienteFrecuenteDTO clienteFrecuente = new ClienteFrecuenteDTO();
    public void setClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente) {
        this.clienteFrecuente = clienteFrecuente;
    }
    public ClienteFrecuenteDTO getClienteFrecuente(){
        return clienteFrecuente;
    }


    //Única instancia
    private static CoordinadorNegocio instancia;
    
    //Constructor privado
    private CoordinadorNegocio() {}
    
    /**
     * Singleton
     * Si se llama por primera vez, crea la instancia
     * Si ya existía, solo la regresa
     * 
     * @return la instancia lista
     */
    public static CoordinadorNegocio getInstance() {
        if (instancia == null) {
            instancia = new CoordinadorNegocio();
        }
        return instancia;
    }
    
    
    
    /**
     * Recibe un DTO con los datos del cliente y los guarda mediante el BO de cliente
     * También lo guarda para ser usado
     * 
     * @param clienteFrecuente 
     */
    @Override
    public void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente){
        setClienteFrecuente(clienteFrecuente);
        ClienteFrecuenteBO.getInstance().guardarCliente(clienteFrecuente);
    }
    
    
    
    /**
     * Consulta todos los clientes de la BD
     * 
     * @return lista de clientes ya registrados
     */
    @Override
    public List<ClienteFrecuenteDTO> consultarClientesFrecuentes() {
        return ClienteFrecuenteBO.getInstance().verClientes();
    }
    
    
    
    /**
     * Actualiza un cliente
     * 
     * @param clienteFrecuente
     * @return el cliente actualizado
     */
    @Override
    public ClienteFrecuenteDTO actualizarCliente(ClienteFrecuenteDTO clienteFrecuente) {
        return ClienteFrecuenteBO.getInstance().modificarCliente(clienteFrecuente);
    }
}