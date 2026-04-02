package mappers;
import DTOs.ClienteDTO;
import DTOs.ClienteFrecuenteDTO;
import Entidades.Cliente;
import Entidades.ClienteFrecuente;

/**
 * Clase que mapea de DTO a entidad y viceversa a ClienteFrecuente
 * @author Andre
 */
public class ClienteMapper {
    
    /**
     * Mapea de DTO a entidad
     * Sirve más como un orquestador de otros métodos
     * Recibe un cliente, pregunta qué tipo son y delega el mapeo a otros métodos
     * Dice: "Dame un cliente y le paso la chamba a otro mapeador especializado"
     * Solo se añade un if y otro método para que crezca
     * Negocio ni se entera de estas movidas internas
     * 
     * @param dto a mapear
     * @return la entidad mapeada
     */
    public static Cliente mapearDTOEntidad(ClienteDTO dto) {

        //Protección si es nulo
        if (dto == null) {
            return null;
        }
        
        //Crea la instancia de dominio primero para no tener problemas de casteo
        Cliente entidad = null;
        
        /**
         * En este grupo de condiciones pregunta por la sublase específica
         * Si una coincide, castea el objeto a la subclase y lo regresa mapeado
         * Primero lo específico, después lo general
         */
        if (dto instanceof ClienteFrecuenteDTO) {
            entidad = mapearDTOEntidadFrecuente((ClienteFrecuenteDTO) dto);
        }
        
        //Si no coincidió ninguna clase, regresa null
        if (entidad == null) {
            return null;
        }
        
        //Mapea los atributos base y regresa la entidad
        mapearDTOEntidadBase(dto, entidad);
        return entidad;
    }
    
    
    
    /**
     * Mapea de DTO a entidad
     * Sirve más como un orquestador de otros métodos
     * Recibe un cliente, pregunta qué tipo son y delega el mapeo a otros métodos
     * Dice: "Dame un DTO y le paso la chamba a otro mapeador especializado"
     * Solo se añade un if y otro método para que crezca
     * Negocio ni se entera de estas movidas internas
     * 
     * @param entidad a mapear
     * @return el dto mapeado
     */
    public static ClienteDTO mapearEntidadDTO(Cliente entidad) {
        //Protección si es nulo
        if (entidad == null) {
            return null;
        }
        
        //Crea la instancia del DTO de una vez para no tener problemas de casteo
        ClienteDTO dto = null;
        
        /**
         * En este grupo de condiciones pregunta por la sublase específica
         * Si una coincide, castea el objeto a la subclase y lo regresa mapeado
         * Primero lo específico, después lo general
         */
        if (entidad instanceof ClienteFrecuente) {
            dto = mapearEntidadDTOFrecuente((ClienteFrecuente) entidad);
        }
        
        //Si no coincidió ninguna clase, regresa null
        if (dto == null) {
            return null;
        }
        
        //Mapea los datos base y regresa el DTO
        mapearEntidadDTOBase(entidad, dto);
        return dto;
    }
    
    
    
    /**
     * Mapea los atributos en específico de clienteFrecuente
     * 
     * @param dto
     * @return cliente mapeado
     */
    private static ClienteFrecuente mapearDTOEntidadFrecuente(ClienteFrecuenteDTO dto) {
        
        //Llama al método base y casta el resultado a un tipo específico
        ClienteFrecuente entidad = new ClienteFrecuente();

        /**
         * Aquí debería estar la lógica de mapeo adicional según la subclase
         * Como en este caso todos los atributos de frecuente son calculados, no hay nada
         * Se deja este método de todas formas por si en el futuro se añaden otros atributos
         * Y sirve de ejemplo para futuros mapeadores especializados
         */
        
        return entidad;
    }
    
    
    
    /**
     * Mepea los atributos de entidad a DTO de clientes frecuents
     * 
     * @param entidad
     * @return un DTO listo
     */
    private static ClienteFrecuenteDTO mapearEntidadDTOFrecuente(ClienteFrecuente entidad) {
        
        //Llama al método base y casta el resultado a un tipo específico
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        
        //Se agregan los atributos específicos de un cliente frecuente
        dto.setVisitas(entidad.getVisitas());
        dto.setGastoTotal(entidad.getGastoTotal());
        dto.setPuntosFidelidad(entidad.getPuntosFidelidad());
        
        return dto;
    }

    
    
    /**
     * Mapea todos los atributos base de cliente
     * No crea ni regresa nada, solo trabaja con dos objetos
     * Esto es para que desde el principio se le pasen entidades creadas por el mapper especialista
     * Centraliza esta lógica para no repetirla en cada mapeo especializado
     * 
     * @param dto del cliente a mapear
     * @param entidad del cliente a llenar
     */
    private static void mapearDTOEntidadBase(ClienteDTO dto, Cliente entidad) {
        entidad.setId(dto.getId());
        entidad.setNombres(dto.getNombres());
        entidad.setApellidoPaterno(dto.getApellidoPaterno());
        entidad.setApellidoMaterno(dto.getApellidoMaterno());
        entidad.setTelefono(dto.getTelefono());
        entidad.setFechaRegistro(dto.getFechaRegistro());
        
        //Correo opcional
        String correo = dto.getCorreo();
        if (correo != null) {
            entidad.setCorreo(correo);
        }
    }
    
    
    
    /**
     * Mapea todos los atributos base de cliente
     * No crea ni regresa nada, solo trabaja con dos objetos
     * Esto es para que desde el principio se le pasen entidades creadas por el mapper especialista
     * Centraliza esta lógica para no repetirla en cada mapeo especializado
     * 
     * @param entidad a mapear
     * @param dto del cliente a llenar
     */
    private static void mapearEntidadDTOBase(Cliente entidad, ClienteDTO dto) {
        dto.setId(entidad.getId());
        dto.setNombres(entidad.getNombres());
        dto.setApellidoPaterno(entidad.getApellidoPaterno());
        dto.setApellidoMaterno(entidad.getApellidoMaterno());
        dto.setTelefono(entidad.getTelefono());
        dto.setFechaRegistro(entidad.getFechaRegistro());
        
        //Correo opcional
        String correo = entidad.getCorreo();
        if (correo != null) {
            dto.setCorreo(entidad.getCorreo());
        }
    }
}