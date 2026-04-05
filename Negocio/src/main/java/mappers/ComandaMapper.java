package mappers;
import DTOs.ClienteDTO;
import Entidades.Comanda;
import excepciones.NegocioException;
import DTOs.ComandaDTO;
import DTOs.DetallesComandaDTO;
import DTOs.MesaDTO;
import DTOs.ProductoDTO;
import Entidades.Cliente;
import Entidades.DetallesComanda;
import Entidades.Mesa;
import Entidades.Producto;
import java.util.List;
import java.util.stream.Collectors;

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
    public static String ESTADO_INICIAL = "Abierta";
    
    public static String CERRADA = "Cerrada";
    
    public static String CANCELADA = "Cancelada";
    
    
    
    /**
     * Pasa un DTO a una entidad
     * Usa un método adicional para mapear la mesa
     * 
     * @param dto a mapear
     * @return la entidad mapeada
     */
    public static Comanda mapearDTOEntidad(ComandaDTO dto) {
        
        //Validaciones
        if (dto == null) {
            throw new NegocioException("ComandaDTO null");
        }
        
        //Crea la entidad y mapea lo básico
        Comanda entidad = new Comanda();
        entidad.setId(dto.getId());
        entidad.setTotal(dto.getTotal());
        
        //Solo le imprime el estado inicial si no lo tiene anteriormente, o sea se está registrando
        if (dto.getEstado() == null || dto.getEstado().isBlank()) {
            entidad.setEstado(ESTADO_INICIAL);
        } else {
            entidad.setEstado(dto.getEstado());
        }
        
        //Solo mapea el folio si ya existe
        if (dto.getFolio() != null && !dto.getFolio().isBlank()) {
            entidad.setFolio(dto.getFolio());
        }
        
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
    public static ComandaDTO mapearEntidadDTO(Comanda entidad) {
        
        //Validaciones
        if (entidad == null) {
            throw new NegocioException("Comanda null");
        }
        
        //Crea la entidad y mapea lo básico
        ComandaDTO dto = new ComandaDTO();
        dto.setId(entidad.getId());
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
                                            entidad.setProducto(ProductoFalso_DTOEntidad(dto.getProducto())); //LUEGO SE REEMPLAZA POR PRODUCTO MAPPER!!!
                                            return entidad;
                                        })
                                        .toList();
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
                                            dto.setProducto(ProductoFalso_EntidadDTO(entidad.getProducto())); //LUEGO SE REEMPLAZA POR PRODUCTO MAPPER!!!
                                            return dto;
                                        })
                                        .toList();
        return dtos;
    }
    
    
    
    
    //LUEGO SE REEMPLAZA POR PRODUCTO MAPPER!!!
    private static Producto ProductoFalso_DTOEntidad(ProductoDTO dto) {
        Producto entidad = new Producto();
        entidad.setId(dto.getId());
        entidad.setNombre(dto.getNombre());
        entidad.setPrecio(100);
        return entidad;
    }
    private static ProductoDTO ProductoFalso_EntidadDTO(Producto entidad) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setPrecio(100);
        return dto;
    }
}