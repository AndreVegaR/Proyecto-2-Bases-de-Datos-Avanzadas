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
    public ClienteFrecuenteDTO modificarCliente(ClienteFrecuenteDTO dto){
        //Validaciones
        Utilerias.esNulo(dto);
        Utilerias.esNulo(dto.getId());
        Utilerias.esCadenadaVacia(dto.getNombres(), "Nombres");
        Utilerias.esCadenadaVacia(dto.getApellidoPaterno(), "Apellido Paterno");
        Utilerias.esCadenadaVacia(dto.getApellidoMaterno(), "Apellido Materno");
        Utilerias.esCadenadaVacia(dto.getTelefono(),"Teléfono");
        if(dto.getFechaRegistro() == null){
            dto.setFechaRegistro(LocalDateTime.now());
        }
        
        //Mapea
        ClienteFrecuente cliente = ClienteMapper.mapearDTOEntidad(dto);
        
        //Encriptar antes de actualizar
        String telefono = cliente.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefono);
        cliente.setTelefono(telefonoEncriptado);
        
        //Modifica
        cliente = ClienteFrecuenteDAO.getInstance().modificarCliente(cliente);
        
        //Mapear
        ClienteFrecuenteDTO resultado = ClienteMapper.mapearEntidadDTO(cliente);
        
        //desencriptar para volverlo legible 
        String telefonoDesencriptado = EncriptarTelefono.desencriptar(resultado.getTelefono());
        resultado.setTelefono(telefonoDesencriptado);
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
            dto.setTelefono(telEncriptado);
        });
        
        return listaDtos;
    }
}