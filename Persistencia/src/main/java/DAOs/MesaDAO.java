package DAOs;
import Entidades.Cliente;
import Entidades.Mesa;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Clase DAO para las mesas
 * No tiene un fin administrativo: solo es para aregarlas desde el inicio
 * 
 * @author Andre
 */
public class MesaDAO {
    
    private static MesaDAO instancia;

    //Constructor privado
    private MesaDAO() {}

    /**
     * Singleton
     * 
     * @return DAO ya listo
     */
    public static MesaDAO getInstance() {
        if (instancia == null) {
            instancia = new MesaDAO();
        }
        return instancia;
    }
    
    
    
    /**
     * Registra una mesa en la BD
     *
     * @param mesa a persistir
     * @return entidad persistida con ID generado
     */
    public Mesa registrarMesa(Mesa mesa) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(mesa);
            em.getTransaction().commit();
            return mesa;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al registrar la mesa: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Actualiza una mesa
     * 
     * @param mesaActualizada
     * @return 
     */
    public Mesa actualizarMesa(Mesa mesaActualizada) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Mesa actualizado = em.merge(mesaActualizada);
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
     * Actualiza el estado de una mesa
     * Primero la intenta encontrar con un query y si solo actúa su sí hubo coincidencia
     * De lo contrario, arroja una excepción
     * 
     * @param idMesa
     * @param nuevoEstado de la mesa
     */
    public void actualizarEstado(Long idMesa, String nuevoEstado) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            int filasActualizadas = em.createQuery("UPDATE Mesa m SET m.estadoMesa = :estado WHERE m.id = :id")
                                                  .setParameter("estado", nuevoEstado)
                                                  .setParameter("id", idMesa)
                                                  .executeUpdate();
            if (filasActualizadas == 0) {
                throw new PersistenciaException("Error al buscar la mesa con ID " + idMesa);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar el estado de la mesa: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Consulta todas las mesas registradas
     *
     * @return lista de todos las mesas
     */
    public List<Mesa> consultarMesas() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT m FROM Mesa m";
            return em.createQuery(jpql, Mesa.class).getResultList();
        }
        catch (Exception e) {
            throw new PersistenciaException("Error al consultar los clientes: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
}