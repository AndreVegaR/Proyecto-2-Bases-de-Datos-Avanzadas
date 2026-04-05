/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mappers;
import DTOs.IngredienteDTO;
import Entidades.Ingrediente;

/**
 * Clase que mapea de DTO a entidad y viceversa para Ingrediente
 * @author Jazmin
 */
public class IngredienteMapper {
    
    /**
     * Mapea de DTO a entidad
     * @param dto a mapear
     * @return entidad mapeada
     */
    public static Ingrediente mapearDTOEntidad(IngredienteDTO dto){
        if(dto == null){
            return null;
        }
        Ingrediente entidad = new Ingrediente();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setUnidadMedida(dto.getUnidadMedida());
        entidad.setStock(dto.getStock());
        entidad.setImagen(dto.getImagen());
        return entidad;
    }
     /**
     * Mapea de entidad a DTO
     * @param entidad a mapear
     * @return DTO mapeado
     */
     public static IngredienteDTO mapearEntidadDTO(Ingrediente entidad){
         if(entidad == null){
             return null;
         }
         IngredienteDTO dto = new IngredienteDTO();
         dto.setId(entidad.getId());
         dto.setNombre(entidad.getNombre());
         dto.setUnidadMedida(entidad.getUnidadMedida());
         dto.setStock(entidad.getStock());
         dto.setImagen(entidad.getImagen());
         return dto;
     }
    
}
