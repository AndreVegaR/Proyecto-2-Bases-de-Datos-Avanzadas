package DAOs;
import Entidades.Cliente;
import java.util.List;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import javax.persistence.EntityManager;

/**
 * DAO para la entidad ClienteFrecuente
 * Aplica el Principio de Sustitución de Liskov: el contrato aplica para el padre y sus hijas
 * @author Jazmin
 */
public class ClienteDAO {

    private static ClienteDAO instancia;

    private ClienteDAO() {
    }

    /**
     * Singleton
     * 
     * @return DAO ya listo
     */
    public static ClienteDAO getInstance() {
        if (instancia == null) {
            instancia = new ClienteDAO();
        }
        return instancia;
    }

    
    
    /**
     * Guarda un cliente
     *
     * @param cliente a persistir
     * @return entidad persistida con ID generado
     */
    public Cliente registrarCliente(Cliente cliente) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(cliente);
            em.getTransaction().commit();
            return cliente;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al registrar al cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    
    
    public Cliente actualizarCliente(Cliente clienteActualizado) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Cliente actualizado = em.merge(clienteActualizado);
            em.getTransaction().commit();
            return actualizado;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar al cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    /**
     * Elimina un cliente por su ID
     *
     * @param id del cliente
     * @return entidad eliminada
     */
    public Cliente eliminarCliente(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
            return cliente;
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar el cliente: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }

    
    
    /**
     * Obtiene todos los clientes de cualquier tipo
     * En la tabla solo se van a mostrar los datos genéricos de los clientes
     *
     * @return lista de todos los clientes
     */
    public List<Cliente> consultarClientes() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Cliente c";
            return em.createQuery(jpql, Cliente.class).getResultList();
        }
        catch (Exception e) {
            throw new PersistenciaException("Error al consultar los clientes: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }

    
    
    /**
     * Busca un cliente por su ID
     *
     * @param id del cliente
     * @return entidad encontrada o null
     */
    public Cliente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Cliente.class, id);
        } 
        catch (Exception e) {
            throw new PersistenciaException("Error al buscar el client con ID " + id + ": " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
}