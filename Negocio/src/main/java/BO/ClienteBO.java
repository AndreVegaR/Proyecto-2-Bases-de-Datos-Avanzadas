package BO;
import DAOs.ClienteDAO;
import DTOs.ClienteDTO;
import Entidades.Cliente;
import excepciones.PersistenciaException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ClienteMapper;
import utilerias.UtilNegocio;


/**
 * Logica de negocio para Cliente
 * Utiliza el singleton de ClienteDAO
 * Utiliza el principio de Liskov, así que acepta cualquier tipo de cliente
 * El mape a dominio-DTO y viceversa se hace mediante métodos mappers
 * Negocio solo sabe que trabaja con clientes, los mappers hacen el resto
 * @author Jazmin
 */
public class ClienteBO {
    
    //Instancia
    private static ClienteBO instancia;
    
    //Constructor privado
    private ClienteBO() {}
    
    /**
     * Singleton
     * 
     * @return el BO listo
     */
    public static ClienteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteBO();
        }
        return instancia;
    }
    
    /**
     * Recibe una ID y la utiliza para consultar un cliente ya registrado
     * Lo regresa empaquetado en DTO para presentación
     * 
     * @param id del cliente a eliminar
     * @return el cliente en DTO
     */
    public ClienteDTO consultarCliente(Long id) {
        UtilNegocio.esNumeroPositivo(id, "ID");
        
        //Obtiene el cliente del DAO
        Cliente cliente = ClienteDAO.getInstance().buscarPorId(id);
        
        //Excepción
        if (cliente == null) {
            throw new PersistenciaException("No se consultar al cliente");
        }
        
        //Regresa al cliente mapeado
        return ClienteMapper.mapearEntidadDTO(cliente);
    }
    
    
    
    /**
     * Recibe datos de presentación y empaquetarlos a persistencia
     * Se procesan mediant un mapper, del cual negocio desconoce su funcionamiento
     * Como el flujo es de presentación a persistencia, se convierte un DTO a entidad
     * 
     * @param clienteRegistrar de presentación
     * @return un cliente persistido
     */
    public ClienteDTO registrarCliente(ClienteDTO clienteRegistrar) {
        validarCliente(clienteRegistrar);
        
        //Mapea a entidad
        Cliente cliente = ClienteMapper.mapearDTOEntidad(clienteRegistrar);
        
        /**
         * Fecha y hora de cuándo se manda a convertir en dominio para ser persistido
         * Se hace aquí porque es lógica de negocio
         * Se maneja en variable aparte: ¿y si por alguna razón esa regla cambia?
         */
        LocalDateTime fecha = LocalDateTime.now();
        cliente.setFechaRegistro(fecha);
        
        //Lo manda a persistirse
        ClienteDAO.getInstance().registrarCliente(cliente);

        //Regresa el DTO
        return ClienteMapper.mapearEntidadDTO(cliente);
    }
    
    
    
    /**
     * Recibe un clienteDTO de presentación
     * Su ID ya debe existir dentro de la BD, pues solo actualiza, no registra
     * Lo regresa empaquetado en DTO para presentación
     * 
     * @param clienteNuevo a actualizar
     * @return el cliente actualizado exitosamente
     */
    public ClienteDTO actualizarCliente(ClienteDTO clienteNuevo) {
        validarCliente(clienteNuevo);
        Long id = clienteNuevo.getId();
        UtilNegocio.esNumeroPositivo(id, "ID");
        
        //Consulta primero si ya existe un cliente con esa ID
        Cliente clienteViejo = ClienteDAO.getInstance().buscarPorId(id);     
        if (clienteViejo == null) {
            throw new PersistenciaException("ID inválida");
        }
        
        //Mapea al cliente a una entidad
        Cliente clienteActualizado = ClienteMapper.mapearDTOEntidad(clienteNuevo);
        
        //Lo manda a actualizar con DAO
        Cliente resultado = ClienteDAO.getInstance().actualizarCliente(clienteActualizado);
        
        //Regresa ese actualizado mapeado como DTO
        return ClienteMapper.mapearEntidadDTO(resultado);
    }
    
    
    
    /**
     * Recibe una ID y la utiliza para consultar un cliente ya registrado
     * Si sí existe, procede a eliminarlo
     * Lo regresa empaquetado en DTO para presentación
     * 
     * @param id del cliente a eliminar
     * @return el cliente en DTO
     */
    public ClienteDTO eliminarCliente(Long id) {
        UtilNegocio.esNumeroPositivo(id, "ID");
        
        //Primero se asegura de que exista ese cliente
        Cliente clienteEliminado = ClienteDAO.getInstance().buscarPorId(id);
        
        //Excepción
        if (clienteEliminado == null) {
            throw new PersistenciaException("No se pudo eliminar al cliente");
        }
        
        //Lo elimina y lo regresa mapeado
        ClienteDAO.getInstance().eliminarCliente(clienteEliminado.getId());
        return ClienteMapper.mapearEntidadDTO(clienteEliminado);
    }
    
    
    
    /**
     * Obtiene del DAO una lista con todos los clientes del sistema
     * No discrimina por subclase
     * Una lista vacía no es excepción: es un estado natural posible
     * 
     * @return lista de clientes en DTO para presentación
     */
    public List<ClienteDTO> consultarClientes() {
        
        //Obtiene todos los clientes del DAO
        List<Cliente> clientes = ClienteDAO.getInstance().consultarClientes();
        
        /**
         * Mapea a todos los clientes en una lista de tipo ClienteDTO
         * Todos empaquetados listos para presentación
         * El mapper es inteligente, cada cliente será procesado como debe ser
         */
        List<ClienteDTO> clientesDTO = clientes.stream()
                                              .map(ClienteMapper :: mapearEntidadDTO)
                                              .collect(Collectors.toList());
        
        //Regresa la nueva lista
        return clientesDTO;
        
    }
    
    
    
     /**
     * Valida los campos base y específicos de un cliente
     * Solo es un envoltorio de validaciones generales, pero en caso de ocuparse puede crecer a ser más específico
     * Los métodos que usen este auxiliar no sabrán nada de los detalles internos, solo validan y punto
     * De otra forma: abstracción necesaria para facilitar escalabilidad
     * 
     * @param cliente a validar
     */
    private void validarCliente(Object objeto) {
        UtilNegocio.validarObjeto(objeto);
        
        //Casteo a clienteDTO
        ClienteDTO cliente = (ClienteDTO) objeto;
        
        //Valida el teléfono
        UtilNegocio.validarTelefono(cliente.getTelefono());
        
        //Valida el correo si lo tiene
        String correo = cliente.getCorreo();
        if (correo != null && !correo.isBlank()) {
            UtilNegocio.validarCorreo(correo);
        }
        
        //Aquí irían condiciones y validaciones específicas...
    }
}