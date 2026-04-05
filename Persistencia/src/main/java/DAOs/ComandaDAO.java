package DAOs;
import Entidades.Comanda;
import Entidades.DetallesComanda;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * DAO para la entidad Comanda
 * 
 * @author Andre
 */
public class ComandaDAO {
    private static ComandaDAO instancia;

    //Constructor privado
    private ComandaDAO() {}

    /**
     * Singleton
     * 
     * @return DAO ya listo
     */
    public static ComandaDAO getInstance() {
        if (instancia == null) {
            instancia = new ComandaDAO();
        }
        return instancia;
    }
    
    
    
    /**
     * Busca una comanda por su ID
     *
     * @param id de la comanda
     * @return entidad encontrada
     */
    public Comanda consultarComanda(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            return em.find(Comanda.class, id);
        } 
        catch (Exception e) {
            throw new PersistenciaException("Error al buscar la comanda con ID " + id + ": " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
    
    
    
    /**
     * Guarda una comanda
     *
     * @param comanda a persistir
     * @return entidad persistida con ID generado
     */
    public Comanda registrarComanda(Comanda comanda) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(comanda);
            em.getTransaction().commit();
            return comanda;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al registrar la comanda: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Actualiza una comanda
     * 
     * @param comandaActualizada
     * @return la comanda actualizada
     */
    public Comanda actualizarComanda(Comanda comandaActualizada) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda actualizada = em.merge(comandaActualizada);
            em.getTransaction().commit();
            return actualizada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al actualizar la comanda: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Elimina una comanda por su ID
     *
     * @param id de la comanda
     * @return entidad eliminada
     */
    public Comanda eliminarComanda(Long id) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            Comanda comanda = em.find(Comanda.class, id);
            if (comanda != null) {
                em.remove(comanda);
            }
            em.getTransaction().commit();
            return comanda;
        }
        catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al eliminar la comanda: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
    
    
    
    /**
     * Obtiene todas las comandas registradas
     *
     * @return lista de todos las comandas
     */
    public List<Comanda> consultarComandas() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT c FROM Comanda c";
            return em.createQuery(jpql, Comanda.class).getResultList();
        }
        catch (Exception e) {
            throw new PersistenciaException("Error al consultar las comandas: " + e.getMessage());
        }
        finally {
            em.close();
        }
    } 
    
    
    
    /**
     * Cuenta todas las comandas del día de cuando se haga la consulta
     * Utiliza COUNT y compara con CURRENT_DATE
     * No cuenta el tamaño de la lista de todas la comandas porque es un crimen contra la memoria
     * 
     * @return la cantidad de comandas
     */
    public long contarComandasHoy() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT COUNT(c) FROM Comanda c WHERE c.fecha = CURRENT_DATE";
            return em.createQuery(jpql, Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Guarda un detallesComanda
     * Está aquí porque está semánticamente ligado: no existe un detalle sin su comanda
     *
     * @param detallesComanda a persistir
     * @return entidad persistida con ID generado
     */
    public DetallesComanda registrarDetallesComanda(DetallesComanda detallesComanda) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(detallesComanda);
            em.getTransaction().commit();
            return detallesComanda;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al registrar el detalle de la comanda: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Consulta todos los detalles de una comanda
     * Está aquí porque está semánticamente ligado: no existe un detalle sin su comanda
     * 
     * @param idComanda a consultarle sus detalles
     * @return los detalles
     */
    public List<DetallesComanda> consultarDetallesComanda(Long idComanda) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            String jpql = "SELECT d FROM DetalleComanda d WHERE d.comanda.id = :idComanda";
            TypedQuery<DetallesComanda> query = em.createQuery(jpql, DetallesComanda.class);
            query.setParameter("idComanda", idComanda);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}