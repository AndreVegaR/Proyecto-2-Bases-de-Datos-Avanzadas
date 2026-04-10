package BO;
import DAOs.ClienteDAO;
import DAOs.MesaDAO;
import DTOs.ClienteDTO;
import DTOs.MesaDTO;
import Entidades.Cliente;
import Entidades.Mesa;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ClienteMapper;
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
     * Obtiene todas las mesas y las empaqueta en DTO
     * 
     * @return las mesas en DTO
     */
    public List<MesaDTO> consultarMesas() {
        //Obtiene todos los clientes del DAO
        List<Mesa> mesas = MesaDAO.getInstance().consultarMesas();
        
        /**
         * Mapea a todos los clientes en una lista de tipo MesaDTO
         * Todos empaquetados listos para presentación
         */
        List<MesaDTO> mesasDTO = mesas.stream()
                                            .map(MesaMapper :: mapearEntidadDTO)
                                            .collect(Collectors.toList());
        
        //Regresa la nueva lista
        return mesasDTO;
    }
    
    
    
    /**
     * Le cambia el estado a una mesa a ocupada
     * Se ve innecesario, pero es un envoltorio necesario
     * Si la lógica crece o cambia, el que llamó este método ni se entera
     * Además de que maneja un mapeado y una llamada al DAO
     * 
     * @param mesa a ocupar
     */
    public void ocuparMesa(MesaDTO mesa) {
        mesa.setEstadoMesa(OCUPADA);
        MesaDAO.getInstance().actualizarEstado(mesa.getId(), mesa.getEstadoMesa());
    }
    
    
    
    /**
     * Mismo sentido que ocuparMesa
     * 
     * @param mesa 
     */
    public void desocuparMesa(MesaDTO mesa) {
        mesa.setEstadoMesa(DISPONIBLE);
        MesaDAO.getInstance().actualizarEstado(mesa.getId(), mesa.getEstadoMesa());
    }
}