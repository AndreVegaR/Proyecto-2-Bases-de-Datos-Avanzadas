/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author Angel
 * Esta tabla sirve para poder identificar cada ingrediente y poder calcular su stock
 */
@Entity
@Table(name = "productos_y_sus_ingredientes")
public class producto_con_ingrediente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ProductoIngrediente")
    private Long id;
    
    //Atributo para saber la cantidad de cada ingrediente
    @Column(name = "cantidad", nullable = false)
    private Double cantidad;
    
    /*
    Mapeos de producto e ingredientes a la tabla intermedia 
    */
    //Muchos productoIngrediente pueden tener 1 producto
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    //Muchos productoIngrediente pueden tener 1 ingrediente
    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    public producto_con_ingrediente(Long id, Double cantidad, Producto producto, Ingrediente ingrediente) {
        this.id = id;
        this.cantidad = cantidad;
        this.producto = producto;
        this.ingrediente = ingrediente;
    }

    public producto_con_ingrediente(Double cantidad, Producto producto, Ingrediente ingrediente) {
        this.cantidad = cantidad;
        this.producto = producto;
        this.ingrediente = ingrediente;
    }

    public producto_con_ingrediente() {
    }

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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }
    
    
    
    
}

