package Coordinadores;
import BO.ClienteFrecuenteBO;
import DTOs.ClienteFrecuenteDTO;

/**
 * @author Angel
 * Coordinador que unifica lógica de negocio
 * Utiliza un singleton
 */
public class CoordinadorNegocio implements ICoordinadorNegocio {
    //Única instancia
    private static CoordinadorNegocio instancia;
    
    //Constructor privado
    private CoordinadorNegocio() {}
    
    /**
     * Singleton
     * Si se llama por primera vez, crea la instancia
     * Si ya existía, solo la regresa
     * 
     * @return la instancia lista
     */
    public static CoordinadorNegocio getInstance() {
        if (instancia == null) {
            instancia = new CoordinadorNegocio();
        }
        return instancia;
    }
    
    
    
    /**
     * Recibe un DTO con los datos del cliente y los guarda mediante el BO de cliente
     * 
     * @param clienteFrecuente 
     */
    @Override
    public void registrarClienteFrecuente(ClienteFrecuenteDTO clienteFrecuente){
        ClienteFrecuenteBO.getInstance().guardarCliente(clienteFrecuente);
    }
}