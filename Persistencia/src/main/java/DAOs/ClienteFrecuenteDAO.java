package DAOs;

import java.util.List;
import Entidades.ClienteFrecuente;
import conexion.ConexionBD;
import javax.persistence.EntityManager;

/**
 * DAO para la entidad ClienteFrecuente
 * @author Jazmin
 */
public class ClienteFrecuenteDAO {

    private static ClienteFrecuenteDAO instancia;

    private ClienteFrecuenteDAO() {
    }

    public static ClienteFrecuenteDAO getInstance() {
        if (instancia == null) {
            instancia = new ClienteFrecuenteDAO();
        }
        return instancia;
    }

    /**
     * Guarda un cliente frecuente
     *
     * @param cliente a persistir
     * @return entidad persistida con ID generado
     */
    public ClienteFrecuente guardarCliente(ClienteFrecuente cliente) {
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
            throw e;
        } finally {
            em.close();
        }
    }

    public ClienteFrecuente modificarCliente(ClienteFrecuente clienteModificado) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();

            ClienteFrecuente persistente = em.find(ClienteFrecuente.class, clienteModificado.getId());

            if (persistente != null) {

                persistente.setNombres(clienteModificado.getNombres());
                persistente.setApellidoPaterno(clienteModificado.getApellidoPaterno());
                persistente.setApellidoMaterno(clienteModificado.getApellidoMaterno());
                persistente.setTelefono(clienteModificado.getTelefono());
                persistente.setCorreo(clienteModificado.getCorreo());

                em.getTransaction().commit();
                return persistente;
            } else {
                throw new RuntimeException("No se encontró el cliente con ID: " + clienteModificado.getId());
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un cliente frecuente por su ID
     *
     * @param id del cliente
     * @return entidad eliminada
     */
    public ClienteFrecuente eliminarCliente(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            ClienteFrecuente cliente = em.find(ClienteFrecuente.class, id);
            if (cliente != null) {
                em.remove(cliente);
            }
            em.getTransaction().commit();
            return cliente;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los clientes frecuentes
     *
     * @return lista de clientes frecuentes
     */
    public List<ClienteFrecuente> verClientes() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM ClienteFrecuente c";
            return em.createQuery(jpql, ClienteFrecuente.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca un cliente frecuente por su ID
     *
     * @param id del cliente
     * @return entidad encontrada o null
     */
    public ClienteFrecuente buscarPorId(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(ClienteFrecuente.class, id);
        } finally {
            em.close();
        }
    }

}
