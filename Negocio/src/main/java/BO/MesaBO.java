package BO;
import DAOs.MesaDAO;
import DTOs.MesaDTO;
import Entidades.Mesa;
import excepciones.NegocioException;
import mappers.MesaMapper;

/**
 * BO para las mesas
 * 
 * @author Andre
 */
public class MesaBO {
    
    /**
     * Registra una mesa en el sistema
     * 
     * @param mesaRegistrar
     * @return la mesa DTO
     */
    public MesaDTO registrarMesa(MesaDTO mesaRegistrar) {
        
        //Protección mínima si es null
        if (mesaRegistrar == null) {
            throw new NegocioException("MesaDTO null");
        }
        
        //Mapea la instancia y la manda a persistir
        Mesa mesa = MesaMapper.mapearDTOEntidad(mesaRegistrar);
        
        //Lo manda a persistirse
        Mesa mesaRegistrada = MesaDAO.getInstance().registrarMesa(mesa);
        
        //Regresa la mesa
        return MesaMapper.mapearEntidadDTO(mesaRegistrada);
    }
    
    
    
    /**
     * Actualiza tu mente
     * 
     * @param mesaActualizar
     * @return 
     */
    public MesaDTO actualizarMesa(MesaDTO mesaActualizar) {
        
        //Protección mínima si es null
        if (mesaActualizar == null) {
            throw new NegocioException("MesaDTO null");
        }
        
        //Mapea la instancia y la manda a persistir
        Mesa mesa = MesaMapper.mapearDTOEntidad(mesaActualizar);
        
        //Lo manda a actualizarse
        Mesa mesaActualizada = MesaDAO.getInstance().actualizarMesa(mesa);
        
        //Regresa la mesa
        return MesaMapper.mapearEntidadDTO(mesaActualizada);
    }
}