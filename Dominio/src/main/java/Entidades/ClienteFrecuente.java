package Entidades;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Representa a un cliente recurrente dentro del negocio
 * Guarda sus visitas
 */
@Entity
public class ClienteFrecuente extends Cliente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributo
    @Transient
    private double gastoTotal = 0;

    
    //Constructores
    /**
     * Constructors
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
    
    
    
    
    
    
    
    
    //ESTO SE MOVERÁ DESPUÉS A NEGOCIO
    /**
     * Asigna una comanda al cliente añadiéndola a lista y actualizando gasto total
     * 
     * @param comanda a asignar
     */
    /**
     * private void asignarComanda(Comanda comanda) {
        if (comanda == null) {
            throw new IllegalArgumentException("Comanda nula");
        }

        if (getComandas().contains(comanda)) {
            throw new IllegalStateException("Comanda ya registrada al usuario");
        }

        agregarComanda(comanda);
        gastoTotal += comanda.getTotal();
    }
     */
    
    
    
    //Getters y setters
    /**
     * Cada visita hecha representa una comanda registrada a su nombre
     *
     * @return cantidad de visitas equivalentes a la de comandas
     */
    public int getVisitas() {
        return getComandas().size();
    }
    
    public double getGastoTotal() {
        return gastoTotal;
    }

    public void setGastoTotal(double gastoTotal) {
        this.gastoTotal = gastoTotal;
    }

    /**
     * Como es atributo calculado, siempre obtiene el valor de la cantidad actualizada de gastoTotal
     * No se guarda en otros lugares, por lo que se ata a un valor establecido (gastoTotal)
     * 
     * @return puntos de fidelidad, que es el gasto total del cliente dividido entre 20, casteado a int
     */
    public int getPuntosFidelidad() {
        return (int) (gastoTotal/20);
    }
}