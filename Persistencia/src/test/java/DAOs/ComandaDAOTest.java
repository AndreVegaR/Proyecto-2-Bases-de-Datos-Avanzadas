package DAOs;

import Entidades.Comanda;
import Entidades.Mesa;
import Entidades.Producto;
import Entidades.DetallesComanda;
import Enumeradores.TipoProducto;
import Enumeradores.EstadoProducto;
import excepciones.PersistenciaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ComandaDAOTest {

    private ComandaDAO comandaDAO;
    private List<Long> idsLimpieza;
    private Mesa mesaMock;

    @BeforeEach
    public void setUp() {
        comandaDAO = ComandaDAO.getInstance();
        idsLimpieza = new ArrayList<>();
        
        // Evitamos error de duplicidad usando un número de mesa aleatorio
        int numeroAzar = (int) (Math.random() * 99999);
        mesaMock = new Mesa(numeroAzar, "DISPONIBLE");
        
        // Registramos la mesa para que exista en la BD antes de la comanda
        MesaDAO.getInstance().registrarMesa(mesaMock);
    }

    @AfterEach
    public void tearDown() {
        for (Long id : idsLimpieza) {
            try {
                comandaDAO.eliminarDetallesDeComanda(id);
                comandaDAO.eliminarComanda(id);
            } catch (Exception e) {
                // Silencioso para no interrumpir otros tests
            }
        }
    }

    private Comanda crearComandaMock() {
        Comanda c = new Comanda();
        c.setTotal(500.0);
        // Folio aleatorio para evitar conflictos de unicidad
        c.setFolio("F-" + (int)(Math.random() * 100000));
        c.setEstado("PENDIENTE");
        c.setMesa(mesaMock);
        c.setFechaRegistro(LocalDateTime.now());
        return c;
    }

    @Test
    public void testRegistrarComanda() {
        Comanda nueva = crearComandaMock();
        Comanda resultado = comandaDAO.registrarComanda(nueva);
        
        if (resultado != null && resultado.getId() != null) {
            idsLimpieza.add(resultado.getId());
        }

        assertNotNull(resultado.getId());
        assertTrue(resultado.getTotal() > 0);
    }

    @Test
    public void testConsultarComanda() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(p.getId());

        Comanda encontrada = comandaDAO.consultarComanda(p.getId());
        assertNotNull(encontrada);
        assertEquals(p.getId(), encontrada.getId());
    }

    @Test
    public void testActualizarComanda() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(p.getId());

        p.setEstado("PAGADA");
        Comanda actualizada = comandaDAO.actualizarComanda(p);
        assertEquals("PAGADA", actualizada.getEstado());
    }

    @Test
    public void testEliminarComanda() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        Long id = p.getId();

        Comanda eliminada = comandaDAO.eliminarComanda(id);
        assertNotNull(eliminada);
        assertNull(comandaDAO.consultarComanda(id));
    }

    @Test
    public void testContarComandasHoy() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(p.getId());

        long conteo = comandaDAO.contarComandasHoy();
        assertTrue(conteo >= 1);
    }

    @Test
    public void testRegistrarDetallesComanda() {
        Comanda comandaPersistida = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(comandaPersistida.getId());

        // SOLUCIÓN AL ERROR id_Producto cannot be null:
        // Debes referenciar un producto que ya exista en tu BD (ID 1 por ejemplo)
        // O crear uno que cumpla con los campos obligatorios
        Producto productoExistente = new Producto();
        productoExistente.setId(1L); 

        DetallesComanda detalle = new DetallesComanda();
        detalle.setComanda(comandaPersistida);
        detalle.setProducto(productoExistente); // <-- Crucial
        detalle.setCantidad(3);
        detalle.setSubtotal(150.0);
        detalle.setPrecioVenta(50.0);
        
        DetallesComanda resultado = comandaDAO.registrarDetallesComanda(detalle);
        assertNotNull(resultado);
        assertNotNull(resultado.getId());
    }

    @Test
    public void testConsultarDetallesComanda() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(p.getId());

        List<DetallesComanda> detalles = comandaDAO.consultarDetallesComanda(p.getId());
        assertNotNull(detalles);
    }

    @Test
    public void testEliminarDetallesDeComanda() {
        Comanda p = comandaDAO.registrarComanda(crearComandaMock());
        idsLimpieza.add(p.getId());

        assertDoesNotThrow(() -> {
            comandaDAO.eliminarDetallesDeComanda(p.getId());
        });
    }
}