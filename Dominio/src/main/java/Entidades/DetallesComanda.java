package Entidades;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Representa los detalles específicos de un producto registrado en una comanda
 * Guarda el producto, los comentarios, la cantidad y el subtotal
 * Esto es para evitar trabajar con los productos directamente en las comandas
 * 
 * @author Andre
 */
@Entity
@Table(name = "detalles_comanda")
public class DetallesComanda implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detallesComanda")
    private Long id;

    @Column(name = "cantidad")
    private int cantidad;
    
    @Column(name = "subtotal")
    private double subtotal;
    
    @Column(name = "comentarios", nullable = true, length = 100)
    private String comentarios;
    
    @Column(name = "precio_venta", nullable = false)
    private double precioVenta;
    
    
    //Aparece en varias comandas
    @ManyToOne
    @JoinColumn(name = "id_comanda", nullable = false)
    private Comanda comanda;
    
    //Es un tipo de producto
    @ManyToOne
    @JoinColumn(name = "id_Producto", nullable = false)
    private Producto producto;
    
    
    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
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

    public Comanda getComanda() {
        return comanda;
    }

    public void setComanda(Comanda comanda) {
        this.comanda = comanda;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = precioVenta;
    }
}