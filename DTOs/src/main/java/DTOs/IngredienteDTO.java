/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;
import Enumeradores.UnidadMedida;
/**
 *DTO para la entidad Ingrediente
 * 
 * @author Jazmin
 */
public class IngredienteDTO {
    private Long id;
    private String nombre;
    private UnidadMedida unidadMedida;
    private Double stock;
    private byte[] imagen;
    //Constructor vacio
    public IngredienteDTO() {
    }
    //Constructor completo
    public IngredienteDTO(Long id, String nombre, UnidadMedida unidadMedida, Double stock) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }
    //Getters y Setters
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

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    
    
    
    
}
