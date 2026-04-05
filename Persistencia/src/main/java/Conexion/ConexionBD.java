package conexion;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 */
public class ConexionBD {
    private static final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ConexionPU");

    /**
     * Constructor privado para evitar instanciar la clase ConexionBD
     */
    
    private ConexionBD(){
        
    }
    
    /**
     * Metodo que genera y devuelve el entityManager.
     * @return 
     */
    public static EntityManager crearConexion(){
        return entityManagerFactory.createEntityManager();
    }
}