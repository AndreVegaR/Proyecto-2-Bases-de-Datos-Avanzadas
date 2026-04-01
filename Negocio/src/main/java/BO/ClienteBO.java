package BO;
import DAOs.ClienteDAO;
import DTOs.ClienteDTO;
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
     * Se encarga de recibir datos de presentación y empaquetarlos a persistencia
     * Se procesan mediant un mapper, del cual negocio desconoce su funcionamiento
     * Como el flujo es de presentación a persistencia, se convierte un DTO a entidad
     * 
     * @param dto de presentación
     * @return un cliente persistido
     */
    public ClienteDTO registrarCliente(ClienteDTO dto) {
        //(Validaciones necesarias)
        
        //Fecha y hora de cuándo se manda a convertir en dominio para ser persistido
        //Se hace aquí porque es lógica de negocio
        dto.setFechaRegistro(LocalDateTime.now());
        
        //Se mapea de DTO a entidad
        Cliente cliente = ClienteMapper.mapearDTOEntidad(dto);
        
        //(Encriptación)
        
        //Se man1da a persistir al DAO
        ClienteDAO.getInstance().registrarCliente(cliente);
        
        //(Desencriptación)
        
        //Se regresa el DTO
        return ClienteMapper.mapearEntidadDTO(cliente);
    }
    
    
    
    /**
     * Guarda un cliente despues de validarse
     * 
     * @param cliente datos del cliente capturado desde la capa de presentación
     * @return Cliente con los datos guardados
     */
    public Cliente agregarCliente(ClienteDTO clienteDTO){
        //Validaciones
        Utilerias.esNulo(clienteDTO);
        Utilerias.esCadenadaVacia(clienteDTO.getNombres(), "Nombres");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoPaterno(), "Apellido Paterno");
        Utilerias.esCadenadaVacia(clienteDTO.getApellidoMaterno(), "Apellido Materno");
        Utilerias.esCadenadaVacia(clienteDTO.getTelefono(),"Teléfono");
        //Utilerias.validarTelefono(clienteDTO.getTelefono());
        

        clienteDTO.setFechaRegistro(LocalDateTime.now());
        
        //Mapeo a entidad
        Cliente cliente = ClienteMapper.mapearDTOEntidad(clienteDTO);
        
        //Encriptar antes de guardar
        String telefono = cliente.getTelefono();
        String telefonoEncriptado = EncriptarTelefono.encriptar(telefono);
        cliente.setTelefono(telefonoEncriptado);
        cliente = (ClienteFrecuente) ClienteDAO.getInstance().guardarCliente(cliente);
        
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
        ClienteFrecuente eliminado = ClienteDAO.getInstance().eliminarCliente(id);
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
        ClienteDAO dao = ClienteDAO.getInstance();
        ClienteFrecuente clienteExistente = ClienteDAO.getInstance().buscarPorId(dto.getId()); 

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
        List<ClienteFrecuente> lista = ClienteDAO.getInstance().verClientes();
        
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
        ClienteFrecuente cliente = ClienteDAO.getInstance().buscarPorId(id);
        return ClienteMapper.mapearEntidadDTO(cliente);
    }
}