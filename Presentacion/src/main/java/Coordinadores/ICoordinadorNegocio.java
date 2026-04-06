package Coordinadores;

import DTOs.ClienteDTO;
import DTOs.IngredienteDTO;
import Enumeradores.UnidadMedida;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.ProductoDTO;
import DTOs.ProductoDTO.EstadoProducto;
import java.util.List;
import DTOs.MesaDTO;

/**
 * @author Angel Interfaz para un control de las pantallas Establece el contrato
 * entre negocio y presentación Es un patrón fachada
 */
public interface ICoordinadorNegocio {

    //Métodos de clientes
    ClienteDTO consultarCliente(Long id);

    ClienteDTO registrarCliente(ClienteDTO cliente);

    ClienteDTO actualizarCliente(ClienteDTO cliente);

    ClienteDTO eliminarCliente();

    List<ClienteDTO> consultarClientes();

    // Métodos de ingredientes
    IngredienteDTO registrarIngrediente(IngredienteDTO ingrediente);

    IngredienteDTO actualizarStock(Long id, Double stock);

    IngredienteDTO eliminarIngrediente(Long id);

    IngredienteDTO buscarIngredientePorId(Long id);

    List<IngredienteDTO> buscarIngredientes(String nombre, UnidadMedida unidadMedida);
    
    List<IngredienteDTO> verTodosLosIngredientes();
    
    //Métodos de comandas
    ComandaDTO consultarComanda(Long id);
    
    ComandaDTO registrarComanda(ComandaDTO comanda);
    
    ComandaDTO actualizarComanda(ComandaDTO comanda);
    
    List<ComandaDTO> consultarComandas();
    
    List<DetallesComandaDTO> consultarDetalles();
    
    //Metodos de productos
    List<ProductoDTO> verTodos();
    
    ProductoDTO actualizarProducto(ProductoDTO producto);
    
    ProductoDTO registrarProducto(ProductoDTO producto);
    
    ProductoDTO buscarProductoPorID(Long id);
    
    ProductoDTO cambiarEstado(Long id,EstadoProducto estado);
    
    //Mesas
    MesaDTO agregarMesa(MesaDTO mesa);
    
    List<MesaDTO> consultarMesas();
    
    MesaDTO actualizarMesa(MesaDTO mesa);
}

