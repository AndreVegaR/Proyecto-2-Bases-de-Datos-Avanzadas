package Coordinadores;

import BO.ClienteBO;
import BO.IngredienteBO;
import DTOs.ClienteDTO;
import DTOs.IngredienteDTO;
import Enumeradores.UnidadMedida;
import java.util.List;

/**
 * @author Angel Coordinador que unifica lógica de negocio solo redirigiendo el
 * trabajo de BOs Utiliza un singleton Los atributos (como el cliente) los
 * guarda aquí para trabajar con ellos después Esto se hace con la lógica de al
 * hacer clic en un registro de la tabla se guarda en el atributo especificado
 */
public class CoordinadorNegocio implements ICoordinadorNegocio {

    //El cliente seleccionado en un momento del programa (clic en registro de la tabla)
    private ClienteDTO cliente = new ClienteDTO();

    public void setCliente(ClienteDTO cliente) {
        this.cliente = cliente;
    }

    public ClienteDTO getCliente() {
        return cliente;
    }

    private IngredienteDTO ingrediente;

    public void setIngrediente(IngredienteDTO ingrediente) {
        this.ingrediente = ingrediente;
    }

    public IngredienteDTO getIngrediente() {
        return ingrediente;
    }
    //Única instancia
    private static CoordinadorNegocio instancia;

    //Constructor privado
    private CoordinadorNegocio() {
    }

    /**
     * Singleton Si se llama por primera vez, crea la instancia Si ya existía,
     * solo la regresa
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

    @Override
    public IngredienteDTO registrarIngrediente(IngredienteDTO ingrediente) {
        return IngredienteBO.getInstance().registrarIngrediente(ingrediente);
    }

    @Override
    public IngredienteDTO actualizarStock(Long id, Double stock) {
        return IngredienteBO.getInstance().actualizarStock(id, stock);
    }

    @Override
    public IngredienteDTO eliminarIngrediente(Long id) {
        return IngredienteBO.getInstance().eliminarIngrediente(id);
    }

    @Override
    public IngredienteDTO buscarIngredientePorId(Long id) {
        return IngredienteBO.getInstance().buscarPorId(id);
    }

    @Override
    public List<IngredienteDTO> buscarIngredientes(String nombre, UnidadMedida unidadMedida) {
        return IngredienteBO.getInstance().buscarIngredientes(nombre, unidadMedida);
    }
}
