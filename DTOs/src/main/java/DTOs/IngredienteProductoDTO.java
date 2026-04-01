/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

/**
 *
 * @author Angel
 */
public class IngredienteProductoDTO {
    
    //ID de la tabla intermedia
    private Long id;
    
    //Cantidad de ingredientes 
    private Double cantidad;

    // IDs de las entidades relacionadas
    private Long productoId;
    private Long ingredienteId;

    //Por si falta poner el nombre en la interfaz
    //Ejemplo dto.setNombreIngrediente(ip.getIngrediente().getNombre());
    private String nombreIngrediente;

    // Constructor vacío
    public IngredienteProductoDTO() {}

    // Constructor completo
    public IngredienteProductoDTO(Long id, Double cantidad, Long productoId, Long ingredienteId, String nombreIngrediente) {
        this.id = id;
        this.cantidad = cantidad;
        this.productoId = productoId;
        this.ingredienteId = ingredienteId;
        this.nombreIngrediente = nombreIngrediente;
    }

    //Constructor si en nombre del ingrediente
    public IngredienteProductoDTO(Double cantidad, Long productoId, Long ingredienteId) {
        this.cantidad = cantidad;
        this.productoId = productoId;
        this.ingredienteId = ingredienteId;
    }
 

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    public Long getIngredienteId() {
        return ingredienteId;
    }

    public void setIngredienteId(Long ingredienteId) {
        this.ingredienteId = ingredienteId;
    }

    public String getNombreIngrediente() {
        return nombreIngrediente;
    }

    public void setNombreIngrediente(String nombreIngrediente) {
        this.nombreIngrediente = nombreIngrediente;
    }
}
