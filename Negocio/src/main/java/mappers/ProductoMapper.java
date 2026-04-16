/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mappers;

import DTOs.ProductoDTO;
import DTOs.ProductoDTO.EstadoProducto;
import DTOs.ProductoDTO.TipoProducto;
import Entidades.Producto;


/**
 *
 * @author Angel
 */
public class ProductoMapper {
    
    
    public static Producto MapearDTOEntidad(ProductoDTO dto){
        Producto productoNuevo = new Producto();
        productoNuevo.setId(dto.getId());
        productoNuevo.setNombre(dto.getNombre());
        productoNuevo.setPrecio(dto.getPrecio());
        productoNuevo.setImagen(dto.getImagen());
        productoNuevo.setDescripcion(dto.getDescripcion());
        productoNuevo.setEstado(Enumeradores.EstadoProducto.valueOf(dto.getEstadoProducto().name()));
        productoNuevo.setTipo(Enumeradores.TipoProducto.valueOf(dto.getTipoProducto().name()));
        return productoNuevo;
    }
    
    public static ProductoDTO MapearEntidadDTO(Producto pr){
        ProductoDTO regresarDTO = new ProductoDTO();
        regresarDTO.setId(pr.getId());
        regresarDTO.setNombre(pr.getNombre());
        regresarDTO.setPrecio(pr.getPrecio());
        regresarDTO.setDescripcion(pr.getDescripcion());
        regresarDTO.setTipoProducto(ProductoDTO.TipoProducto.valueOf(pr.getTipo().name()));
        regresarDTO.setEstadoProducto(ProductoDTO.EstadoProducto.valueOf(pr.getEstadoProducto().name()));
        regresarDTO.setImagen(pr.getImagen());
        return regresarDTO;
    }
    
    
    
    
}
