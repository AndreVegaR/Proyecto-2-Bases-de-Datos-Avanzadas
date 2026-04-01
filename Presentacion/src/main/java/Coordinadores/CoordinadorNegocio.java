package Coordinadores;
import BO.ClienteBO;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
import java.util.List;

/**
 * @author Angel
 * Coordinador que unifica lógica de negocio
 * Utiliza un singleton
 * Los atributos (como el cliente) los guarda aquí para trabajar con ellos después
 * Esto se hace con la lógica de al hacer clic en un registro de la tabla se guarda en el atributo especificado
 */
public class CoordinadorNegocio implements ICoordinadorNegocio {
    
    //El cliente seleccionado en un momento del programa (clic en registro de la tabla)
    private ClienteDTO cliente = new ClienteDTO();
    public void setClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente) {
        this.cliente = cliente;
    }
    public ClienteDTO getClienteFrecuente(){
        return cliente;
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
     * Llama al método de BO simplemente
     * 
     * @param cliente a registrar
     */
    @Override
    public void registrarCliente(ClienteDTO cliente) {
        ClienteBO.getInstance().registrarCliente(cliente);
    }
    
    
    
    
    
    
    /**
     * Consulta todos los clientes de la BD
     * 
     * @return lista de clientes ya registrados
     */
    /**
     *  @Override
    public List<ClienteFrecuenteDTO> consultarClientesFrecuentes() {
        return ClienteBO.getInstance().verClientes();
    }
     */
   
    
    
    
    /**
     * Actualiza un cliente
     * 
     * @param clienteFrecuente
     * @return el cliente actualizado
     */
    /**
     * @Override
    public ClienteFrecuenteDTO actualizarCliente(ClienteFrecuenteDTO clienteFrecuente) {
        return ClienteBO.getInstance().modificarCliente(clienteFrecuente);
    }
     * 
     * 
     */
    
    
    
    /**
     * Elimina un cliente
     * 
     * @param clienteFrecuente
     */
    /**
     * 
     * @Override
    public void eliminarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente) {
        //ClienteBO.getInstance().eliminarCliente(clienteFrecuente.getId());
    }
     * 
     */
    
    
    
    /**
     * 
     * @Override
    public ClienteFrecuenteDTO consultarCliente(Long id) {
        //return ClienteFrecuenteBO.getInstance().consultarCliente(id);
        return null;
    }
     * 
     */
}