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
            throw new PersistenciaException("Error al registrar al cliente");
        } finally {
            em.close();
        }
    }

    
    
    /**
     * Actualiza un cliente ya existente
     * 
     * @param clienteActualizado
     * @return cliente actualizado
     */
    public Cliente actualizarCliente(Cliente clienteActualizado) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            //Obtiene el tipo de cliente del parámetro con el .getClass()
            Cliente cliente = em.find(clienteActualizado.getClass(), clienteActualizado.getId());

            //Lo actualiza si no es nulo
            if (cliente != null) {
                cliente.setNombres(clienteActualizado.getNombres());
                cliente.setApellidoPaterno(clienteActualizado.getApellidoPaterno());
                cliente.setApellidoMaterno(clienteActualizado.getApellidoMaterno());
                cliente.setTelefono(clienteActualizado.getTelefono());
                cliente.setCorreo(clienteActualizado.getCorreo());
                em.getTransaction().commit();
                return cliente;
            } else {
                throw new PersistenciaException("No se encontró el cliente con ID: " + clienteActualizado.getId());
            }
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el cliente con ID: " + clienteActualizado.getId());
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
            throw new PersistenciaException("Error al eliminar el cliente");
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
            throw new PersistenciaException("Error al consultar los clientes");
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
            throw new PersistenciaException("Error al buscar el client con ID " + id);
        }
        finally {
            em.close();
        }
    }
}