package DAOs;

import Entidades.Cliente;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Teste de clientEDAO
 * 
 * @author Andre
 */
public class ClienteDAOTest {

    private ClienteDAO clienteDAO;
    private List<Long> idsParaLimpiar;

    @BeforeEach
    public void setUp() {
        clienteDAO = ClienteDAO.getInstance();
        idsParaLimpiar = new ArrayList<>();
    }

    @AfterEach
    public void tearDown() {
        for (Long id : idsParaLimpiar) {
            try {
                clienteDAO.eliminarCliente(id);
            } catch (Exception e) {
            }
        }
    }

    private Cliente crearClienteMock(String nombre) {
        return new Cliente(
            nombre, 
            "Paterno", 
            "Materno", 
            "1234567890", 
            LocalDateTime.now(), 
            nombre.replace(" ", "") + "@test.com"
        );
    }

    @Test
    public void testRegistrarCliente() {
        Cliente nuevo = crearClienteMock("Test Registro");
        Cliente resultado = clienteDAO.registrarCliente(nuevo);
        
        if (resultado != null && resultado.getId() != null) {
            idsParaLimpiar.add(resultado.getId());
        }

        assertNotNull(resultado.getId());
        assertEquals("Test Registro", resultado.getNombres());

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.registrarCliente(null);
        });
    }

    @Test
    public void testActualizarCliente() {
        Cliente cliente = crearClienteMock("Original");
        Cliente persistido = clienteDAO.registrarCliente(cliente);
        idsParaLimpiar.add(persistido.getId());
        
        persistido.setNombres("Actualizado");
        Cliente resultado = clienteDAO.actualizarCliente(persistido);
        assertEquals("Actualizado", resultado.getNombres());

        assertThrows(PersistenciaException.class, () -> {
            clienteDAO.actualizarCliente(null);
        });
    }

    @Test
    public void testEliminarCliente() {
        Cliente cliente = crearClienteMock("A Eliminar");
        Cliente persistido = clienteDAO.registrarCliente(cliente);
        Long id = persistido.getId();

        Cliente eliminado = clienteDAO.eliminarCliente(id);
        assertNotNull(eliminado);
        assertNull(clienteDAO.buscarPorId(id));

        Cliente inexistente = clienteDAO.eliminarCliente(999999L);
        assertNull(inexistente);
    }

    @Test
    public void testConsultarClientes() {
        Cliente c = crearClienteMock("Consulta Test");
        Cliente registrado = clienteDAO.registrarCliente(c);
        idsParaLimpiar.add(registrado.getId());

        List<Cliente> lista = clienteDAO.consultarClientes();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }

    @Test
    public void testBuscarPorId() {
        Cliente cliente = crearClienteMock("Busqueda");
        Cliente persistido = clienteDAO.registrarCliente(cliente);
        idsParaLimpiar.add(persistido.getId());

        Cliente encontrado = clienteDAO.buscarPorId(persistido.getId());
        assertNotNull(encontrado);
        assertEquals(persistido.getId(), encontrado.getId());

        Cliente noEncontrado = clienteDAO.buscarPorId(-1L);
        assertNull(noEncontrado);
    }
}