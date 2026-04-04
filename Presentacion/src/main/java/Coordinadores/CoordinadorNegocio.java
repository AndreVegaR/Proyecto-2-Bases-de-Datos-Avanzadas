package Coordinadores;
import BO.ClienteBO;
import BO.ComandaBO;
import DTOs.ClienteDTO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import Utilerias.Constantes;
import java.util.List;

/**
 * @author Angel
 * Coordinador que unifica lógica de negocio solo redirigiendo el trabajo de BOs
 * Utiliza un singleton
 * Los atributos (como el cliente) los guarda aquí para trabajar con ellos después
 * Esto se hace con la lógica de al hacer clic en un registro de la tabla se guarda en el atributo especificado
 */
public class CoordinadorNegocio implements ICoordinadorNegocio {
    
    //El cliente seleccionado en un momento del programa (clic en registro de la tabla)
    private ClienteDTO cliente = new ClienteDTO();
    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }
    public ClienteDTO getCliente(){
        return cliente;
    }
    
    //La comanda selecciona en un momento del programa (clic en el registro de la tabla)
    private ComandaDTO comanda = new ComandaDTO();
    public void setComanda(ComandaDTO comanda) {
        this.comanda = comanda;
    }
    public ComandaDTO getComanda(){
        return comanda;
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
    
    
    
    //Métodos de cliente
    @Override
    public ClienteDTO consultarCliente(Long id) {
        return ClienteBO.getInstance().consultarCliente(id);
    }
    
    @Override
    public ClienteDTO registrarCliente(ClienteDTO cliente) {
        return ClienteBO.getInstance().registrarCliente(cliente);
    }
    
    @Override
    public ClienteDTO actualizarCliente(ClienteDTO cliente) {
        ClienteDTO c = null;
        if (this.cliente != null) {
            c = ClienteBO.getInstance().actualizarCliente(cliente);
        }
        this.cliente = null;
        return c;
    }
    
    //Su único punto de verdad es su variable interna
    @Override
    public ClienteDTO eliminarCliente() {
        if (this.cliente != null) {
            return ClienteBO.getInstance().eliminarCliente(this.cliente.getId());
        }
        return null;
    }

    @Override
    public List<ClienteDTO> consultarClientes() {
        return ClienteBO.getInstance().consultarClientes();
    }
    
    
    
    //Métodos de comanda
    @Override
    public ComandaDTO consultarComanda(Long id) {
        return ComandaBO.getInstance().consultarComanda(id);
    }
    
    @Override
    public ComandaDTO registrarComanda(ComandaDTO comanda) {
        return ComandaBO.getInstance().registrarComanda(comanda);
    }
    
    @Override
    public ComandaDTO actualizarComanda(ComandaDTO comanda) {
        ComandaDTO c = null;
        if (this.cliente != null) {
            c = ComandaBO.getInstance().actualizarComanda(comanda);
        }
        this.cliente = null;
        return c; 
    }
    
    @Override
    public List<ComandaDTO> consultarComandas() {
        return ComandaBO.getInstance().consultarComandas();
    }
    
    //Su único punto de verdad es la variable interna
    @Override
    public List<DetallesComandaDTO> consultarDetalles() {
        if (Constantes.TEST_MODE) {
            return comanda.getDetalles();
        }
        if (comanda != null) {
            return ComandaBO.getInstance().consultarDetalles(comanda.getId());
        }
        return null;
    }
}