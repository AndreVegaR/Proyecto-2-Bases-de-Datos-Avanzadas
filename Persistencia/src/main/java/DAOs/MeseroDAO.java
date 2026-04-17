package DAOs;
import Entidades.Cliente;
import Entidades.Mesero;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;

/**
 * Dao simple para meseros
 * 
 * @author Andre
 */
public class MeseroDAO {
    
    //Singleton
    private static MeseroDAO instancia = null;
    public static MeseroDAO getInstance() {
        if (instancia == null) {
            instancia = new MeseroDAO();
        }
        return instancia;
    }
    
    /**
     * Agrega un mesero en la BD
     * 
     * @param mesero
     * 
     * @return el mesero
     */
    public Mesero agregarMesero(Mesero mesero) {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.getTransaction().begin();
            em.persist(mesero);
            em.getTransaction().commit();
            return mesero;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new PersistenciaException("Error al registrar al mesero: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    
    
    /**
     * Trae todos los meseros de la BD
     * 
     * @return los meseros
     */
    public List<Mesero> consultarMeseros() {
        EntityManager em = ConexionBD.crearConexion();
        try {
            em.clear();
            String jpql = "SELECT m FROM Mesero";
            return em.createQuery(jpql, Mesero.class).getResultList(); 
        }
        catch (Exception e) {
            throw new PersistenciaException("Error al consultar los meseros: " + e.getMessage());
        }
        finally {
            em.close();
        }
    }
    
    
    
    /**
     * Consulta un mesero por su pin
     * No usa el ID porque valida el pin ingreado desde presentación
     * 
     * @param pin
     * @return 
     */
    public Mesero buscarPorPin(String pin) {
        EntityManager em = ConexionBD.crearConexion();
        String jpql = "SELECT m FROM Mesero m WHERE m.pin = :pin";
        try {
            return em.createQuery(jpql, Mesero.class)
                     .setParameter("pin", pin)
                     .getSingleResult();
        } catch (Exception e) {
            throw new PersistenciaException("Error al consultar al mesero: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}