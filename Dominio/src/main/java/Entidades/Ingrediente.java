/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Angel
 * Tabla de ingrediente junto con su union con la tabla de producto mediante
 * una tabla intermedia
 */
@Entity
@Table(name = "ingredientes")
public class Ingrediente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Ingrediente")
    private Long id;
    
    @Column(name = "nombre",nullable = false)
    private String nombre;
    
    @Column(name = "unidad_de_medida",nullable = false)
    private String unidadMedida;
    
    @Column(name = "stock",nullable = false)
    private Double stock;
    
    //mapeo para tabla intermedia entre product e ingrediente
    @OneToMany(mappedBy = "ingrediente")
    private List<producto_con_ingrediente> productosIngredientes = new ArrayList<>();

    public Ingrediente(Long id, String nombre, String unidadMedida, Double stock) {
        this.id = id;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }

    public Ingrediente(String nombre, String unidadMedida, Double stock) {
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stock = stock;
    }
     

    public Ingrediente() {
    }
    
    

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

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public List<producto_con_ingrediente> getProductosIngredientes() {
        return productosIngredientes;
    }

    public void setProductosIngredientes(List<producto_con_ingrediente> productosIngredientes) {
        this.productosIngredientes = productosIngredientes;
    }
    
    
    
}
