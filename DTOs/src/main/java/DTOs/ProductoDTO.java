/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.util.List;
/**
 *
 * @author Angel
 */
public class ProductoDTO {
    
    private Long id;
    private String nombre;
    private TipoProducto tipoProducto;
    private EstadoProducto estadoProducto;
    
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
    
}
