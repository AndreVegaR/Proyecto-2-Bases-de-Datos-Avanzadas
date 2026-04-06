package Coordinadores;

import BO.ClienteBO;
import BO.IngredienteBO;
import DTOs.IngredienteDTO;
import Enumeradores.UnidadMedida;
import BO.ComandaBO;
import BO.ProductoBO;
import DTOs.ClienteDTO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.ProductoDTO;
import Enumeradores.EstadoProducto;
import Utilerias.Constantes;
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

    // Ingrediente seleccionado
    private IngredienteDTO ingrediente;

    public void setIngrediente(IngredienteDTO ingrediente) {
        this.ingrediente = ingrediente;
    }

    public IngredienteDTO getIngrediente() {
        return ingrediente;
    }

    // Comanda seleccionada
    private ComandaDTO comanda = new ComandaDTO();

    public void setComanda(ComandaDTO comanda) {
        this.comanda = comanda;
    }

    public ComandaDTO getComanda() {
        return comanda;
    }

    // Singleton
    private static CoordinadorNegocio instancia;

    private CoordinadorNegocio() {}

    public static CoordinadorNegocio getInstance() {
        if (instancia == null) {
            instancia = new CoordinadorNegocio();
        }
        return instancia;
    }

    // CLIENTE
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

    // INGREDIENTE
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

    //COMANDA
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
        if (this.comanda != null) { // 🔧 corregido (antes decía cliente)
            c = ComandaBO.getInstance().actualizarComanda(comanda);
        }
        this.comanda = null;
        return c;
    }

    @Override
    public List<ComandaDTO> consultarComandas() {
        return ComandaBO.getInstance().consultarComandas();
    }

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

    @Override
    public List<ProductoDTO> verTodos() {
        return ProductoBO.getInstance().verTodosLosProductos();
    }

    @Override
    public ProductoDTO actualizarProducto(ProductoDTO producto) {
        return ProductoBO.getInstance().actualizarProducto(producto);
    }

    @Override
    public ProductoDTO registrarProducto(ProductoDTO producto) {
        return ProductoBO.getInstance().agregarProducto(producto);
    }

    @Override
    public List<IngredienteDTO> verTodosLosIngredientes() {
        return IngredienteBO.getInstance().obtenerTodos();
    }

    @Override
    public ProductoDTO buscarProductoPorID(Long id) {
        return ProductoBO.getInstance().buscarProductoPorId(id);
    }

    @Override
    public ProductoDTO cambiarEstado(Long id, ProductoDTO.EstadoProducto estado) {
        return ProductoBO.getInstance().cambiarEstado(id, EstadoProducto.valueOf(estado.name().toUpperCase()));
    }

  


    
}