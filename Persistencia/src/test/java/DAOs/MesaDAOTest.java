package DAOs;

import Entidades.Mesa;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MesaDAOTest {

    private MesaDAO mesaDAO;

    @BeforeEach
    public void setUp() {
        mesaDAO = MesaDAO.getInstance();
    }

    // Método auxiliar para generar números de mesa que no choquen
    private int generarNumeroUnico() {
        return (int) (Math.random() * 1000000);
    }

    @Test
    public void testRegistrarMesa() {
        Mesa nueva = new Mesa(generarNumeroUnico(), "DISPONIBLE");
        Mesa resultado = mesaDAO.registrarMesa(nueva);
        
        assertNotNull(resultado.getId());
    }

    @Test
    public void testActualizarMesa() {
        Mesa mesa = new Mesa(generarNumeroUnico(), "DISPONIBLE");
        Mesa persistida = mesaDAO.registrarMesa(mesa);

        persistida.setEstadoMesa("OCUPADA");
        Mesa actualizada = mesaDAO.actualizarMesa(persistida);
        assertEquals("OCUPADA", actualizada.getEstadoMesa());
    }

    @Test
    public void testActualizarEstado() {
        Mesa mesa = new Mesa(generarNumeroUnico(), "DISPONIBLE");
        Mesa persistida = mesaDAO.registrarMesa(mesa);

        assertDoesNotThrow(() -> {
            mesaDAO.actualizarEstado(persistida.getId(), "RESERVADA");
        });
    }

    @Test
    public void testConsultarMesas() {
        Mesa mesa = new Mesa(generarNumeroUnico(), "DISPONIBLE");
        mesaDAO.registrarMesa(mesa);

        List<Mesa> lista = mesaDAO.consultarMesas();
        assertNotNull(lista);
        assertFalse(lista.isEmpty());
    }
}