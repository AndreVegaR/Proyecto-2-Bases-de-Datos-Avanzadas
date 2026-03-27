package mappers;
import DTOs.ClienteFrecuenteDTO;
import Entidades.ClienteFrecuente;

/**
 * Clase que mapea de DTO a entidad y viceversa a ClienteFrecuente
 * @author Andre
 */
public class ClienteMapper {
   
    
    /**
     * Mapea de DTO a entidad
     * Al crearse no tiene ni visitas, ni gasto total ni puntos de fidelidad, así que no se consideran
     * 
     * @param dto a mapear
     * @return la entidad mapeada
     */
    public static ClienteFrecuente mapearDTOEntidad(ClienteFrecuenteDTO dto) {
        ClienteFrecuente entidad = new ClienteFrecuente();
        
        //Atributos comunes
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
       
        return entidad;
    }
    
    
    
    /**
     * Mapea de entidad a DTO
     * Obtiene los datos calculados de la entidad para mostrarlos actualizados
     * 
     * @param entidad a mapear
     * @return el dto mapeado
     */
    public static ClienteFrecuenteDTO mapearEntidadDTO(ClienteFrecuente entidad) {
        ClienteFrecuenteDTO dto = new ClienteFrecuenteDTO();
        
        //Atributos comunes
        dto.setId(entidad.getId());
        dto.setNombres(entidad.getNombres());
        dto.setApellidoPaterno(entidad.getApellidoPaterno());
        dto.setApellidoMaterno(entidad.getApellidoMaterno());
        dto.setTelefono(entidad.getTelefono());
        dto.setFechaRegistro(entidad.getFechaRegistro());

        //Correo opcional
        String correo = entidad.getCorreo();
        if (correo != null) {
            dto.setCorreo(correo);
        }
        
        //Datos específicos
        dto.setVisitas(entidad.getVisitas());
        dto.setGastoTotal(entidad.getGastoTotal());
        dto.setPuntosFidelidad(entidad.getPuntosFidelidad());
        
        return dto;
    }
}