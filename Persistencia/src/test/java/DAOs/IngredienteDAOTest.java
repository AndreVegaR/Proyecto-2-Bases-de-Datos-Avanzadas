package DAOs;

import Entidades.Ingrediente;
import Enumeradores.UnidadMedida;
import excepciones.PersistenciaException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas para IngredienteDAO.
 *
 * @author Jazmin
 */
class IngredienteDAOTest {

    private IngredienteDAO ingredienteDAO;

    @BeforeEach
    void setUp() {
        ingredienteDAO = IngredienteDAO.getInstance();
    }

    @Test
    void testGuardarIngredienteOk() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Sal");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(10.0);

        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        assertNotNull(guardado);
        assertNotNull(guardado.getId());
        assertEquals("Sal", guardado.getNombre());
        assertEquals(UnidadMedida.KILOGRAMOS, guardado.getUnidadMedida());
        assertEquals(10.0, guardado.getStock());
    }

    @Test
    void testActualizarStockOk() {
        // Primero se guarda
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Azucar");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(5.0);
        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        // Actualiza su stock
        Ingrediente actualizado = ingredienteDAO.actualizarStock(guardado.getId(), 20.0);

        assertNotNull(actualizado);
        assertEquals(20.0, actualizado.getStock());

    }

    @Test
    void testEliminarIngredienteOk() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Pimienta");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(3.0);
        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        Ingrediente eliminado = ingredienteDAO.eliminarIngrediente(guardado.getId());

        assertNotNull(eliminado);
        assertNull(ingredienteDAO.buscarPorId(guardado.getId()));
    }

    @Test
    void testBuscarPorIdOk() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Oregano");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(1.0);
        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        Ingrediente encontrado = ingredienteDAO.buscarPorId(guardado.getId());

        assertNotNull(encontrado);
        assertEquals(guardado.getId(), encontrado.getId());
        assertEquals("Oregano", encontrado.getNombre());

    }

    @Test
    void testBuscarIngredientesPorNombreParcial() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Comino");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(2.0);
        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        List<Ingrediente> lista = ingredienteDAO.buscarIngredientes("com", null);

        assertNotNull(lista);
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(guardado.getId())));

    }
      @Test
    void testBuscarIngredientesPorUnidadMedida() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Leche");
        nuevo.setUnidadMedida(UnidadMedida.LITROS);
        nuevo.setStock(5.0);
        Ingrediente guardado = ingredienteDAO.guardarIngrediente(nuevo);

        List<Ingrediente> lista = ingredienteDAO.buscarIngredientes(null, UnidadMedida.LITROS);

        assertNotNull(lista);
        assertTrue(lista.stream().anyMatch(i -> i.getId().equals(guardado.getId())));

    }
    
    @Test
    void testElimiarIngredienteInexistente(){
        Ingrediente eliminado = ingredienteDAO.eliminarIngrediente(Long.MAX_VALUE);
        assertNull(eliminado);
    }

    @Test
    void testActualizarStockIngredienteInexistente() {
        assertThrows(PersistenciaException.class, () -> {
            ingredienteDAO.actualizarStock(Long.MAX_VALUE, 15.0);
        });
    }

    @Test
    void testBuscarPorIdInexistente() {
        assertDoesNotThrow(() -> {
            ingredienteDAO.buscarPorId(-99L);
        });
    }

    @Test
    void testBuscarIngredientesTodosConNull() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("sal");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(1.0);
        ingredienteDAO.guardarIngrediente(nuevo);

        List<Ingrediente> lista = ingredienteDAO.buscarIngredientes(null, null);

        assertTrue(lista.stream().anyMatch(i -> i.getNombre().equals("sal")));
    }

    @Test
    void testExisteIngredienteDuplicado() {
        Ingrediente nuevo = new Ingrediente();
        nuevo.setNombre("Canela");
        nuevo.setUnidadMedida(UnidadMedida.KILOGRAMOS);
        nuevo.setStock(1.0);
        ingredienteDAO.guardarIngrediente(nuevo);

        boolean existe = ingredienteDAO.existeIngrediente("Canela", UnidadMedida.KILOGRAMOS);

        assertTrue(existe);

    }

    @Test
    void testExisteIngredienteNoExiste() {
        boolean existe = ingredienteDAO.existeIngrediente("IngredienteInexistente", UnidadMedida.PIEZAS);
        assertFalse(existe);
    }

}
