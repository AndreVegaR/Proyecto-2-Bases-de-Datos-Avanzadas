package DAOs;
import Entidades.Mesa;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
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
}