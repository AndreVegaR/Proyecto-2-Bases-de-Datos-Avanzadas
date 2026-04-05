/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DTOs.IngredienteDTO;
import Entidades.Ingrediente;
import Enumeradores.UnidadMedida;
import java.util.List;
import java.util.stream.Collectors;
import mappers.IngredienteMapper;
import utilerias.UtilNegocio;
import DAOs.IngredienteDAO;
import excepciones.NegocioException;


/**
 * Lógica de negocio para Ingrediente
 * Utiliza el singleton de IngredienteDAO
 * El mapeo DTO-Entidad se delega al IngredienteMapper
 * @author Jazmin
 */
public class IngredienteBO {
    
    private static IngredienteBO instancia;

    public IngredienteBO() {
    }
    
    public static IngredienteBO getInstance(){
        if(instancia == null){
            instancia = new IngredienteBO();
        }
        return instancia;
    }
     /**
     * Registra un ingrediente nuevo
     * Valida que no exista otro con el mismo nombre y unidad de medida
     * @param dto datos del ingrediente
     * @return DTO del ingrediente registrado
     */
    public IngredienteDTO registrarIngrediente(IngredienteDTO dto){
        //validaciones 
        UtilNegocio.esNulo(dto);
        UtilNegocio.esCadenadaVacia(dto.getNombre(), "Nombre");
        UtilNegocio.validarNombre(dto.getNombre());
        UtilNegocio.esNulo(dto.getUnidadMedida());
        UtilNegocio.esNumeroPositivo(dto.getStock(), "Stock");
        
        //Verificar duplicado
        if(IngredienteDAO.getInstance().existeIngrediente(dto.getNombre(),dto.getUnidadMedida())){
            throw new NegocioException("Ya existe un ingrediente con ese nombre y unidad de medida");
        }
        //mapeo dto a entidad
        Ingrediente entidad = IngredienteMapper.mapearDTOEntidad(dto);
        //persistir
        entidad = IngredienteDAO.getInstance().guardarIngrediente(entidad);
        //mapeo entidad a dto
        return IngredienteMapper.mapearEntidadDTO(entidad);
    }
     /**
      * Actualiza el stock de un Ingrediente existente
      * @param id del ingrediente
      * @param stock nuevo valor
      * @return DTO del ingrediente actualizado
      */
    public IngredienteDTO actualizarStock(Long id,Double stock){
        //validaciones
        UtilNegocio.esNumeroPositivo(id, "ID");
        UtilNegocio.esNumeroPositivo(stock, "Stock");
        
        //actualizar en BD
        Ingrediente entidad = IngredienteDAO.getInstance().actualizarStock(id, stock);
        
        //mapeo entidad a dto
        return IngredienteMapper.mapearEntidadDTO(entidad);
    }
   /**
    * Elimina un ingrediente por su ID
    * @param id del ingrediente
    * @return DTO del ingrediente eliminado
    */
    public IngredienteDTO eliminarIngrediente(Long id){
        UtilNegocio.esNumeroPositivo(id, "ID");
        Ingrediente eliminado = IngredienteDAO.getInstance().eliminarIngrediente(id);
        return IngredienteMapper.mapearEntidadDTO(eliminado);
    }
    /**
     * Busca ingrediente por su nombre parcial o unidad de medidad
     * si ambos son null regresa todos los ingredientes
     * @param nombre parcial del ingrediente, puede ser null
     * @param unidadMedida a filtar , puede ser null
     * @return lista de IngredienteDTO
     */
    public List<IngredienteDTO> buscarIngredientes(String nombre, UnidadMedida unidadMedida){
        List<Ingrediente> entidades= IngredienteDAO.getInstance().buscarIngredientes(nombre, unidadMedida);
        return entidades.stream().map(IngredienteMapper::mapearEntidadDTO).collect(Collectors.toList());
    }
      /**
       * Busca un ingrediente por su ID
       * @param id del ingrediente
       * @return DTO del ingrediente encontrado
       */
     public IngredienteDTO buscarPorId(Long id){
          UtilNegocio.esNumeroPositivo(id, "ID");
          Ingrediente entidad = IngredienteDAO.getInstance().buscarPorId(id);
          return IngredienteMapper.mapearEntidadDTO(entidad);
     }
     
    
}
