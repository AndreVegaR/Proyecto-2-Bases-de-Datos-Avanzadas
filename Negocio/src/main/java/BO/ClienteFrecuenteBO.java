package BO;
import DAOs.ClienteFrecuenteDAO;
import DTOs.ClienteFrecuenteDTO;
import Entidades.Cliente;
import Entidades.ClienteFrecuente;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ClienteMapper;
import utilerias.EncriptarTelefono;
import utilerias.Utilerias;


/**
 * Logica de negocio para ClienteFrecuentes
 * Utiliza el singleton de ClienteFrecuenteDAO
 * @author Jazmin
 */
public class ClienteFrecuenteBO {
    
    //Instancia
    private static ClienteFrecuenteBO instancia;
    
    //Constructor privado
    private ClienteFrecuenteBO() {}
    
    /**
     * Singleton
     * 
     * @return el BO listo
     */
    public static ClienteFrecuenteBO getInstance() {
        if (instancia == null) {
            instancia = new ClienteFrecuenteBO();
        }
        return instancia;
    }
    
    
    
    /**
     * Guarda un cliente frecuente despues de validarse
     * 
     * @param clienteDTO datos del cliente capturado desde la capa de presentación
     * @return ClienteFrecuenteDTO con los datos guardados
     */
    public ClienteFrecuenteDTO guardarCliente(ClienteFrecuenteDTO clienteDTO){
        //Validaciones
        Utilerias.esNulo(clienteDTO);
        Utilerias.esCadenadaVacia(clienteDTO.getNombres(), "Nombres");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoPaterno(), "Apellido Paterno");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoMaterno(), "Apellido Materno");
        Utilerias.esCadenadaVacia(clienteDTO.getTelefono(),"Teléfono");
        //Utilerias.validarTelefono(clienteDTO.getTelefono());
        
        //Crea la fecha del registro al persistirse
        if(clienteDTO.getFechaRegistro() == null){
            clienteDTO.setFechaRegistro(LocalDateTime.now());
        }
        
        //Mapeo a entidad
        ClienteFrecuente cliente = ClienteMapper.mapearDTOEntidad(clienteDTO);
        
        //Encriptar antes de guardar
        String telefono = cliente.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefono);
        cliente.setTelefono(telefonoEncriptado);
        cliente = ClienteFrecuenteDAO.getInstance().guardarCliente(cliente);
        
        //Mapeo a DTO
        ClienteFrecuenteDTO resultado = ClienteMapper.mapearEntidadDTO(cliente);
        
        //Desencriptar para volverlo legible 
        String telefonoDesencriptado = EncriptarTelefono.desencriptar(resultado.getTelefono());
        resultado.setTelefono(telefonoDesencriptado);
        return resultado;
    }
    
    
    
    /**
     * Elimina un cliente frecuente por ID y lo regresa
     * 
     * @param id identificador unico del cliente a eliminar
     * @return el cliente eliminado
     */
    public ClienteFrecuenteDTO eliminarCliente(Long id){
        Utilerias.esNumeroPositivo(id, "ID");
        ClienteFrecuente eliminado = ClienteFrecuenteDAO.getInstance().eliminarCliente(id);
        return ClienteMapper.mapearEntidadDTO(eliminado);
    }
    
    
    
    /**
     * Modifica un cliente frecuente
     * 
     * @param dto datos modificados del cliente frecuente
     * @return ClienteFrecuenteDTO con la informacion actualizada
     */
    public ClienteFrecuenteDTO modificarCliente(ClienteFrecuenteDTO dto) {

        Utilerias.esNulo(dto);
        if (dto.getId() == null) throw new IllegalArgumentException("El ID es obligatorio para actualizar");

        // 2. BUSCAR EL REGISTRO ORIGINAL (Para no perder datos como puntos o fecha)
        ClienteFrecuenteDAO dao = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuente clienteExistente = ClienteFrecuenteDAO.getInstance().buscarPorId(dto.getId()); 

        if (clienteExistente == null) throw new RuntimeException("Cliente no encontrado");

        clienteExistente.setNombres(dto.getNombres());
        clienteExistente.setApellidoPaterno(dto.getApellidoPaterno());
        clienteExistente.setApellidoMaterno(dto.getApellidoMaterno());
        clienteExistente.setCorreo(dto.getCorreo());

        String telefonoLimpio = dto.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefonoLimpio);
        clienteExistente.setTelefono(telefonoEncriptado);

        ClienteFrecuente clienteActualizado = dao.modificarCliente(clienteExistente);

        ClienteFrecuenteDTO resultado = ClienteMapper.mapearEntidadDTO(clienteActualizado);

        resultado.setTelefono(EncriptarTelefono.desencriptar(resultado.getTelefono()));

        return resultado;
    }

    
    
    /**
     * Obtiene la lista de todos los clientes frecuentes registrados
     * 
     * @return lista de ClienteFrecuente con los datos actualizados
     */
    public List<ClienteFrecuenteDTO> verClientes(){
        List<ClienteFrecuente> lista = ClienteFrecuenteDAO.getInstance().verClientes();
        
        List<ClienteFrecuenteDTO> listaDtos = lista.stream()
                                              .map(ClienteMapper :: mapearEntidadDTO)
                                              .collect(Collectors.toList());
        listaDtos.forEach(dto -> {
            String telEncriptado = dto.getTelefono();
            String telDesencriptado = EncriptarTelefono.desencriptar(telEncriptado);
            dto.setTelefono(telDesencriptado);
        });
        
        return listaDtos;
    }
    
    
    
    public ClienteFrecuenteDTO consultarCliente(Long id) {
        ClienteFrecuente cliente = ClienteFrecuenteDAO.getInstance().buscarPorId(id);
        return ClienteMapper.mapearEntidadDTO(cliente);
    }
}