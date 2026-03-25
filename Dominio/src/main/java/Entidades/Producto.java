/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import Enumeradores.EstadoProducto;
import Enumeradores.TipoProducto;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Angel
 * Tabla de producto junto con su union con la tabla de ingredientes mediante
 * una tabla intermedia
 */
@Entity
@Table(name = "Productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_Producto")
    private Long id;
    
    @Column(name = "nombre",nullable = false,unique = true)
    private String nombre;
    
    /*
    Este es un enum para los estados del producto ya sea activo e inactivo
    EnumType.String guarda el nombre del string
    Por ejemplo Producto p = new Producto();
    p.setTipo(TipoProducto.BEBIDA);
    */
   @Enumerated(EnumType.STRING)  
   @Column(name = "tipo_producto",nullable = false)
   private TipoProducto tipoProducto;

   
   @Column(name = "estado", nullable = false)
   private EstadoProducto estadoProducto = EstadoProducto.ACTIVO;
   
   //mapeo para tabla intermedia entre product e ingrediente
   @OneToMany(mappedBy = "producto",cascade = CascadeType.PERSIST)
   private List<IngredienteProducto> productosIngredientes = new ArrayList<>();

   
    public Producto(Long id, String nombre, TipoProducto tipo) {
        this.id = id;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
    }

    public Producto(String nombre, TipoProducto tipo) {
        this.nombre = nombre;
        this.tipoProducto = tipo;
    }
    
    public Producto() {
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

    public TipoProducto getTipo() {
        return tipoProducto;
    }

    public void setTipo(TipoProducto tipoProducto) {
        this.tipoProducto = tipoProducto;
    }

    public EstadoProducto isEstado() {
        return estadoProducto;
    }

    public void setEstado(EstadoProducto estadoProducto) {
        this.estadoProducto = estadoProducto;
    }

    public List<IngredienteProducto> getProductosIngredientes() {
        return productosIngredientes;
    }

    public void setProductosIngredientes(List<IngredienteProducto> productosIngredientes) {
        this.productosIngredientes = productosIngredientes;
    } 
}