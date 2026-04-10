/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Entidades.Producto;
import Enumeradores.EstadoProducto;
import Enumeradores.TipoProducto;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Usuario
 */
public class ProductoDAOTest {
    
    private ProductoDAO productoDAO;
    
    @BeforeEach
    void setUp(){
        productoDAO = ProductoDAO.getInstance();
    }
    
    @Test
    public void testGuardarProducto_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Sprite");
    p.setPrecio(15.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipoProducto(TipoProducto.BEBIDA);

    Producto guardado = dao.guardarProducto(p);

    assertNotNull(guardado.getId());
    }
    
    //Falla por que el producto es null
    @Test
    public void testGuardarProducto_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    assertThrows(PersistenciaException.class, () -> {
        dao.guardarProducto(null);
    });
    }
    
    @Test
    public void testActualizarProducto_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Fanta");
    p.setPrecio(12.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipo(TipoProducto.ENTRADA);

    p = dao.guardarProducto(p);

    p.setPrecio(18.0);
    dao.actualizarProducto(p);

    Producto actualizado = dao.buscarProductoPorId(p.getId());

    assertEquals(18.0, actualizado.getPrecio());
    }
    
    //Falla el test por que el producto es null
    @Test
    public void testActualizarProducto_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    assertThrows(PersistenciaException.class, () -> {
        dao.actualizarProducto(null);
    });
    }
    
    @Test
    public void testEliminarProducto_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Pepsi");
    p.setPrecio(10.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipoProducto(TipoProducto.BEBIDA);

    p = dao.guardarProducto(p);

    assertNotNull(p.getId());

    dao.eliminarProducto(p.getId());
    }
    
    //Falla por que el id no existe
    @Test
    public void testEliminarProducto_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

        dao.eliminarProducto(99999L);
    }
    
    @Test
    public void testBuscarProducto_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Coca");
    p.setPrecio(20.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipoProducto(TipoProducto.BEBIDA);

    p = dao.guardarProducto(p);

    Producto encontrado = dao.buscarProductoPorId(p.getId());

    assertEquals("Coca", encontrado.getNombre());
    }
    
    //Falla por que el id es inexistente
    @Test
    public void testBuscarProducto_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    assertThrows(PersistenciaException.class, () -> {
        dao.buscarProductoPorId(99999L);
    });
    }
    
    @Test
    public void testVerTodos_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    List<Producto> lista = dao.verTodosLosProductos();

    assertNotNull(lista);
    }
    
    @Test
    public void testCambiarEstado_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Agua");
    p.setPrecio(8.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipoProducto(TipoProducto.BEBIDA);

    p = dao.guardarProducto(p);

    dao.cambiarEstado(p.getId(), EstadoProducto.INACTIVO);

    Producto actualizado = dao.buscarProductoPorId(p.getId());

    assertEquals(EstadoProducto.INACTIVO, actualizado.getEstadoProducto());
    }
    
    //Falla debido al id invalido
    @Test
    public void testCambiarEstado_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto resultado = dao.cambiarEstado(99999L, EstadoProducto.INACTIVO);

    assertNull(resultado);
    }
    
    @Test
    public void testExisteNombre_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p = new Producto();
    p.setNombre("Doritos");
    p.setPrecio(12.0);
    p.setEstado(EstadoProducto.ACTIVO);
    p.setTipoProducto(TipoProducto.BEBIDA);

    dao.guardarProducto(p);

    assertTrue(dao.existeNombre("Doritos"));
    }
    
    //Falla por que ese producto no existe
    @Test
    public void testExisteNombre_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    assertFalse(dao.existeNombre("ProductoQueNoExiste"));
    }
    
    @Test
    public void testExisteNombreParaActualizar_NoFalla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    Producto p1 = new Producto();
    p1.setNombre("Galletas");
    p1.setPrecio(10.0);
    p1.setEstado(EstadoProducto.ACTIVO);
    p1.setTipoProducto(TipoProducto.BEBIDA);

    Producto p2 = new Producto();
    p2.setNombre("Pan");
    p2.setPrecio(5.0);
    p2.setEstado(EstadoProducto.ACTIVO);
    p2.setTipoProducto(TipoProducto.ENTRADA);

    p1 = dao.guardarProducto(p1);
    p2 = dao.guardarProducto(p2);

    boolean existe = dao.existeNombreParaActualizar("Galletas", p2.getId());

    assertTrue(existe);
    }
    
    //El nombre no existe y falla
    @Test
    public void testExisteNombreParaActualizar_Falla() {
    ProductoDAO dao = ProductoDAO.getInstance();

    boolean existe = dao.existeNombreParaActualizar("NombreUnico", 1L);

    assertFalse(existe);
    }
    
}
