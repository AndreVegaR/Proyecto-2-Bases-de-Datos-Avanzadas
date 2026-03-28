package DAOs;

import Entidades.Cliente;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {

    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        clienteDAO = ClienteDAO.getInstance();
    }

    @Test
    void testOperacionesCompletas() {
        
        /**
         * // 1. Probar Guardar
        Cliente nuevo = new Cliente();
        // nuevo.setNombre("Juan"); // Asumiendo que tienes setters
        
        Cliente guardado = clienteDAO.guardarCliente(nuevo);
        
        assertNotNull(guardado);
        assertNotNull(guardado.getId());

        // 2. Probar Ver Todos
        List<Cliente> lista = clienteDAO.verClientes();
        assertFalse(lista.isEmpty());
        assertTrue(lista.stream().anyMatch(c -> c.getId().equals(guardado.getId())));

        // 3. Probar Modificar
        // Nota: Tu método modificarCliente(Long id) en el DAO tiene un detalle: 
        // Solo busca y hace merge de lo que encontró sin cambiar nada.
        Cliente modificado = clienteDAO.modificarCliente(guardado.getId());
        assertNotNull(modificado);
        assertEquals(guardado.getId(), modificado.getId());

        // 4. Probar Eliminar
        Cliente eliminado = clienteDAO.eliminarCliente(guardado.getId());
        assertNotNull(eliminado);
        
        // Verificar que ya no existe
        List<Cliente> listaPostEliminar = clienteDAO.verClientes();
        assertTrue(listaPostEliminar.stream().noneMatch(c -> c.getId().equals(guardado.getId())));
    }

    @Test
    void testSingleton() {
        ClienteDAO instancia1 = ClienteDAO.getInstance();
        ClienteDAO instancia2 = ClienteDAO.getInstance();
        assertSame(instancia1, instancia2);
         */
        
        
    }
}