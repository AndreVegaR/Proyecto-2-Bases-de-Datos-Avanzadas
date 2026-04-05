package Coordinadores;

import DTOs.ClienteDTO;
import DTOs.IngredienteDTO;
import Enumeradores.UnidadMedida;
import java.util.List;

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
}
