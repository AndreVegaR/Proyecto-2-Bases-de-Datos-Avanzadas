package BO;
import DAOs.ComandaDAO;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import Entidades.Comanda;
import excepciones.NegocioException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import mappers.ComandaMapper;
import utilerias.UtilNegocio;

/**
 * BO para las comandas
 * 
 * @author Andre
 */
public class ComandaBO {
    
    
    //Instancia
    private static ComandaBO instancia;
    
    //Constructor privado
    private ComandaBO() {}
    
    /**
     * Singleton
     * 
     * @return el BO listo
     */
    public static ComandaBO getInstance() {
        if (instancia == null) {
            instancia = new ComandaBO();
        }
        return instancia;
    }
    
    
    /**
     * Consulta una comanda por su ID
     * 
     * @param id de la comanda
     * @return la comanda DTO
     */
    public ComandaDTO consultarComanda(Long id) {
        UtilNegocio.esNumeroPositivo(id, "ID");
        
        //Verifica que exista
        Comanda entidad = ComandaDAO.getInstance().consultarComanda(id);
        if (entidad == null) {
            throw new NegocioException("No existe la comanda consultada");
        }
        return ComandaMapper.mapearEntidadDTO(entidad);
    }
    
    
    
    /**
     * Registra una comanda en el sistema
     * Se apoya de un conteo del DAO y de un método de MesaBO para ocupar mesas
     * 
     * @param comanda a registrar
     * @return esa comanda procesada en DTO
     */
    public ComandaDTO registrarComanda(ComandaDTO comanda) {
        
        //Excepciones
        UtilNegocio.esNulo(comanda);
        if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
            throw new NegocioException("No se puede registrar una comanda sin productos");
        }
        
        //Mapea la comanda
        Comanda comandaRegistrar = ComandaMapper.mapearDTOEntidad(comanda);
        
        /**
         * Fecha y hora de cuándo se manda a convertir en dominio para ser persistido
         * Se hace aquí porque es lógica de negocio
         * Se maneja en variable aparte: ¿y si por alguna razón esa regla cambia?
         */
        LocalDateTime fecha = LocalDateTime.now();
        comandaRegistrar.setFechaRegistro(fecha);
        
        /**
         * Obtiene el número de comandas en el día de hoy
         * A ese conteo le suma 1 para crear el folio
         * Funciona así porque es consecutivo, según el orden en el mismo día
         * Arroja un long, pero se castea a int
         */
        int conteo = (int) ComandaDAO.getInstance().contarComandasHoy() + 1;
        
        //Crea el folio y lo asigna
        String folio = UtilNegocio.crearFolio(conteo);
        comandaRegistrar.setFolio(folio);
        
        //La manda al DAO para persistir
        Comanda comandaRegistrada = ComandaDAO.getInstance().registrarComanda(comandaRegistrar);
        
        //Regresa la entidad casteada
        ComandaDTO comandaRegresar = ComandaMapper.mapearEntidadDTO(comandaRegistrada);
        
        //Asigna la mesa
        MesaBO.getInstance().ocuparMesa(comandaRegresar.getMesa());
        
        //Regresa la comanda
        return comandaRegresar;
    }
    
    
    
    /**
     * Actualiza una comanda en el sistema
     * 
     * @param comanda a actualizar
     * @return el DTO
     */
    public ComandaDTO actualizarComanda(ComandaDTO comanda) {
        
        //Excepciones
        UtilNegocio.esNulo(comanda);
        if (comanda.getDetalles() == null || comanda.getDetalles().isEmpty()) {
            throw new NegocioException("No se puede actualizar una comanda sin productos");
        }
        
        //Mapea la comanda
        Comanda comandaActualizar = ComandaMapper.mapearDTOEntidad(comanda);
        
        //La manda a actualizar
        Comanda comandaActualizada = ComandaDAO.getInstance().actualizarComanda(comandaActualizar);
        
        //Regresa en DTO
        ComandaDTO comandaRegresar = ComandaMapper.mapearEntidadDTO(comandaActualizada);
        
        /**
         * Si el estado no es "Abierta", significa que está cerrada o cancelada 
         * Entonces se manda a desocupar la mesa
         */
        if (!ComandaMapper.ESTADO_INICIAL.equals(comandaRegresar.getEstado())) {
            MesaBO.getInstance().desocuparMesa(comandaRegresar.getMesa());
        }

        //Regresa la comanda
        return comandaRegresar;
    }   
    
    
    
    
    /**
     * Obtiene del DAO una lista con todos las comandas del sistema
     * Una lista vacía no es excepción: es un estado natural posible
     * 
     * @return lista de comandas en DTO para presentación
     */
    public List<ComandaDTO> consultarComandas() {
        
        //Obtiene todos las comandas del DAO
        List<Comanda> entidades = ComandaDAO.getInstance().consultarComandas();
        
        //Mapea todas las comandas
        List<ComandaDTO> dtos = entidades.stream()
                                              .map(ComandaMapper::mapearEntidadDTO)
                                              .collect(Collectors.toList());
        
        //Regresa la nueva lista
        return dtos;
    }
    
    
    
    /**
     * Consulta todos los detalles asociados a una comanda
     * Solo regresa la lista de la comanda obtenida por consultarComanda
     * 
     * @param id de la comanda
     * @return la lista de sus detalles
     */
    public List<DetallesComandaDTO> consultarDetalles(Long id) {
        UtilNegocio.esNumeroPositivo(id, "ID");
        
        //Comprueba que exista esa comanda
        ComandaDTO comanda = consultarComanda(id);
        if (comanda == null) {
            throw new NegocioException("No existe esa comanda");
        }
        
        //Regresa la lista de sus detalles
        return comanda.getDetalles();
    }
}