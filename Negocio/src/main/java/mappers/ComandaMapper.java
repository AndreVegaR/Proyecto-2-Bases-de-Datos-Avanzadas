package mappers;
import DTOs.ClienteDTO;
import Entidades.Comanda;
import excepciones.NegocioException;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.MesaDTO;
import Entidades.Cliente;
import Entidades.DetallesComanda;
import Entidades.Mesa;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import utilerias.UtilNegocio;

/**
 * Mapper que transforma una comanda de DTO a entidad y viceversa
 * 
 * @author Andre
 */
public class ComandaMapper {
    
    /**
     * String estático que guarda el estado base de una comanda al ser creada
     * Como este es el único lugar donde se crea, no puede haber error de dedo
     * Reemplaza un enumerador que debe viajar entre DTO, Dominio, Negocio...
     */
    private static String ESTADO_INICIAL = "Abierta";
    
    
    
    /**
     * Pasa un DTO a una entidad
     * Usa un método adicional para mapear la mesa
     * 
     * @param dto a mapear
     * @param numConsecutivo del DAO para crear el folio
     * @return la entidad mapeada
     */
    public static Comanda mapearDTOEntidad(ComandaDTO dto, int numConsecutivo) {
        
        //Validaciones
        if (dto == null) {
            throw new NegocioException("ComandaDTO null");
        }
        UtilNegocio.esNumeroPositivo(numConsecutivo, "Número consecutivo");
        
        //Crea la entidad y mapea lo básico
        Comanda entidad = new Comanda();
        entidad.setTotal(dto.getTotal());
        entidad.setEstado(ESTADO_INICIAL);
        
        //Crea y le asigna el folio
        String folio = crearFolio(numConsecutivo);
        entidad.setFolio(folio);
        
        //Mapea el cliente
        Cliente clienteMapeado = ClienteMapper.mapearDTOEntidad(dto.getCliente());
        entidad.setCliente(clienteMapeado);
        
        //Mapea la mesa
        Mesa mesaMapeada = MesaMapper.mapearDTOEntidad(dto.getMesa());
        entidad.setMesa(mesaMapeada);
        
        //Mapea los detalles
        List<DetallesComanda> detallesMapeados = mapearDTOEntidadDetalles(dto.getDetalles(), entidad);
        entidad.setDetalles(detallesMapeados);
        
        //Regresa la entidad
        return entidad;
    }
    
    
    
    /**
     * Pasa una entidad a DTO
     * Usa un método adicional para mapear la mesa
     * 
     * @param entidad a mapear
     * @return la entidad mapeada
     */
    public static ComandaDTO mapearDTOEntidad(Comanda entidad) {
        
        //Validaciones
        if (entidad == null) {
            throw new NegocioException("Comanda null");
        }
        
        //Crea la entidad y mapea lo básico
        ComandaDTO dto = new ComandaDTO();
        dto.setTotal(entidad.getTotal());
        dto.setEstado(entidad.getEstado());
        dto.setFolio(entidad.getFolio());
        
        //Mapea el cliente
        ClienteDTO clienteMapeado = ClienteMapper.mapearEntidadDTO(entidad.getCliente());
        dto.setCliente(clienteMapeado);
        
        //Mapea la mesa
        MesaDTO mesaMapeada = MesaMapper.mapearEntidadDTO(entidad.getMesa());
        dto.setMesa(mesaMapeada);
        
        //Mapea los detalles
        List<DetallesComandaDTO> detallesMapeados = mapearEntidadDTODetalles(entidad.getDetalles());
        dto.setDetalles(detallesMapeados);
        
        //Regresa el DTO
        return dto;
    }
    
    
    
    /**
     * Mediante un lambda mapea todos los detallesComanda
     * De DTO a entidad
     * 
     * @param dtos de los detalles
     * @return los detalles en entidad
     */
    private static List<DetallesComanda> mapearDTOEntidadDetalles(List<DetallesComandaDTO> dtos, Comanda comanda) {
        List<DetallesComanda> entidades = dtos.stream().map(dto -> {
                                            DetallesComanda entidad = new DetallesComanda();
                                            entidad.setCantidad(dto.getCantidad());
                                            entidad.setSubtotal(dto.getSubtotal());
                                            entidad.setPrecioVenta(dto.getPrecioVenta());
                                            entidad.setComentarios(dto.getComentarios());
                                            entidad.setComanda(comanda);
                                            //entidad.setProducto(ProductoMapper.mapearDTOEntidad(dto.getProducto()));
                                            return entidad;
                                        })
                                        .collect(Collectors.toList());
        return entidades;
    }
    
    
    
    /**
     * Mediante un lambda mapea todos los detallesComanda
     * De entidad a DTO
     * 
     * @param entidades de los detalles
     * @return los detalles en dto
     */
    private static List<DetallesComandaDTO> mapearEntidadDTODetalles(List<DetallesComanda> entidades) {
        List<DetallesComandaDTO> dtos = entidades.stream().map(entidad -> {
                                            DetallesComandaDTO dto = new DetallesComandaDTO();
                                            dto.setCantidad(entidad.getCantidad());
                                            dto.setSubtotal(entidad.getSubtotal());
                                            dto.setPrecioVenta(entidad.getPrecioVenta());
                                            dto.setComentarios(entidad.getComentarios());
                                            //dto.setProducto(ProductoMapper.mapearEntidadDTO(entidad.getProducto()));
                                            return dto;
                                        })
                                        .collect(Collectors.toList());
        return dtos;
    }
    
    
    
    /**
     * Crea un folio único para una comanda
     * Utiliza un prefijo establecido, la fecha y el número de la comanda de ese día
     * Este número se lo pasa el BO, al que llama a un método del DAO
     * 
     * @param consecutivo
     * @return el folio armado
     */
    private static String crearFolio(int numConsecutivo) {
        
        //Prefijo asignado para el folio
        String PREFIJO = "OB";
        
        //Obtiene el año, mes y día en String
        String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        //Rella con ceros el número de 3 dígitos (ejemplo: 19 -> 019)
        String clave = String.format("%03d", numConsecutivo);
        
        //Arma el folio y lo regresa
        return PREFIJO + "-" + fecha + "-" + clave;       
    }
}