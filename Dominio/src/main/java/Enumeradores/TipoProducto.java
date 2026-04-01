package Enumeradores;

/**
 * Posibles tipos de productos
 * @author Andre
 * */
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