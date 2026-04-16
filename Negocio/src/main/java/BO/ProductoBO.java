/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BO;

import DAOs.ProductoDAO;
import DTOs.IngredienteProductoDTO;
import DTOs.ProductoDTO;
import Entidades.Ingrediente;
import Entidades.IngredienteProducto;
import Entidades.Producto;
import Enumeradores.EstadoProducto;
import Enumeradores.TipoProducto;
import excepciones.NegocioException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import utilerias.UtilNegocio;
import DAOs.IngredienteDAO;

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
    Metodo que elimina un producto y su relación con la tabla intermedia mediante su id
    @param id producto
    */
    public void eliminarProducto(Long id){
        //Validaciones
        UtilNegocio.esNulo(id);
       ProductoDAO.getInstance().eliminarProducto(id);
        
    }
    
     /*
    Metodo que actualiza un producto mediante su id
    @param id producto
    @return producto
    */
    public ProductoDTO actualizarProducto(ProductoDTO dto){    
        //Creamos una lista de ingredientes para agregarselo a el producto
        List<IngredienteProducto> nuevosIngredientes = new ArrayList<>();
        
        //Validaciones
        UtilNegocio.esNulo(dto);
        UtilNegocio.esCadenadaVacia(dto.getNombre(), "nombre");
        UtilNegocio.esCadenadaVacia(dto.getDescripcion(), "descripcion");
        UtilNegocio.esNulo(dto.getDescripcion());
        UtilNegocio.validarDescripcion(dto.getDescripcion());
        UtilNegocio.validarNombre(dto.getNombre());
        UtilNegocio.esNulo(dto.getEstadoProducto());
        UtilNegocio.esNulo(dto.getTipoProducto());
        //Convertimos a string el precio para poder usar el validador de Precio de UtilNegocio
        String precio = String.valueOf(dto.getPrecio());
        UtilNegocio.validarPrecio(precio);
             
        Producto producto = ProductoDAO.getInstance().buscarProductoPorId(dto.getId());
        //Validamos que no exista otro producto con el mismo nombre al momento de actualizar el producto
        //Busca en el dao a ver si hay un nombre igual al producto que le vamos a cambiar el nombre
        if (ProductoDAO.getInstance().existeNombreParaActualizar(dto.getNombre(), dto.getId())) {
            throw new NegocioException("Ya existe un producto con ese nombre");
        }
        if(producto == null){
            throw new NegocioException("Producto no encontrado");
        }
        //Le damos los valores nuevos
        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());

        producto.setEstado(
            EstadoProducto.valueOf(dto.getEstadoProducto().name())
        );

        producto.setTipo(
            TipoProducto.valueOf(dto.getTipoProducto().name())
        );
        producto.setImagen(dto.getImagen());
        producto.setDescripcion(dto.getDescripcion());
        //INGREDIENTES
        /*
        En esta parte es parecida a la de agregarProducto
        Primero limpiamos la lista para que no esten los otros ingredientes
        */
         producto.getProductosIngredientes().clear();
         //Iteramos en la lista de ingredientes que tiene el producto y nos fijamos si existen
         for (IngredienteProductoDTO ipDTO : dto.getIngredientes()) {
            Ingrediente ingrediente = IngredienteDAO.getInstance().buscarPorId(ipDTO.getIngredienteId());
        if (ingrediente == null) {
            throw new NegocioException("Ingrediente no encontrado");
        }
        //Se crea un ingredienteProducto con los datos de los ingredientes y el producto y los relacionamos
        IngredienteProducto ip = new IngredienteProducto();
        ip.setProducto(producto);
        ip.setIngrediente(ingrediente);
        ip.setCantidad(ipDTO.getCantidad());
        //Agregamos a la lista la tabla intermedia de ingredientes
        nuevosIngredientes.add(ip);
       }
         
        //Validamos que no este vacia la lista
         if(dto.getIngredientes() == null || dto.getIngredientes().isEmpty()) {
         throw new NegocioException("Debe agregar al menos un ingrediente");
        }
        //Relacionamos producto con la nueva tabla de ingredientes
        producto.setProductosIngredientes(nuevosIngredientes);

        //Actualizamos el producto
        Producto actualizado = ProductoDAO.getInstance().actualizarProducto(producto);
        
        //Lo regresamos como DTO
        ProductoDTO returnDTO = new ProductoDTO();
        returnDTO.setId(actualizado.getId());
        returnDTO.setNombre(actualizado.getNombre());
        returnDTO.setPrecio(actualizado.getPrecio());
        returnDTO.setEstadoProducto(ProductoDTO.EstadoProducto.valueOf(actualizado.getEstadoProducto().name()));
        returnDTO.setTipoProducto( ProductoDTO.TipoProducto.valueOf(actualizado.getTipo().name()) );
        returnDTO.setImagen(actualizado.getImagen());
        returnDTO.setDescripcion(actualizado.getDescripcion());

        return returnDTO;
    }
   
    /*
    Metodo para ver todos los productos
    @return List<Producto>
    */
    public List<ProductoDTO> verTodosLosProductos(){
        List<Producto> listaProductos = ProductoDAO.getInstance().verTodosLosProductos();
        //Validamos que no sea null
        UtilNegocio.esNulo(listaProductos);
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
            dto.setPrecio(producto.getPrecio());
            dto.setTipoProducto(
                ProductoDTO.TipoProducto.valueOf(producto.getTipo().name())
            );
            dto.setEstadoProducto(
                ProductoDTO.EstadoProducto.valueOf(producto.getEstadoProducto().name())
            );
            dto.setDescripcion(producto.getDescripcion());
              //MAPEO DE INGREDIENTES
        List<IngredienteProductoDTO> listaIngredientes = producto.getProductosIngredientes().stream().map(ip -> {
            IngredienteProductoDTO ipDTO = new IngredienteProductoDTO();
            ipDTO.setIngredienteId(ip.getIngrediente().getId());
            ipDTO.setCantidad(ip.getCantidad());
            return ipDTO;
        }) .collect(Collectors.toList());
        
            dto.setIngredientes(listaIngredientes);
            dto.setImagen(producto.getImagen());
            return dto;
        })
        .collect(Collectors.toList());
    }
    
   /*
    Metodo cambiar el estado de un Producto
    @return ProductoDTO con otro estado
    @param id del producto, estado nuevo del producto
    */
    
    public ProductoDTO cambiarEstado(Long id,EstadoProducto estado){
    //Validaciones    
    UtilNegocio.esNulo(id);
    UtilNegocio.esNulo(estado);  

    Producto producto = ProductoDAO.getInstance().cambiarEstado(id, estado);

    ProductoDTO dto = new ProductoDTO();
    dto.setId(producto.getId());
    dto.setEstadoProducto(ProductoDTO.EstadoProducto.valueOf(producto.getEstadoProducto().name()));

    return dto;  
    }
    
    
    /*
    Metodo para agregar un producto
    @return producto agregado
    @param ProductoDTO
    En este metodo se hace la relacion con la tabla intemedia de IngredienteProductoDTO 
    Se elije el ingrediente mediante un DAO de buscar ingrediente mediante la tabla intermedia y validamos que exista
    Si existe creamos un IngredienteProducto y le damos los valores del dto de IngredienteProductoDTO y despues relacionamos producto con la tabla intermedia
    y visceversa
    Al final se regresa un ProductoDTO mapeado
    */
    public ProductoDTO agregarProducto(ProductoDTO productoDTO){
       
        List<IngredienteProductoDTO> ingredientesDTO = productoDTO.getIngredientes();

        // VALIDACIONES
        UtilNegocio.esCadenadaVacia(productoDTO.getNombre(), "nombre");
        UtilNegocio.esNulo(productoDTO.getPrecio());
        UtilNegocio.esCadenadaVacia(productoDTO.getDescripcion(), "descripcion");
        UtilNegocio.esNulo(productoDTO.getDescripcion());
        UtilNegocio.validarNombre(productoDTO.getNombre());
        UtilNegocio.esNulo(productoDTO.getTipoProducto());
        UtilNegocio.validarDescripcion(productoDTO.getDescripcion());
        //Convertimos a string el precio para poder usar el validador de Precio de UtilNegocio
        String precio = String.valueOf(productoDTO.getPrecio());
        UtilNegocio.validarPrecio(precio);

        if (ProductoDAO.getInstance().existeNombre(productoDTO.getNombre())) {
            throw new NegocioException("Ya existe un producto con ese nombre");
        }

        if (ingredientesDTO == null || ingredientesDTO.isEmpty()) {
            throw new NegocioException("Debe agregar al menos un ingrediente");
        }
        // MAPEO PRODUCTO
        Producto productoNuevo = new Producto();
        productoNuevo.setNombre(productoDTO.getNombre());
        productoNuevo.setPrecio(productoDTO.getPrecio());
        productoNuevo.setImagen(productoDTO.getImagen());

        productoNuevo.setTipo(TipoProducto.valueOf(productoDTO.getTipoProducto().name()));
        //Por default activo
        productoNuevo.setEstado(EstadoProducto.ACTIVO);
        productoNuevo.setDescripcion(productoDTO.getDescripcion());

        // INGREDIENTES
        for (IngredienteProductoDTO dto : ingredientesDTO) {
            if (dto.getCantidad() <= 0) {
                throw new NegocioException("Cantidad inválida");
            }
            //Vemos si existe el ingrediente
            Ingrediente ingrediente = IngredienteDAO.getInstance().buscarPorId(dto.getIngredienteId());

            if (ingrediente == null) {
                throw new NegocioException("Ingrediente no encontrado");
            }
       
            //Si existe creamos la tabla intermedia y le damos los valores del dto
            IngredienteProducto ip = new IngredienteProducto();
            ip.setCantidad(dto.getCantidad());
            ip.setIngrediente(ingrediente);
            
            //Los relacionamos con el producto a crear
            productoNuevo.getProductosIngredientes().add(ip);
            //Relacionamos la tabla intermedia con el producto a crear
            ip.setProducto(productoNuevo);
        }
        Producto guardado = ProductoDAO.getInstance().guardarProducto(productoNuevo);
        ProductoDTO regresarDTO = new ProductoDTO();
        regresarDTO.setId(guardado.getId());
        regresarDTO.setNombre(guardado.getNombre());
        regresarDTO.setPrecio(guardado.getPrecio());
        regresarDTO.setTipoProducto(ProductoDTO.TipoProducto.valueOf(guardado.getTipo().name()));
        regresarDTO.setEstadoProducto(ProductoDTO.EstadoProducto.valueOf(guardado.getEstadoProducto().name()));
        regresarDTO.setImagen(guardado.getImagen());
        regresarDTO.setDescripcion(guardado.getDescripcion());

        return regresarDTO;
    }
    
   /*
    Metodo para buscar un producto pro su id
    @return producto 
    @param Long id
    */  
    public ProductoDTO buscarProductoPorId(Long id){
        //Validaciones 
        UtilNegocio.esNulo(id);
        Producto buscarProducto = new Producto();
        buscarProducto.setId(id);
        ProductoDAO.getInstance().buscarProductoPorId(buscarProducto.getId());
        ProductoDTO regresarProducto = new ProductoDTO();
        return regresarProducto;
    }
}