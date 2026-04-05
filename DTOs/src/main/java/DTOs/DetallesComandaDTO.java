package DTOs;

/**
 * DTO de los detalles de una comanda
 * Tiene un ProductoDTO para evitar atributos innecesarios, además puede escalar la información
 * No necesita conocer a la comanda
 * 
 * @author Andre
 */
public class DetallesComandaDTO {
    
    //Atributos
    private Long id;
    private ProductoDTO producto;
    private int cantidad;
    private double precioVenta;
    private double subtotal;
    private String comentarios;
    
    //Constructor vacío
    public DetallesComandaDTO(){}
    
    /**
     * Constructor completo
     * @param id
     * @param producto
     * @param cantidad
     * @param precioVenta
     * @param subtotal
     * @param comentarios 
     */
    public DetallesComandaDTO(Long id, ProductoDTO producto, int cantidad, double precioVenta, double subtotal, String comentarios) {
        this.id = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.subtotal = subtotal;
        this.comentarios = comentarios;
    }

    /**
     * Constructor sin id
     * @param producto
     * @param cantidad
     * @param precioVenta
     * @param subtotal
     * @param comentarios 
     */
    public DetallesComandaDTO(ProductoDTO producto, int cantidad, double precioVenta, double subtotal, String comentarios) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.subtotal = subtotal;
        this.comentarios = comentarios;
    }
    
    //Getters y setters
    public ProductoDTO getProducto() {
        return producto;
    }

    public void setProducto(ProductoDTO producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    //El subtotal es un calculo entre el precio del producto y la cantidad
    public double getSubtotal() {
        if (this.subtotal > 0.0) {
            return this.subtotal;
        }
        return precioVenta * cantidad;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    } 

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioMomento) {
        this.precioVenta = precioMomento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}