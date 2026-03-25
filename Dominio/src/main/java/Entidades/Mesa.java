package Entidades;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representa una mesa donde los clientes se sentarán a comer
 * El número es único en el restaurante
 */
@Entity
public class Mesa implements Serializable {
    private static final long serialVersionUID = 1L;
    
    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mesa")
    private Long id;
    
    @Column(name = "disponible", nullable = false)
    private boolean disponible = true;
    
    @Column(name = "numero", nullable = false, unique = true)
    private static int numero;

    //Contador que llevará que lleva un registro de cuántas mesas hay y se asignan automáticamente en el constructor
    private static int contador = 1;
    
    
    
    /**
     * Constructor
     */
    public Mesa() {
        this.numero = contador++;
    }
    
    
    
    //Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public static int getNumero() {
        return numero;
    }

    public static void setNumero(int numero) {
        Mesa.numero = numero;
    }
}