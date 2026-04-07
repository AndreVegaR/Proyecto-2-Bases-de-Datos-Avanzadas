package Entidades;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Representa una mesa donde los clientes se sentarán a comer
 * El número es único en el restaurante
 */
@Entity
@Table(name = "mesas")
public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Long id;
    
    @Column(name = "disponible", nullable = false, length = 30)
    private String estadoMesa;
    
    @Column(name = "numero", nullable = false, unique = true)
    private int numero;  
    
    
    public Mesa() {}
    
    /**
     * Constructor
     */
    public Mesa(int numero, String estadoMesa) {
        this.numero = numero;
        this.estadoMesa = estadoMesa;
    }
    
    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getEstadoMesa() {
        return estadoMesa;
    }

    public void setEstadoMesa(String estadoMesa) {
        this.estadoMesa = estadoMesa;
    }
}