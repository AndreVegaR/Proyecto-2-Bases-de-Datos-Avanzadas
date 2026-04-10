package Entidades;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Representa a un cliente recurrente dentro del negocio
 * Guarda sus visitas
 */
@Entity
@Table(name = "clientes_frecuentes")
@PrimaryKeyJoinColumn(name = "id_cliente") 
@DiscriminatorValue("ClienteFrecuente")
public class ClienteFrecuente extends Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Constructores
    /**
     * Constructor completo
     * @param nombres
     * @param apellidoPaterno
     * @param apellidoMaterno
     * @param telefono
     * @param fechaRegistro
     * @param correo 
     */
    public ClienteFrecuente(String nombres, String apellidoPaterno, String apellidoMaterno, String telefono, LocalDateTime fechaRegistro, String correo) {
        super(nombres, apellidoPaterno, apellidoMaterno, telefono, fechaRegistro, correo);
     
    }
    
    /**
     * Constructor vacío
     */
    public ClienteFrecuente() {
        super();
    }
    
    
    //Getters y setters
    /**
     * Cada visita hecha representa una comanda registrada a su nombre
     *
     * @return cantidad de visitas equivalentes a la de comandas
     */
    public int getVisitas() {
        return getComandas().size();
    }
    
    //Suma el total de todas las comandas del cliente
    public double getGastoTotal() {
        List<Comanda> comandas = getComandas();
        double gastoTotal = 0.0;
        for (Comanda comanda: comandas) {
            gastoTotal += comanda.getTotal();
        }
        return gastoTotal;
    }
    
    /**
     * Como es atributo calculado, siempre obtiene el valor de la cantidad actualizada de gastoTotal
     * No se guarda en otros lugares, por lo que se ata a un valor establecido (gastoTotal)
     * 
     * @return puntos de fidelidad, que es el gasto total del cliente dividido entre 20, casteado a int
     */
    public int getPuntosFidelidad() {
        return (int) (getGastoTotal()/20);
    }
}