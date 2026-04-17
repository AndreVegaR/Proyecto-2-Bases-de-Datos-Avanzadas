package DTOs;
import java.util.List;
/**
 *
 * @author Angel
 * DTO DE PRODUCTO
 */
public class ProductoDTO {
    
    private Long id;
    private String nombre;
    private TipoProducto tipoProducto;
    private EstadoProducto estadoProducto;
    private double precio;
    private byte[] imagen;
    private String descripcion;
    
    // Lista de ingredientes con cantidad
    private List<IngredienteProductoDTO> ingredientes;

    // Constructor vacío
    public ProductoDTO() {}

    // Constructor completo
    public ProductoDTO(Long id, String nombre, TipoProducto tipoProducto, EstadoProducto estadoProducto, List<IngredienteProductoDTO> ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.estadoProducto = estadoProducto;
        this.ingredientes = ingredientes;
    }

    
    public ProductoDTO(Long id, String nombre, TipoProducto tipoProducto, EstadoProducto estadoProducto, double precio, byte[] imagen, String descripcion, List<IngredienteProductoDTO> ingredientes) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.estadoProducto = estadoProducto;
        this.precio = precio;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.ingredientes = ingredientes;
    }
    
    

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoProducto getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public EstadoProducto getEstadoProducto() {
        return estadoProducto;
    }

    public void setEstadoProducto(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public List<IngredienteProductoDTO> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteProductoDTO> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    /*
    Este es un enum para los estados del producto ya sea activo e inactivo
    EnumType.String guarda el nombre del string
    Por ejemplo Producto p = new Producto();
    p.setTipo(TipoProducto.BEBIDA);
    */
    public enum TipoProducto {
        ENTRADA,
        PLATILLO,
        BEBIDA,
        POSTRE
    }
    
    //Enum de estados del producto
    public enum EstadoProducto {
    ACTIVO,
    INACTIVO
}

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "ProductoDTO{" + "id=" + id + ", nombre=" + nombre + ", tipoProducto=" + tipoProducto + ", estadoProducto=" + estadoProducto + ", precio=" + precio + ", imagen=" + imagen + ", ingredientes=" + ingredientes + '}';
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}