/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.ProductoDAO;
import DTOs.ProductoDTO;
import Entidades.Producto;
import Enumeradores.EstadoProducto;
import Enumeradores.TipoProducto;
import java.util.List;
import java.util.stream.Collectors;
import utilerias.Utilerias;

/**
 *
 * @author Angel
 * BO de producto con la lógica del singleton
 */
public class ProductoBO {
    
    private static ProductoBO instancia;
    
    private ProductoBO(){
        
    }
    
    public static ProductoBO getInstance(){
        if(instancia == null){
            return instancia = new ProductoBO();
        }
        return instancia;
    }
    
    /*
    Metodo para agregar un producto
    @return producto agregado
    @param producto
    */
    
    public Producto agregarProducto(ProductoDTO producto){
        //Validaciones
        Utilerias.esCadenadaVacia(producto.getNombre(), "nombre");
        Utilerias.esNulo(producto.getEstadoProducto());
        Utilerias.esNulo(producto.getTipoProducto());
      
        //Mapeo a entidad
        Producto productoNuevo = new Producto();
        productoNuevo.setNombre(producto.getNombre());
        
        //TipoProducto 
        //Primero hacemos un set con el nuevo tipo del producto
        //Depues convertimos un string a un tipoProducto mediante lo que ingresó el usuario
        //name es de enum a texto por que valueOf solo acepta texto y no enums
        productoNuevo.setTipo(TipoProducto.valueOf(producto.getTipoProducto().name().toUpperCase()));

        // EstadoProducto 
        //Primero hacemos un set del nuevo producto
        //Despues convertimos un string a un EstadoProducto mediante lo que ingresó el usuario en presentacion
        //El name es para el enum a texto por que valueOf solo acepta string y no enums
        productoNuevo.setEstado(EstadoProducto.valueOf(producto.getEstadoProducto().name().toUpperCase()));
        
        ProductoDAO.getInstance().guardarProducto(productoNuevo);
        return productoNuevo;
    
    }
    
    /*
    Metodo que elimina un producto y su relación con la tabla intermedia mediante su id
    @param id producto
    */
    public void eliminarProducto(Long id){
        //Validaciones
        Utilerias.esNulo(id);
       ProductoDAO.getInstance().eliminarProducto(id);
        
    }
    
     /*
    Metodo que actualiza un producto mediante su id
    @param id producto
    @return producto
    */
    public Producto actualizarProducto(ProductoDTO dto){
        //Validaciones
        Utilerias.esNulo(dto);
        Utilerias.esCadenadaVacia(dto.getNombre(), "nombre");
        Utilerias.esNulo(dto.getEstadoProducto());
        Utilerias.esNulo(dto.getTipoProducto());
        //Si no existe el producto lanzamos una excepción
        Producto existeProducto = ProductoDAO.getInstance().buscarProductoPorId(dto.getId());
        if(existeProducto == null){
             throw new RuntimeException("Producto no encontrado");
        }
      existeProducto.setNombre(dto.getNombre());
      //Convertimos lo que escribio el usuario y lo hacemos string mediante valueOf
      //Por ejemplo si escribe activo se convierte a Activo y el EstadoProducto lo convierte a Enum y lo manda al set
      existeProducto.setEstado(EstadoProducto.valueOf(dto.getEstadoProducto().name().toUpperCase()));
      //Por ejemplo si escribe bebida se convierte a bebida y el TipoProducto lo convierte a Enum y lo manda al set
      existeProducto.setTipo(TipoProducto.valueOf(dto.getTipoProducto().name().toUpperCase()));
      
      //Regresamos el producto
      return ProductoDAO.getInstance().actualizarProducto(existeProducto);
    }
    
    /*
    Metodo para ver todos los productos
    @return List<Producto>
    */
    public List<ProductoDTO> verTodosLosProductos(){
        List<Producto> listaProductos = ProductoDAO.getInstance().verTodosLosProductos();
        //Validamos que no sea null
        Utilerias.esNulo(listaProductos);
        /*
        En esta parte primero con el stream hacemos un flujo o linea de objetos 1 por 1 
        En el map pasamos el Producto a ProductoDTO para poder utilizarlo sin dañar la logica y no usar a la entidad
        Dentro del mapeo creamos un DTO y tomamos los valores de la listaProductos y se los damos al dto
        Al final en collect convertimos todo a una lista
        */
          return listaProductos.stream().map(producto -> {
            ProductoDTO dto = new ProductoDTO();
            dto.setId(producto.getId());
            dto.setNombre(producto.getNombre());
            dto.setTipoProducto(
                ProductoDTO.TipoProducto.valueOf(producto.getTipo().name())
            );
            dto.setEstadoProducto(
                ProductoDTO.EstadoProducto.valueOf(producto.getEstadoProducto().name())
            );
            //Regresamos el dto que mapeamos cada uno
            return dto;
        })
        .collect(Collectors.toList());
    }
    
   /*
    Metodo cambiar el estado de un Producto
    @return Producto con otro estado
    @param id del producto, estado nuevo del producto
    */
    
    public Producto cambiarEstado(Long id,EstadoProducto estado){
        //Validaciones
        Utilerias.esNulo(id);
        Utilerias.esNulo(estado);  
        //En el dao se valida que exista un producto con ese id asi que solo lo regresamos
        return ProductoDAO.getInstance().cambiarEstado(id, estado);    
    }
    
    
}
