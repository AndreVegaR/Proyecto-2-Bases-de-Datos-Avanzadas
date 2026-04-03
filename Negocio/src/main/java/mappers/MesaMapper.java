package mappers;
import DTOs.MesaDTO;
import Entidades.Mesa;

/**
 * Mapea una mesa de DTO a entidad y viceversa
 * Podría parecer excesivo, pero en realidad la mesa se necesita en dos archivos:
 * -MesaBO, para registrarlas directo a la BD
 * -ComandaMapper, como parte del mapeo de todos sus atributos
 * Se incluye el ID para evitar que JPA se confunda y persista mesas fantasma
 * 
 * @author Andre
 */
public class MesaMapper {
    
    /**
     * String estático que guarda el estado base de una mesa al ser creada
     * Como este es el único lugar donde se crea, no puede haber error de dedo
     * Reemplaza un enumerador que debe viajar entre DTO, Dominio, Negocio...
     */
    private static String ESTADO_INICIAL = "Disponible"; 
    
    /**
     * Mapea de DTO a entidad
     * Como es de presentación a persistencia, significa que se está creando
     * Entonces aquí se le asigna el estado
     * 
     * @param dto a mapear
     * @return la entidad mapeada
     */
    public static Mesa mapearDTOEntidad(MesaDTO dto) {
        Mesa entidad = new Mesa();
        entidad.setNumero(dto.getNumero());
        entidad.setId(dto.getId());
        
        /**
         * Si el estado no está vacío, significa que se está mapeando dentro del flujo de comanda
         * En ese caso, simplemente se pasa el atributo
         * De lo contrario, apenas se está creando, y se le debe asignar el estado base
         */
        if (dto.getEstadoMesa() != null && !dto.getEstadoMesa().isEmpty()) {
            entidad.setEstadoMesa(dto.getEstadoMesa());
        } else {
            entidad.setEstadoMesa(ESTADO_INICIAL);
        }
        return entidad;
    }
    
    /**
     * Mapea de entidad a DTO
     * 
     * @param entidad
     * @return 
     */
    public static MesaDTO mapearEntidadDTO(Mesa entidad) {
        MesaDTO dto = new MesaDTO();
        dto.setId(entidad.getId());
        dto.setNumero(entidad.getNumero());
        dto.setEstadoMesa(entidad.getEstadoMesa());
        return dto; 
    }
}