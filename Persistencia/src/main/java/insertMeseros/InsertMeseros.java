package insertMeseros;
import DAOs.MeseroDAO;
import Entidades.Mesero;
import conexion.ConexionBD;
import excepciones.PersistenciaException;
import javax.persistence.EntityManager;

/**
 * Esta clase añade varios meseros al sistema
 * A diferencia de las mesas, se agrega directamente en persistencia
 * Esto debido a que las mesas sí tienen lógica propia, como de ocupación
 * Los meseros solo existen y no son manejados en el flujo del programa
 * 
 * @author Andre
 */
public class InsertMeseros {
    public static void main(String[] args) {
        EntityManager em = ConexionBD.crearConexion();
        
        //Datos de los meseros
        String[] nombres = {"Jazmín", "Ángel", "Andre"};
        String[] apellidosPaternos = {"Ochoa", "Pérez", "Vega"};
        String[] apellidosMaternos = {"Baldenegro", "Gaxiola", "Romero"};
        String[] pines = {"12345", "88888", "54321"};
        
        //Arreglo de arreglos!!!!!!
        String[][] arreglo = {nombres, apellidosPaternos, apellidosMaternos, pines};
        
        //Itera por cada mesero
        for (int i = 0; i < pines.length; i++) {
            
            //Lo crea y lo persiste
            Mesero mesero = new Mesero();
            mesero.setNombres(arreglo[0][i]);
            mesero.setApellidoPaterno(arreglo[1][i]);
            mesero.setApellidoMaterno(arreglo[2][i]);
            mesero.setPin(arreglo[3][i]);
            MeseroDAO.getInstance().agregarMesero(mesero);
        }
        
        //Cierra la conexión
        em.close();
    }
}