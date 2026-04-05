package BO;
import DAOs.MesaDAO;
import DTOs.MesaDTO;
import Entidades.Mesa;
import mappers.MesaMapper;
import utilerias.UtilNegocio;

/**
 * BO para las mesas
 * 
 * @author Andre
 */
public class MesaBO {
    private static MesaBO instancia;

    //Constructor privado
    private MesaBO() {}

    /**
     * Singleton
     * 
     * @return DAO ya listo
     */
    public static MesaBO getInstance() {
        if (instancia == null) {
            instancia = new MesaBO();
        }
        return instancia;
    }
    
    
    
    //Establece cómo se trabaja con el estado de mesa ocupada con un valor fijo
    public static final String OCUPADA = "Ocupada";
    public static final String DISPONIBLE = "Disponible";
    
    
    
    /**
     * Registra una mesa en el sistema
     * 
     * @param mesa a registrar
     * @return la mesa DTO
     */
    public MesaDTO registrarMesa(MesaDTO mesa) {
        UtilNegocio.esNulo(mesa);
        
        //Mapea la instancia y la manda a persistir
        Mesa mesaRegistrar = MesaMapper.mapearDTOEntidad(mesa);
        
        //Lo manda a persistirse
        Mesa mesaRegistrada = MesaDAO.getInstance().registrarMesa(mesaRegistrar);
        
        //Regresa la mesa
        return MesaMapper.mapearEntidadDTO(mesaRegistrada);
    }
    
    
    
    /**
     * Actualiza una mesa
     * 
     * @param mesa a actualizar
     * @return la mesa en DTO
     */
    public MesaDTO actualizarMesa(MesaDTO mesa) {
        UtilNegocio.esNulo(mesa);
        
        //Mapea la instancia y la manda a persistir
        Mesa mesaActualizar = MesaMapper.mapearDTOEntidad(mesa);
        
        //Lo manda a actualizarse
        Mesa mesaActualizada = MesaDAO.getInstance().actualizarMesa(mesaActualizar);
        
        //Regresa la mesa
        return MesaMapper.mapearEntidadDTO(mesaActualizada);
    }
    
    
    
    /**
     * Le cambia el estado a una mesa a ocupada
     * Se ve innecesario, pero es un envoltorio necesario
     * Si la lógica crece o cambia, el que llamó este método ni se entera
     * Además de que maneja un mapeado y una llamada al DAO
     * 
     * @param mesa a ocupar
     */
    public static void ocuparMesa(MesaDTO mesa) {
        mesa.setEstadoMesa(OCUPADA);
        MesaDAO.getInstance().actualizarEstado(mesa.getId(), mesa.getEstadoMesa());
    }
    
    
    
    /**
     * Mismo sentido que ocuparMesa
     * 
     * @param mesa 
     */
    public static void desocuparMesa(MesaDTO mesa) {
        mesa.setEstadoMesa(DISPONIBLE);
        MesaDAO.getInstance().actualizarEstado(mesa.getId(), mesa.getEstadoMesa());
    }
}