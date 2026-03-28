package DAOs;

import Entidades.ClienteFrecuente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClienteFrecuenteDAOTest {

    private ClienteFrecuenteDAO dao;

    @BeforeEach
    void setUp() {
        dao = ClienteFrecuenteDAO.getInstance();
    }

    @Test
    void testFlujoCompletoClienteFrecuente() {
        // 1. Test Guardar
        ClienteFrecuente nuevo = new ClienteFrecuente();
        nuevo.setNombres("Jazmin");
        nuevo.setApellidoPaterno("Perez");
        nuevo.setApellidoMaterno("Garcia");
        nuevo.setTelefono("1234567890");
        nuevo.setCorreo("jazmin@mail.com");

        ClienteFrecuente guardado = dao.guardarCliente(nuevo);
        
        assertNotNull(guardado.getId());
        assertEquals("Jazmin", guardado.getNombres());

        // 2. Test Buscar por ID
        ClienteFrecuente buscado = dao.buscarPorId(guardado.getId());
        assertNotNull(buscado);
        assertEquals(guardado.getId(), buscado.getId());

        // 3. Test Modificar
        buscado.setNombres("Jazmin Editado");
        ClienteFrecuente modificado = dao.modificarCliente(buscado);
        
        assertEquals("Jazmin Editado", modificado.getNombres());
        assertEquals(buscado.getId(), modificado.getId());

        // 4. Test Ver Clientes (Lista)
        List<ClienteFrecuente> lista = dao.verClientes();
        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(c -> c.getId().equals(guardado.getId())));

        // 5. Test Eliminar
        ClienteFrecuente eliminado = dao.eliminarCliente(guardado.getId());
        assertNotNull(eliminado);
        
        ClienteFrecuente postEliminado = dao.buscarPorId(guardado.getId());
        assertNull(postEliminado);
    }

    @Test
    void testSingleton() {
        ClienteFrecuenteDAO instancia1 = ClienteFrecuenteDAO.getInstance();
        ClienteFrecuenteDAO instancia2 = ClienteFrecuenteDAO.getInstance();
        assertSame(instancia1, instancia2);
    }

    @Test
    void testModificarInexistenteLanzaExcepcion() {
        ClienteFrecuente inexistente = new ClienteFrecuente();
        inexistente.setId(999999L); // ID que no existe
        
        assertThrows(RuntimeException.class, () -> {
            dao.modificarCliente(inexistente);
        });
    }
}