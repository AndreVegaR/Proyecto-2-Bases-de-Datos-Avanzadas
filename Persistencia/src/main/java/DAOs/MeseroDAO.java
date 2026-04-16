package DAOs;
import Entidades.Mesero;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
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
            throw new PersistenciaException("Error al registrar al cliente: " + e.getMessage());
        } finally {
            em.close();
        }
    }
}